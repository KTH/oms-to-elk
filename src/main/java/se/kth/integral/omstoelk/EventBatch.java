package se.kth.integral.omstoelk;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.kth.infosys.lumberjack.Event;

public class EventBatch {
    private static final Logger LOG = LoggerFactory.getLogger(EventBatch.class);
    private static final ObjectMapper OM = new ObjectMapper();

    private List<Event> events;
    private DateTime timestamp;

    public static EventBatch readEvents(final int batchSize, final Queue<JsonNode> queue) {
        EventBatch batch = new EventBatch();
        batch.events = new ArrayList<Event>(batchSize);

        for (int i = 0; i < batchSize; i++) {
            try {
                JsonNode json = queue.poll();
                if (json == null) {
                    break;
                }
                batch.events.add(new Event().addField("line", OM.writeValueAsString(json)));
                batch.timestamp = DateTime.parse(json.get("TimeGenerated").asText());
            } catch (UnsupportedEncodingException | JsonProcessingException e) {
                LOG.error("Failed to encode json: {}", e.getMessage());
            }
        }
        return batch;
    }

    public List<Event> getEvents() {
        return events;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }
}
