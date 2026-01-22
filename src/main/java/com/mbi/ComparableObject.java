package com.mbi;

import org.apache.commons.lang3.Validate;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 * Wrapper class to compare JSON-like objects based on content rather than reference.
 * Internally uses {@link JSONAssert} with {@link JSONCompareMode#NON_EXTENSIBLE}.
 */
final class ComparableObject {

    /**
     * The object to compare. Should be convertible to JSONObject via toString().
     */
    private final Object object;

    /**
     * Constructs a wrapper for the object.
     *
     * @param object object to wrap; must not be null.
     */
    public ComparableObject(final Object object) {
        Validate.notNull(object, "Passed object can't be null");
        this.object = object;
    }

    /**
     * Converts the wrapped object to a {@link JSONObject}.
     *
     * @return JSON representation of the wrapped object.
     */
    public JSONObject toJsonObject() {
        return new JSONObject(object.toString());
    }

    /**
     * Returns the hash code of the wrapped object.
     *
     * @return hash code.
     */
    @Override
    public int hashCode() {
        return object.hashCode();
    }

    /**
     * Compares this object to another using strict JSON equality.
     * Returns true if both represent the same JSON structure and values.
     *
     * @param obj other object to compare to.
     * @return true if equal based on JSON content; false otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof ComparableObject other)) {
            return false;
        }

        return isEqual(this.toJsonObject(), new JSONObject(other.object.toString()));
    }

    /**
     * Compares two JSONObjects using NON_EXTENSIBLE mode.
     * Returns true if they are considered equal, false otherwise.
     *
     * @param json1 1st json object.
     * @param json2 2nd json object.
     * @return equality result.
     */
    private boolean isEqual(final JSONObject json1, final JSONObject json2) {
        try {
            JSONAssert.assertEquals(json1, json2, JSONCompareMode.NON_EXTENSIBLE);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    /**
     * Returns a string representation of the wrapped object.
     *
     * @return string value of the wrapped object.
     */
    @Override
    public String toString() {
        return object.toString();
    }
}
