package se.kth.integral.omstoelk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StatisticsLogger implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(StatisticsLogger.class);
    private static final ObjectMapper OM = new ObjectMapper();
    private final Statistics statistics;

    public StatisticsLogger(Statistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                LOG.info(OM.writeValueAsString(statistics));
                statistics.reset();
            }
        } catch (JsonProcessingException e) {
            LOG.error("Failed to log statistics: {}", e.getMessage(), e);
        }
    }
}
