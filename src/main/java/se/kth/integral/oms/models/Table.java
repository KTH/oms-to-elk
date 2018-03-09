/**
 * Code generated by Microsoft (R) AutoRest Code Generator.
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package se.kth.integral.oms.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A query response table.
 * Contains the columns and rows for one table in a query response.
 */
public class Table {
    /**
     * The name of the table.
     */
    @JsonProperty(value = "name", required = true)
    private String name;

    /**
     * The list of columns in this table.
     */
    @JsonProperty(value = "columns", required = true)
    private List<Column> columns;

    /**
     * The resulting rows from this query.
     */
    @JsonProperty(value = "rows", required = true)
    private List<List<String>> rows;

    /**
     * Get the name value.
     *
     * @return the name value
     */
    public String name() {
        return this.name;
    }

    /**
     * Set the name value.
     *
     * @param name the name value to set
     * @return the Table object itself.
     */
    public Table withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the columns value.
     *
     * @return the columns value
     */
    public List<Column> columns() {
        return this.columns;
    }

    /**
     * Set the columns value.
     *
     * @param columns the columns value to set
     * @return the Table object itself.
     */
    public Table withColumns(List<Column> columns) {
        this.columns = columns;
        return this;
    }

    /**
     * Get the rows value.
     *
     * @return the rows value
     */
    public List<List<String>> rows() {
        return this.rows;
    }

    /**
     * Set the rows value.
     *
     * @param rows the rows value to set
     * @return the Table object itself.
     */
    public Table withRows(List<List<String>> rows) {
        this.rows = rows;
        return this;
    }

}
