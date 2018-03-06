package se.kth.integral.omstoelk;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import se.kth.infosys.lumberjack.Event;

public class EventBatch {
    private static final Logger LOG = LoggerFactory.getLogger(EventBatch.class);
    private static final ObjectMapper OM = new ObjectMapper();

    private List<Event> events;
    private Instant timestamp;

    public static EventBatch readEvents(final int batchSize, final Queue<JsonObject> queue) {
        EventBatch batch = new EventBatch();
        batch.events = new ArrayList<Event>(batchSize);

        for (int i = 0; i < batchSize; i++) {
            try {
                JsonObject json = queue.poll();
                if (json == null) {
                    break;
                }
                batch.events.add(new Event().addField("line", OM.writeValueAsString(json)));
                batch.timestamp = Instant.parse(json.get("TimeGenerated").getAsString());
            } catch (UnsupportedEncodingException | JsonProcessingException e) {
                LOG.error("Failed to encode json: {}", e.getMessage());
            }
        }
        return batch;
    }

    public List<Event> getEvents() {
        return events;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
