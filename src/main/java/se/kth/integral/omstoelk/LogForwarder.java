package se.kth.integral.omstoelk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import se.kth.infosys.lumberjack.ProtocolAdapter;
import se.kth.infosys.lumberjack.protocol.LumberjackClient;
import se.kth.infosys.lumberjack.util.AdapterException;

public class LogForwarder implements Runnable {
    private static final int SOCKET_TIMEOUT = 60000;
    private static final int BACKOFF_SLEEP_TIME = 10000;
    private static final int BATCH_SIZE = 100;
    private static final Logger LOG = LoggerFactory.getLogger(LogForwarder.class);

    private final Statistics statistics;
    private final Queue<JsonNode> queue;
    private final String server;
    private final int port;
    private final String keystore;

    private FileOutputStream timestampFile;
    private ProtocolAdapter logstash;

    public LogForwarder(Statistics statistics, Queue<JsonNode> queue, final String server, final int port, final String keystore) throws IOException {
        this.statistics = statistics;
        this.queue = queue;
        this.server = server;
        this.port = port;
        this.keystore = keystore;
    }

    private void connect() throws IOException {
        try {
            logstash.close();
        } catch (Exception e) {}

        this.logstash = new LumberjackClient(keystore, server, port, SOCKET_TIMEOUT);
        this.logstash.getSocket().setKeepAlive(true);
        this.logstash.getSocket().setSoTimeout(SOCKET_TIMEOUT);
    }

    private void storeTimestamp(String timestamp) {
        try {
            if (timestampFile == null) {
                timestampFile = new FileOutputStream(new File("timestamp"));
            }
            timestampFile.getChannel().position(0);
            timestampFile.write(timestamp.getBytes(), 0, timestamp.length());
            timestampFile.flush();
            statistics.currentTimestamp = timestamp;
        } catch (IOException e) {
            LOG.error("Error saving timestamp: {}", e.getMessage(), e);
            timestampFile = null;
        }
    }

    @Override
    public void run() {
        while (true) {
            boolean backoff = false;
            boolean success = false;

            EventBatch batch = EventBatch.readEvents(BATCH_SIZE, queue);

            while (! success) {
                try {
                    if (logstash == null || ! logstash.isConnected()) {
                        connect();
                    }

                    if (! batch.getEvents().isEmpty()) {
                        logstash.sendEvents(batch.getEvents());
                        LOG.debug("Sent {} messages.", batch.getEvents().size());
                        statistics.addCount(batch.getEvents().size());
                        storeTimestamp(batch.getTimestamp().toString());
                    } else {
                        backoff = true;
                    }
                    success = true;
                } catch (AdapterException | IOException e) {
                    LOG.warn("Error sending log entries to logstash, retrying: {}", e.getMessage());
                    backoff = true;
                    try {
                        logstash.close();
                        logstash = null;
                    } catch (Exception e1) {
                        logstash = null;
                    }
                }

                if (backoff || batch.getEvents().size() < BATCH_SIZE) {
                    try {
                        backoff = false;
                        Thread.sleep(BACKOFF_SLEEP_TIME);
                    } catch (InterruptedException e1) {}
                }
            }
        }
    }
}
