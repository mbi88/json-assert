package com.mbi;

import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.Objects;

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
        if (Objects.isNull(obj)) {
            return false;
        }

        try {
            JSONAssert.assertEquals(
                    this.toJsonObject(),
                    new JSONObject(obj.toString()),
                    JSONCompareMode.NON_EXTENSIBLE);
        } catch (AssertionError error) {
            return false;
        }

        return obj instanceof CompareObject;
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
