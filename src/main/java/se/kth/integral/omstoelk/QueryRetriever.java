package se.kth.integral.omstoelk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.kth.integral.azure.opinsights.SavedSearches;
import se.kth.integral.azure.opinsights.models.SavedSearch;

public class QueryRetriever implements Runnable {
    static final private Logger LOG = LoggerFactory.getLogger(QueryRetriever.class);

    final private Statistics statistics;
    final private SavedSearches client;
    final private String workspace;
    final private String resourceGroup;
    final private String query;

    private SavedSearch savedSearch = null;

    public QueryRetriever(Statistics statistics, final SavedSearches client,
            final String resourceGroup, final String workspace, final String query) {
        if (query.contains(":")) {
            this.query = query.trim().replace(":", "|");
        } else {
            this.query = query.trim();
        }
        this.statistics = statistics;
        this.client = client;
        this.workspace = workspace;
        this.resourceGroup = resourceGroup;
    }

    @Override
    public void run() {
        while (true) {
            try {
                SavedSearch res = client.get(resourceGroup, workspace, query);

                if (savedSearch == null && res == null) {
                    LOG.info("Query {} not intiailized yet, waiting 10s", query);
                } else if (savedSearch == null) {
                    LOG.info("Initialized query: {}: {}", query, res.query());
                    savedSearch = res;
                    statistics.query = res.query();
                } else if (! savedSearch.query().equals(res.query())) {
                    LOG.info("Got new query: {}: {}", query, res.query());
                    savedSearch = res;
                    statistics.query = res.query();
                }
            } catch (Exception e) {
                LOG.warn("Error setting query: {}, retrying", e);
            }

            try {
                if (savedSearch == null) {
                    Thread.sleep(10 * 1000);
                } else {
                    Thread.sleep(600 * 1000);
                }
            } catch (Exception e) {}
        }
    }

    public SavedSearch getSavedSearch() {
        return savedSearch;
    }
}
