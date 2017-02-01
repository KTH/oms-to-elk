package se.kth.integral.omstoelk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class Statistics {
    @JsonProperty("Type")
    public final String type = "OmsToElkStatistics";
    
    @JsonProperty("Messages")
    public long messages;

    @JsonProperty("TotalMessages")
    public long totalMessages;

    @JsonProperty("CurrentTimestamp")
    public String currentTimestamp;

    @JsonProperty("Query")
    public String query;

    public synchronized void addCount(long messages) {
        this.messages += messages;
        this.totalMessages += messages;
    }

    public synchronized void reset() {
        this.messages = 0;
    }
}
