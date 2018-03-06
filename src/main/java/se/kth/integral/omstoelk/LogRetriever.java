package se.kth.integral.omstoelk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Queue;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.kth.integral.azure.opinsights.Workspaces;
import se.kth.integral.azure.opinsights.models.SearchParameters;
import se.kth.integral.azure.opinsights.models.SearchResultsResponse;

public class LogRetriever implements Runnable {
    private static final int LOOK_BEHIND_FOR_LATE_INDEX_TIME = 10000;
    private static final int BACKOFF_SLEEP_TIME = 10000;
    private static final long BATCH_SIZE = 100;
    private static final Logger LOG = LoggerFactory.getLogger(QueryRetriever.class);
    private static final ObjectMapper OM = new ObjectMapper();
    private static final FifoCache<String, JsonNode> cache = new FifoCache<String, JsonNode>(10000);


    private final QueryRetriever queryRetriever;
    private final Workspaces workspaces;
    private final String resourceGroup;
    private final String workspace;
    private Queue<JsonNode> queue;

    public LogRetriever(
            final Workspaces workspaces,
            final String resourceGroup, 
            final String workspace,
            final QueryRetriever queryRetriever,
            final Queue<JsonNode> queue) {
        this.queryRetriever = queryRetriever;
        this.workspaces = workspaces;
        this.resourceGroup = resourceGroup;
        this.workspace = workspace;
        this.queue = queue;
    }

    @Override
    public void run() {
        int backoff = 0;
        int lookBehind = LOOK_BEHIND_FOR_LATE_INDEX_TIME;

        DateTime lastTimestamp;
        try {
            lastTimestamp = DateTime.parse(new String(Files.readAllBytes(Paths.get("timestamp"))));
        } catch (IOException e) {
            lastTimestamp = DateTime.now();
        }

        LOG.info("Start retrieving log entries from {}", lastTimestamp);

        while (true) {
            try {
                Thread.sleep(backoff);
            } catch (InterruptedException e) {}
 
            if (queryRetriever.getSavedSearch() == null) {
                backoff = BACKOFF_SLEEP_TIME;
                continue;
            }

            try {
                DateTime now = DateTime.now();
                SearchParameters parameters = new SearchParameters()
                        .withTop(BATCH_SIZE)
                        .withStart(lastTimestamp.minusMillis(lookBehind))
                        .withEnd(now)
                        .withQuery(queryRetriever.getSavedSearch().query());
                SearchResultsResponse searchResults = 
                        workspaces.beginGetSearchResults(resourceGroup, workspace, parameters);

                // Wind back a period of time at end of results to increase 
                // chance of catching items indexed out of order.
                if (searchResults.value().size() < BATCH_SIZE) {
                    lookBehind = LOOK_BEHIND_FOR_LATE_INDEX_TIME;
                } else {
                    lookBehind = 0;
                }

                // Assume we are at end of results and should back off 
                // before walking through results and discover otherwise.
                backoff = BACKOFF_SLEEP_TIME;

                for (Object res : searchResults.value()) {
                    JsonNode json = OM.convertValue(res, JsonNode.class);
                    String timestampStr = json.get("TimeGenerated").asText();
                    lastTimestamp = DateTime.parse(timestampStr);
//                    String id = json.get("id").asText();
                    String id = String.join(":", timestampStr, json.get("msg_s").asText());

                    if (!cache.containsKey(id)) {
                        cache.put(id, null);
                        queue.add(json);
                        // Found new entries, immediately look for more in next cycle.
                        backoff = 0;
                    }
                }
            } catch (Exception e) {
                LOG.warn("Error while getting log entries, retrying: {}", e.getMessage(), e);
            }
        }
    }
}
