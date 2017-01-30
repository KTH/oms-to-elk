/**
 * Code generated by Microsoft (R) AutoRest Code Generator 0.17.0.0
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package se.kth.integral.azure.opinsights.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Value object for schema results.
 */
public class SearchSchemaValue {
    /**
     * The name of the schema.
     */
    private String name;

    /**
     * The display name of the schema.
     */
    private String displayName;

    /**
     * The type.
     */
    private String type;

    /**
     * The boolean that indicates the field is searchable as free text.
     */
    @JsonProperty(required = true)
    private boolean indexed;

    /**
     * The boolean that indicates whether or not the field is stored.
     */
    @JsonProperty(required = true)
    private boolean stored;

    /**
     * The boolean that indicates whether or not the field is a facet.
     */
    @JsonProperty(required = true)
    private boolean facet;

    /**
     * The array of workflows containing the field.
     */
    private List<String> ownerType;

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
     * @return the SearchSchemaValue object itself.
     */
    public SearchSchemaValue withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the displayName value.
     *
     * @return the displayName value
     */
    public String displayName() {
        return this.displayName;
    }

    /**
     * Set the displayName value.
     *
     * @param displayName the displayName value to set
     * @return the SearchSchemaValue object itself.
     */
    public SearchSchemaValue withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

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
     * @return the SearchSchemaValue object itself.
     */
    public SearchSchemaValue withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Get the indexed value.
     *
     * @return the indexed value
     */
    public boolean indexed() {
        return this.indexed;
    }

    /**
     * Set the indexed value.
     *
     * @param indexed the indexed value to set
     * @return the SearchSchemaValue object itself.
     */
    public SearchSchemaValue withIndexed(boolean indexed) {
        this.indexed = indexed;
        return this;
    }

    /**
     * Get the stored value.
     *
     * @return the stored value
     */
    public boolean stored() {
        return this.stored;
    }

    /**
     * Set the stored value.
     *
     * @param stored the stored value to set
     * @return the SearchSchemaValue object itself.
     */
    public SearchSchemaValue withStored(boolean stored) {
        this.stored = stored;
        return this;
    }

    /**
     * Get the facet value.
     *
     * @return the facet value
     */
    public boolean facet() {
        return this.facet;
    }

    /**
     * Set the facet value.
     *
     * @param facet the facet value to set
     * @return the SearchSchemaValue object itself.
     */
    public SearchSchemaValue withFacet(boolean facet) {
        this.facet = facet;
        return this;
    }

    /**
     * Get the ownerType value.
     *
     * @return the ownerType value
     */
    public List<String> ownerType() {
        return this.ownerType;
    }

    /**
     * Set the ownerType value.
     *
     * @param ownerType the ownerType value to set
     * @return the SearchSchemaValue object itself.
     */
    public SearchSchemaValue withOwnerType(List<String> ownerType) {
        this.ownerType = ownerType;
        return this;
    }

}