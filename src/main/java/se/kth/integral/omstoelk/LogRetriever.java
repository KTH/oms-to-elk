package se.kth.integral.omstoelk;

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
        while (true) {
            if (queryRetriever.getSavedSearch() == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                continue;
            }

            DateTime starttime = DateTime.now();
            SearchParameters parameters = new SearchParameters()
                    .withTop(100L)
                    .withStart(starttime.minusDays(1))
                    .withEnd(starttime)
                    .withQuery(queryRetriever.getSavedSearch().query());
            SearchResultsResponse searchResults = 
                    workspaces.beginGetSearchResults(resourceGroup, workspace, parameters);
            LOG.debug("Got {} hits", searchResults.value().size());

            for (Object res : searchResults.value()) {
                queue.add(OM.convertValue(res, JsonNode.class));
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {}
        }
    }
}
