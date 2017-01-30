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
    private static final Logger LOG = LoggerFactory.getLogger(QueryRetriever.class);
    private static final ObjectMapper OM = new ObjectMapper();
    private static final FifoCache<String, JsonNode> cache = new FifoCache<String, JsonNode>();


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
        boolean backoff = false;
        int backtick = 10000;

        DateTime lastTimestamp;
        try {
            lastTimestamp = DateTime.parse(new String(Files.readAllBytes(Paths.get("timestamp"))));
        } catch (IOException e) {
            lastTimestamp = DateTime.now();
        }

        LOG.info("Start retrieving log entries from {}", lastTimestamp);

        while (true) {
            if (backoff) {
                try {
                    backoff = false;
                    Thread.sleep(10000);
                } catch (InterruptedException e) {}
            }

            if (queryRetriever.getSavedSearch() == null) {
                backoff = true;
                continue;
            }

            try {
                DateTime now = DateTime.now();
                SearchParameters parameters = new SearchParameters()
                        .withTop(100L)
                        .withStart(lastTimestamp.minusMillis(backtick))
                        .withEnd(now)
                        .withQuery(queryRetriever.getSavedSearch().query());
                SearchResultsResponse searchResults = 
                        workspaces.beginGetSearchResults(resourceGroup, workspace, parameters);

                backoff = true;
                backtick = 10000;
                for (Object res : searchResults.value()) {
                    JsonNode json = OM.convertValue(res, JsonNode.class);
                    lastTimestamp = DateTime.parse(json.get("TimeGenerated").asText());
                    String id = json.get("id").asText();
                    if (!cache.containsKey(id)) {
                        cache.put(id, json);
                        queue.add(json);
                        backoff = false;
                        backtick = 0;
                    }
                }
            } catch (Exception e) {
                LOG.warn("Error while getting log entries, retrying: {}", e.getMessage(), e);
            }
        }
    }
}
