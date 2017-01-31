/**
 * Code generated by Microsoft (R) AutoRest Code Generator 0.17.0.0
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package se.kth.integral.azure.opinsights.models;

import org.joda.time.DateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Parameters specifying the search query and range.
 */
public class SearchParameters {
    /**
     * The number to get from the top.
     */
    private Long top;

    /**
     * The highlight that looks for all occurences of a string.
     */
    private SearchHighlight highlight;

    /**
     * The query to search.
     */
    @JsonProperty(required = true)
    private String query;

    /**
     * The start date filter, so the only query results returned are after
     * this date.
     */
    private DateTime start;

    /**
     * The end date filter, so the only query results returned are before this
     * date.
     */
    private DateTime end;

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
     * @return the SearchParameters object itself.
     */
    public SearchParameters withTop(Long top) {
        this.top = top;
        return this;
    }

    /**
     * Get the highlight value.
     *
     * @return the highlight value
     */
    public SearchHighlight highlight() {
        return this.highlight;
    }

    /**
     * Set the highlight value.
     *
     * @param highlight the highlight value to set
     * @return the SearchParameters object itself.
     */
    public SearchParameters withHighlight(SearchHighlight highlight) {
        this.highlight = highlight;
        return this;
    }

    /**
     * Get the query value.
     *
     * @return the query value
     */
    public String query() {
        return this.query;
    }

    /**
     * Set the query value.
     *
     * @param query the query value to set
     * @return the SearchParameters object itself.
     */
    public SearchParameters withQuery(String query) {
        this.query = query;
        return this;
    }

    /**
     * Get the start value.
     *
     * @return the start value
     */
    public DateTime start() {
        return this.start;
    }

    /**
     * Set the start value.
     *
     * @param start the start value to set
     * @return the SearchParameters object itself.
     */
    public SearchParameters withStart(DateTime start) {
        this.start = start;
        return this;
    }

    /**
     * Get the end value.
     *
     * @return the end value
     */
    public DateTime end() {
        return this.end;
    }

    /**
     * Set the end value.
     *
     * @param end the end value to set
     * @return the SearchParameters object itself.
     */
    public SearchParameters withEnd(DateTime end) {
        this.end = end;
        return this;
    }

}
