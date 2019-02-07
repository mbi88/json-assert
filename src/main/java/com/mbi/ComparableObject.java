package com.mbi;

import org.apache.commons.lang3.Validate;
import org.json.JSONObject;

/**
 * Need for json objects equality assurance.
 */
final class ComparableObject {

    /**
     * Object to compare.
     */
    private final Object object;

    /**
     * Constructor.
     *
     * @param object object to compare.
     */
    ComparableObject(final Object object) {
        this.object = object;
    }

    /**
     * Converts object to json object.
     *
     * @return json object.
     */
    public JSONObject toJsonObject() {
        return new JSONObject(this.object.toString());
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return hash code.
     */
    @Override
    public int hashCode() {
        return this.object.hashCode();
    }

    /**
     * Compares two objects via JSONAssert.assertEquals() method. JSONCompareMode.NON_EXTENSIBLE is used by default.
     * Returns true if no assertion error exception was caught.
     *
     * @param obj object to be compared.
     * @return comparison result.
     */
    @Override
    public boolean equals(final Object obj) {
        Validate.notNull(obj, "Passed object can't be null");
        return obj instanceof ComparableObject && this.toJsonObject().similar(new JSONObject(obj.toString()));
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return this.object.toString();
    }
}
