package se.kth.integral.omstoelk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.kth.infosys.lumberjack.Event;
import se.kth.infosys.lumberjack.ProtocolAdapter;
import se.kth.infosys.lumberjack.protocol.LumberjackClient;
import se.kth.infosys.lumberjack.util.AdapterException;

public class LogForwarder implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(LogForwarder.class);
    private static final ObjectMapper OM = new ObjectMapper();

    private final Queue<JsonNode> queue;
    private final String server;
    private final int port;
    private final String keystore;

    private ProtocolAdapter logstash;

    public LogForwarder(Queue<JsonNode> queue, final String server, final int port, final String keystore) throws IOException {
        this.queue = queue;
        this.server = server;
        this.port = port;
        this.keystore = keystore;
    }

    private void connect() throws IOException {
        try {
            this.logstash.close();
        } catch (Exception e) {}

        this.logstash = new LumberjackClient(keystore, server, port, 10000);
    }

    @Override
    public void run() {
        boolean backoff = false;

        while (true) {
            if (backoff) {
                try {
                    backoff = false;
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {}
            }

            try {
                if (logstash == null) {
                    connect();
                }
            } catch (Exception e) {
                LOG.warn("Error connecting to logstash server: {}", e.getMessage());
                backoff = true;
                continue;
            }

            List<Event> events = new ArrayList<Event>(100);

            for (int i = 0; i < 100; i++) {
                try {
                    JsonNode json = queue.poll();
                    if (json == null) {
                        backoff = true;
                        break;
                    }
                    events.add(new Event().addField("line", OM.writeValueAsString(json)));
                } catch (UnsupportedEncodingException | JsonProcessingException e) {
                    LOG.error("Failed to encode json: {}", e.getMessage());
                }
            }

            try {
                if (! events.isEmpty()) {
                    logstash.sendEvents(events);
                    LOG.debug("Sent {} messages.", events.size());
                }
            } catch (AdapterException e) {
                LOG.warn("Error communicating with logstash server, retrying: {}", e.getMessage());
                try {
                    logstash.close();
                    Thread.sleep(1000);
                } catch (Exception e1) {
                    logstash = null;
                }
            }
        }
    }

}
