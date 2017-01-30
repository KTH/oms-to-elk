/**
 * Code generated by Microsoft (R) AutoRest Code Generator 0.17.0.0
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package se.kth.integral.azure.opinsights.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Describes a storage account connection.
 */
public class StorageAccount {
    /**
     * The Azure Resource Manager ID of the storage account resource.
     */
    @JsonProperty(required = true)
    private String id;

    /**
     * The storage account key.
     */
    @JsonProperty(required = true)
    private String key;

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
     * @return the StorageAccount object itself.
     */
    public StorageAccount withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Get the key value.
     *
     * @return the key value
     */
    public String key() {
        return this.key;
    }

    /**
     * Set the key value.
     *
     * @param key the key value to set
     * @return the StorageAccount object itself.
     */
    public StorageAccount withKey(String key) {
        this.key = key;
        return this;
    }

}