package com.mbi;

import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 * Need for json objects equality assurance.
 */
final class CompareObject {

    /**
     * Compare object.
     */
    private final Object object;

    /**
     * Constructor.
     *
     * @param object compare object.
     */
    CompareObject(final Object object) {
        this.object = object;
    }

    /**
     * Converts object to json object.
     *
     * @return json object.
     */
    JSONObject toJsonObject() {
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
     * Returns true if no assertion error exception is caught.
     *
     * @param obj object to be compared.
     * @return comparison result.
     */
    @Override
    public boolean equals(final Object obj) {
        assert obj != null : "passed object can't be null";
        return obj instanceof CompareObject && isEqual(this.toJsonObject(), new JSONObject(obj.toString()));
    }

    /**
     * Internal equality assertion for overriding equals method.
     *
     * @param json1 json object.
     * @param json2 json object.
     * @return equality result.
     */
    private boolean isEqual(final JSONObject json1, final JSONObject json2) {
        boolean equals;
        try {
            JSONAssert.assertEquals(json1, json2, JSONCompareMode.NON_EXTENSIBLE);
            equals = true;
        } catch (AssertionError error) {
            equals = false;
        }

        return equals;
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
