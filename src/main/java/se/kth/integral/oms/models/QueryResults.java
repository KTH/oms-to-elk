/**
 * Code generated by Microsoft (R) AutoRest Code Generator.
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package se.kth.integral.oms.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A query response.
 * Contains the tables, columns &amp; rows resulting from a query.
 */
public class QueryResults {
    /**
     * The list of tables, columns and rows.
     */
    @JsonProperty(value = "tables", required = true)
    private List<Table> tables;

    /**
     * Get the tables value.
     *
     * @return the tables value
     */
    public List<Table> tables() {
        return this.tables;
    }

    /**
     * Set the tables value.
     *
     * @param tables the tables value to set
     * @return the QueryResults object itself.
     */
    public QueryResults withTables(List<Table> tables) {
        this.tables = tables;
        return this;
    }

}
