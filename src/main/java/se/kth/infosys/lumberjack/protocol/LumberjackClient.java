package se.kth.infosys.lumberjack.protocol;

/*
 * Copyright (c) 2017 Kungliga Tekniska högskolan
 * Copyright (c) 2015 Didier Fetter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Socket;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.io.HexDump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.kth.infosys.lumberjack.Event;
import se.kth.infosys.lumberjack.ProtocolAdapter;
import se.kth.infosys.lumberjack.util.AdapterException;

public class LumberjackClient implements ProtocolAdapter {
    private final static Logger logger = LoggerFactory.getLogger(LumberjackClient.class);
    private final static byte PROTOCOL_VERSION = 0x31;
    private final static byte FRAME_ACK = 0x41;
    private final static byte FRAME_WINDOW_SIZE = 0x57;
    private final static byte FRAME_DATA = 0x44;
    private final static byte FRAME_COMPRESSED = 0x43;

    private Socket socket;
    private SSLSocket sslSocket;
    private KeyStore keyStore;
    private String server;
    private int port;
    private DataOutputStream output;
    private DataInputStream input;
    private int sequence = 1;

    public LumberjackClient(String keyStoreFile, String server, int port, int timeout) throws IOException {
        this.server = server;
        this.port = port;

        try {
            if(keyStoreFile == null) {
                throw new IOException("Key store not configured");
            }
            if(server == null) {
                throw new IOException("Server address not configured");
            }

            keyStore = KeyStore.getInstance("JKS");
            InputStream keystoreStream = this.getClass().getClassLoader().getResourceAsStream(keyStoreFile);
            keyStore.load(keystoreStream, null);
            keystoreStream.close();

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            SSLSocketFactory socketFactory = context.getSocketFactory();
            socket = new Socket();
            socket.connect(new InetSocketAddress(InetAddress.getByName(server), port), timeout);
            sslSocket = (SSLSocket)socketFactory.createSocket(socket, server, port, true);
            sslSocket.setUseClientMode(true);
            sslSocket.startHandshake();

            output = new DataOutputStream(new BufferedOutputStream(sslSocket.getOutputStream()));
            input = new DataInputStream(sslSocket.getInputStream());

            logger.info("Connected to {}:{}", server, port);
        } catch(IOException e) {
            throw e;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int sendWindowSizeFrame(int size) throws IOException {
        output.writeByte(PROTOCOL_VERSION);
        output.writeByte(FRAME_WINDOW_SIZE);
        output.writeInt(size);
        output.flush();
        logger.trace("Sending window size frame: {} frames", size);
        return 6;
    }

    private int sendDataFrame(DataOutputStream output, Map<String,byte[]> keyValues) throws IOException {
        output.writeByte(PROTOCOL_VERSION);
        output.writeByte(FRAME_DATA);
        output.writeInt(sequence++);
        output.writeInt(keyValues.size());
        int bytesSent = 10;
        for(String key : keyValues.keySet()) {
            int keyLength = key.length();
            output.writeInt(keyLength);
            bytesSent += 4;
            output.write(key.getBytes());
            bytesSent += keyLength;
            byte[] value = keyValues.get(key);
            output.writeInt(value.length);
            bytesSent += 4;
            output.write(value);
            bytesSent += value.length;
        }
        output.flush();
        return bytesSent;
    }

    public int sendDataFrameInSocket(Map<String,byte[]> keyValues) throws IOException {
        return sendDataFrame(output, keyValues);
    }

    public int sendCompressedFrame(List<Map<String,byte[]>> keyValuesList) throws IOException {
        output.writeByte(PROTOCOL_VERSION);
        output.writeByte(FRAME_COMPRESSED);

        ByteArrayOutputStream uncompressedBytes = new ByteArrayOutputStream();
        DataOutputStream uncompressedOutput = new DataOutputStream(uncompressedBytes);
        for(Map<String,byte[]> keyValues : keyValuesList) {
            logger.trace("Adding data frame");
            sendDataFrame(uncompressedOutput, keyValues);
        }
        uncompressedOutput.close();
        Deflater compressor = new Deflater();
        byte[] uncompressedData = uncompressedBytes.toByteArray();
        logger.trace("Deflating data: {} bytes", uncompressedData.length);
        if (logger.isTraceEnabled()) {
            HexDump.dump(uncompressedData, 0, System.out, 0);
        }
        compressor.setInput(uncompressedData);
        compressor.finish();

        ByteArrayOutputStream compressedBytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while(!compressor.finished()) {
            int count = compressor.deflate(buffer);
            compressedBytes.write(buffer, 0, count);
        }
        compressedBytes.close();
        byte[] compressedData = compressedBytes.toByteArray();
        logger.trace("Deflated data: {} bytes", compressor.getTotalOut());
        if (logger.isTraceEnabled()) {
            HexDump.dump(compressedData, 0, System.out, 0);
        }

        output.writeInt(compressor.getTotalOut());
        output.write(compressedData);
        output.flush();

        logger.trace("Sending compressed frame: {} frames", keyValuesList.size());
        return 6 + compressor.getTotalOut();
    }

    public int readAckFrame() throws ProtocolException, IOException {
        byte protocolVersion = input.readByte();
        if (protocolVersion != PROTOCOL_VERSION) {
            throw new ProtocolException("Protocol version should be 1, received " + protocolVersion);
        }
        byte frameType = input.readByte();
        if(frameType != FRAME_ACK) {
            throw new ProtocolException("Frame type should be Ack, received " + frameType);
        }
        int sequenceNumber = input.readInt();
        logger.trace("Received ack sequence: {}", sequenceNumber);
        return sequenceNumber;
    }

    public int sendEvents(List<Event> eventList) throws AdapterException {
        try {
            int beginSequence = sequence;
            int numberOfEvents = eventList.size();
            logger.debug("Sending {} events", numberOfEvents);
            sendWindowSizeFrame(numberOfEvents);
            List<Map<String,byte[]>> keyValuesList = new ArrayList<Map<String,byte[]>>(numberOfEvents);
            for(Event event : eventList) {
                keyValuesList.add(event.getKeyValues());
            }
            sendCompressedFrame(keyValuesList);
            while(readAckFrame() < (sequence - 1) ) {}
            return sequence - beginSequence;
        } catch(Exception e) {
            throw new AdapterException(e);
        }
    }

    public void close() throws AdapterException {
        try {
            sslSocket.close();
        } catch(Exception e) {
            throw new AdapterException(e);
        }
        logger.info("Connection to {}:{} closed", server, port);
    }

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public boolean isConnected() {
        return sslSocket != null && sslSocket.isConnected();
    }

    public Socket getSocket() {
        return sslSocket;
    }
}