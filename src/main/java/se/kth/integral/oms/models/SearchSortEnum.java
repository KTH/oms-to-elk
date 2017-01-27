/**
 * Code generated by Microsoft (R) AutoRest Code Generator 0.17.0.0
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package se.kth.integral.oms.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for SearchSortEnum.
 */
public final class SearchSortEnum {
    /** Static value asc for SearchSortEnum. */
    public static final SearchSortEnum ASC = new SearchSortEnum("asc");

    /** Static value desc for SearchSortEnum. */
    public static final SearchSortEnum DESC = new SearchSortEnum("desc");

    private String value;

    /**
     * Creates a custom value for SearchSortEnum.
     * @param value the custom value
     */
    public SearchSortEnum(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SearchSortEnum)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        SearchSortEnum rhs = (SearchSortEnum) obj;
        if (value == null) {
            return rhs.value == null;
        } else {
            return value.equals(rhs.value);
        }
    }
}
