/**
 * Code generated by Microsoft (R) AutoRest Code Generator 0.17.0.0
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package se.kth.integral.oms.models;


/**
 * Details for a search error.
 */
public class SearchError {
    /**
     * The error type.
     */
    private String type;

    /**
     * The error message.
     */
    private String message;

    /**
     * Get the type value.
     *
     * @return the type value
     */
    public String type() {
        return this.type;
    }

    /**
     * Set the type value.
     *
     * @param type the type value to set
     * @return the SearchError object itself.
     */
    public SearchError withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Get the message value.
     *
     * @return the message value
     */
    public String message() {
        return this.message;
    }

    /**
     * Set the message value.
     *
     * @param message the message value to set
     * @return the SearchError object itself.
     */
    public SearchError withMessage(String message) {
        this.message = message;
        return this;
    }

}
