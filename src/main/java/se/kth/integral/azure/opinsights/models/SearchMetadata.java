/**
 * Code generated by Microsoft (R) AutoRest Code Generator 0.17.0.0
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package se.kth.integral.azure.opinsights.models;

import java.util.List;
import org.joda.time.DateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Metadata for search results.
 */
public class SearchMetadata {
    /**
     * The request id of the search.
     */
    @JsonProperty(value = "RequestId")
    private String searchId;

    /**
     * The search result type.
     */
    private String resultType;

    /**
     * The total number of search results.
     */
    private Long total;

    /**
     * The number of top search results.
     */
    private Long top;

    /**
     * The id of the search results request.
     */
    private String id;

    /**
     * The core summaries.
     */
    @JsonProperty(value = "CoreSummaries")
    private List<CoreSummary> coreSummaries;

    /**
     * The status of the search results.
     */
    @JsonProperty(value = "Status")
    private String status;

    /**
     * The start time for the search.
     */
    @JsonProperty(value = "StartTime")
    private DateTime startTime;

    /**
     * The time of last update.
     */
    @JsonProperty(value = "LastUpdated")
    private DateTime lastUpdated;

    /**
     * The ETag of the search results.
     */
    @JsonProperty(value = "ETag")
    private String eTag;

    /**
     * How the results are sorted.
     */
    private List<SearchSort> sort;

    /**
     * The request time.
     */
    private Long requestTime;

    /**
     * The aggregated value field.
     */
    private String aggregatedValueField;

    /**
     * The aggregated grouping fields.
     */
    private String aggregatedGroupingFields;

    /**
     * The sum of all aggregates returned in the result set.
     */
    private Long sum;

    /**
     * The max of all aggregates returned in the result set.
     */
    private Long max;

    /**
     * The schema.
     */
    private SearchMetadataSchema schema;

    /**
     * Get the searchId value.
     *
     * @return the searchId value
     */
    public String searchId() {
        return this.searchId;
    }

    /**
     * Set the searchId value.
     *
     * @param searchId the searchId value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withSearchId(String searchId) {
        this.searchId = searchId;
        return this;
    }

    /**
     * Get the resultType value.
     *
     * @return the resultType value
     */
    public String resultType() {
        return this.resultType;
    }

    /**
     * Set the resultType value.
     *
     * @param resultType the resultType value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withResultType(String resultType) {
        this.resultType = resultType;
        return this;
    }

    /**
     * Get the total value.
     *
     * @return the total value
     */
    public Long total() {
        return this.total;
    }

    /**
     * Set the total value.
     *
     * @param total the total value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withTotal(Long total) {
        this.total = total;
        return this;
    }

    /**
     * Get the top value.
     *
     * @return the top value
     */
    public Long top() {
        return this.top;
    }

    /**
     * Set the top value.
     *
     * @param top the top value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withTop(Long top) {
        this.top = top;
        return this;
    }

    /**
     * Get the id value.
     *
     * @return the id value
     */
    public String id() {
        return this.id;
    }

    /**
     * Set the id value.
     *
     * @param id the id value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Get the coreSummaries value.
     *
     * @return the coreSummaries value
     */
    public List<CoreSummary> coreSummaries() {
        return this.coreSummaries;
    }

    /**
     * Set the coreSummaries value.
     *
     * @param coreSummaries the coreSummaries value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withCoreSummaries(List<CoreSummary> coreSummaries) {
        this.coreSummaries = coreSummaries;
        return this;
    }

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
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * Get the startTime value.
     *
     * @return the startTime value
     */
    public DateTime startTime() {
        return this.startTime;
    }

    /**
     * Set the startTime value.
     *
     * @param startTime the startTime value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withStartTime(DateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    /**
     * Get the lastUpdated value.
     *
     * @return the lastUpdated value
     */
    public DateTime lastUpdated() {
        return this.lastUpdated;
    }

    /**
     * Set the lastUpdated value.
     *
     * @param lastUpdated the lastUpdated value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withLastUpdated(DateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    /**
     * Get the eTag value.
     *
     * @return the eTag value
     */
    public String eTag() {
        return this.eTag;
    }

    /**
     * Set the eTag value.
     *
     * @param eTag the eTag value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withETag(String eTag) {
        this.eTag = eTag;
        return this;
    }

    /**
     * Get the sort value.
     *
     * @return the sort value
     */
    public List<SearchSort> sort() {
        return this.sort;
    }

    /**
     * Set the sort value.
     *
     * @param sort the sort value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withSort(List<SearchSort> sort) {
        this.sort = sort;
        return this;
    }

    /**
     * Get the requestTime value.
     *
     * @return the requestTime value
     */
    public Long requestTime() {
        return this.requestTime;
    }

    /**
     * Set the requestTime value.
     *
     * @param requestTime the requestTime value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withRequestTime(Long requestTime) {
        this.requestTime = requestTime;
        return this;
    }

    /**
     * Get the aggregatedValueField value.
     *
     * @return the aggregatedValueField value
     */
    public String aggregatedValueField() {
        return this.aggregatedValueField;
    }

    /**
     * Set the aggregatedValueField value.
     *
     * @param aggregatedValueField the aggregatedValueField value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withAggregatedValueField(String aggregatedValueField) {
        this.aggregatedValueField = aggregatedValueField;
        return this;
    }

    /**
     * Get the aggregatedGroupingFields value.
     *
     * @return the aggregatedGroupingFields value
     */
    public String aggregatedGroupingFields() {
        return this.aggregatedGroupingFields;
    }

    /**
     * Set the aggregatedGroupingFields value.
     *
     * @param aggregatedGroupingFields the aggregatedGroupingFields value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withAggregatedGroupingFields(String aggregatedGroupingFields) {
        this.aggregatedGroupingFields = aggregatedGroupingFields;
        return this;
    }

    /**
     * Get the sum value.
     *
     * @return the sum value
     */
    public Long sum() {
        return this.sum;
    }

    /**
     * Set the sum value.
     *
     * @param sum the sum value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withSum(Long sum) {
        this.sum = sum;
        return this;
    }

    /**
     * Get the max value.
     *
     * @return the max value
     */
    public Long max() {
        return this.max;
    }

    /**
     * Set the max value.
     *
     * @param max the max value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withMax(Long max) {
        this.max = max;
        return this;
    }

    /**
     * Get the schema value.
     *
     * @return the schema value
     */
    public SearchMetadataSchema schema() {
        return this.schema;
    }

    /**
     * Set the schema value.
     *
     * @param schema the schema value to set
     * @return the SearchMetadata object itself.
     */
    public SearchMetadata withSchema(SearchMetadataSchema schema) {
        this.schema = schema;
        return this;
    }

}
