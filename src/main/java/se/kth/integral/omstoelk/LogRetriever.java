package se.kth.integral.omstoelk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import se.kth.integral.oms.Querys;
import se.kth.integral.oms.models.QueryResponse;
import se.kth.integral.oms.models.TableObject;

public class LogRetriever implements Runnable {
    private static final int LOOK_BEHIND_FOR_LATE_INDEX_TIME = 10000;
    private static final int BACKOFF_SLEEP_TIME = 10000;
    private static final long BATCH_SIZE = 1000;

    private static final Logger LOG = LoggerFactory.getLogger(LogRetriever.class);
    private static final FifoCache<String, Object> cache = new FifoCache<String, Object>(10000);

    private final String query;
    private final Queue<JsonObject> queue;
    private final Querys querys;

    public LogRetriever(
            final Querys querys,
            final String query,
            final Queue<JsonObject> queue) {
        this.querys = querys;
        this.query = query;
        this.queue = queue;
    }

    @Override
    public void run() {
        int backoff = 0;
        int lookBehind = LOOK_BEHIND_FOR_LATE_INDEX_TIME;

        Instant lastTimestamp;
        try {
            lastTimestamp = Instant.parse(new String(Files.readAllBytes(Paths.get("timestamp"))));
        } catch (IOException e) {
            lastTimestamp = Instant.now();
        }

        LOG.info("Start retrieving log entries from {}", lastTimestamp);

        while (true) {
            try {
                Thread.sleep(backoff);
            } catch (InterruptedException e) {}
 
            try {
                QueryResponse searchResults = querys.get(
                        String.format("%s | where TimeGenerated > %s and TimeGenerated < %s | limit %s | order by TimeGenereted asc",
                                query,
                                lastTimestamp.minusMillis(lookBehind),
                                Instant.now(),
                                BATCH_SIZE));
//                SearchParameters parameters = new SearchParameters()
//                        .withTop(BATCH_SIZE)
//                        .withStart())
//                        .withEnd(now)
//                        .withQuery(query);
//                SearchResultsResponse searchResults = 
//                        workspaces.beginGetSearchResults(resourceGroup, workspace, parameters);

                // Wind back a period of time at end of results to increase 
                // chance of catching items indexed out of order.
                if (searchResults.tables().size() < BATCH_SIZE) {
                    lookBehind = LOOK_BEHIND_FOR_LATE_INDEX_TIME;
                } else {
                    lookBehind = 0;
                }

                // Assume we are at end of results and should back off 
                // before walking through results and discover otherwise.
                backoff = BACKOFF_SLEEP_TIME;

                TableObject res = searchResults.tables().get(0);
                for (List<String> row : res.rows()) {
                    JsonObject json = new JsonObject();

                    for (int i = 0; i < row.size(); i++) {
                        json.addProperty(res.columns().get(i).name(), row.get(i));
                    }
                    String id = json.toString();

                    lastTimestamp = Instant.parse(json.get("TimeGenerated").getAsString());

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
