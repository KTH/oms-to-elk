/**
 * Code generated by Microsoft (R) AutoRest Code Generator 0.17.0.0
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package se.kth.integral.oms.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The core summary of a search.
 */
public class CoreSummary {
    /**
     * The status of a core summary.
     */
    @JsonProperty(value = "Status")
    private String status;

    /**
     * The number of documents of a core summary.
     */
    @JsonProperty(value = "NumberOfDocuments", required = true)
    private long numberOfDocuments;

    /**
     * Get the status value.
     *
     * @return the status value
     */
    public String status() {
        return this.status;
    }

    /**
     * Set the status value.
     *
     * @param status the status value to set
     * @return the CoreSummary object itself.
     */
    public CoreSummary withStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * Get the numberOfDocuments value.
     *
     * @return the numberOfDocuments value
     */
    public long numberOfDocuments() {
        return this.numberOfDocuments;
    }

    /**
     * Set the numberOfDocuments value.
     *
     * @param numberOfDocuments the numberOfDocuments value to set
     * @return the CoreSummary object itself.
     */
    public CoreSummary withNumberOfDocuments(long numberOfDocuments) {
        this.numberOfDocuments = numberOfDocuments;
        return this;
    }

}
