package com.mbi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.HashSet;
import java.util.LinkedHashSet;

class EqualityAsserter {

    /**
     * Method to assert two json objects are equal
     *
     * @param actual   actual json object
     * @param expected expected json object
     * @param mode     compare mode
     * @param ignore   array of fields to be ignored on assertion
     */
    void assertEquals(JSONObject actual, JSONObject expected, CompareMode mode, String... ignore) {
        // Initialize new expected/actual objects to avoid removing fields from objects while assertion with ignore
        JSONObject a = Cutter.cutFields(new JSONObject(actual.toString()), ignore);
        JSONObject e = Cutter.cutFields(new JSONObject(expected.toString()), ignore);

        JSONCompareMode jsonCompareMode = CompareMode.getCompareMode(mode);

        try {
            JSONAssert.assertEquals(e, a, jsonCompareMode);
        } catch (AssertionError ae) {
            String message = ae.getMessage()
                    .concat("\n\n")
                    .concat("Expected:  " + expected.toString(4))
                    .concat("\n\n")
                    .concat("But found: " + actual.toString(4));

            throw new AssertionError(message);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Method to assert two json arrays are equal
     *
     * @param actual   actual json array
     * @param expected expected json array
     * @param mode     compare mode
     * @param ignore   array of fields to be ignored on assertion
     */
    void assertEquals(JSONArray actual, JSONArray expected, CompareMode mode, String... ignore) {
        // Initialize new expected/actual arrays to avoid removing fields from objects while assertion with ignore
        JSONArray a = Cutter.cutFields(new JSONArray(actual.toString()), ignore);
        JSONArray e = Cutter.cutFields(new JSONArray(expected.toString()), ignore);

        JSONCompareMode jsonCompareMode = CompareMode.getCompareMode(mode);

        JSONArray actualCommon;
        if (mode.isExtensibleArray()) {
            actualCommon = getEntryArray(e, a);
        } else {
            actualCommon = a;
        }

        try {
            JSONAssert.assertEquals(e, actualCommon, jsonCompareMode);
        } catch (AssertionError ae) {
            String message = ae.getMessage()
                    .concat("\n\n")
                    .concat("Expected:  " + expected.toString(4))
                    .concat("\n\n")
                    .concat("But found: " + actual.toString(4));

            throw new AssertionError(message);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * Transforms json objects array to json array
     *
     * @param jsonObjects json objects array
     * @return json array
     */
    JSONArray objectsToArray(JSONObject[] jsonObjects) {
        JSONArray expectedArray = new JSONArray();

        for (JSONObject j : jsonObjects) {
            expectedArray.put(j);
        }

        return expectedArray;
    }

    /**
     * Returns json array with objects which are included in both arrays
     *
     * @param expected expected json array
     * @param actual   actual json array
     * @return json array with common objects
     */
    private JSONArray getEntryArray(JSONArray expected, JSONArray actual) {
        JSONArray newArray = new JSONArray();
        HashSet<CustomJSONObject> actualSet = new LinkedHashSet<>();

        for (Object ao : actual) {
            CustomJSONObject actualJson = new CustomJSONObject(ao);
            for (Object eo : expected) {
                CustomJSONObject expectedJson = new CustomJSONObject(eo);
                if (actualJson.equals(expectedJson)) {
                    actualSet.add(expectedJson);
                }
            }
        }

        for (CustomJSONObject o : actualSet) {
            newArray.put(o.toJSONObject());
        }

        // Throw error if no common objects
        if (newArray.length() == 0) {
            // Cut response if length > 10000 symbols
            String actualStr = actual.toString(4).length() > 10000
                    ? actual.toString(4).substring(0, 10000)
                    : actual.toString(4);
            String expectedStr = expected.toString(4).length() > 10000
                    ? expected.toString(4).substring(0, 10000)
                    : expected.toString(4);

            throw new Error(
                    "No common objects in passed arrays!"
                            .concat("\n\n")
                            .concat("Expected:  " + expectedStr)
                            .concat("\n\n")
                            .concat("But found: " + actualStr)
            );
        }

        return newArray;
    }

    /**
     * Need for json objects equality assurance
     */
    private class CustomJSONObject {

        private Object o;

        CustomJSONObject(Object o) {
            this.o = o;
        }

        JSONObject toJSONObject() {
            return new JSONObject(this.o.toString());
        }

        @Override
        public int hashCode() {
            return this.o.hashCode();
        }

        /**
         * Compares two objects via JSONAssert.assertEquals() method. JSONCompareMode.NON_EXTENSIBLE is used by default.
         * Returns true if no assertion error exception is caught
         *
         * @param obj object to be compared
         * @return comparison result
         */
        @Override
        public boolean equals(Object obj) {
            try {
                JSONAssert.assertEquals(
                        this.toJSONObject(),
                        new JSONObject(obj.toString()),
                        JSONCompareMode.NON_EXTENSIBLE
                );
            } catch (AssertionError error) {
                return false;
            }

            return obj instanceof CustomJSONObject;
        }

        @Override
        public String toString() {
            return this.o.toString();
        }
    }
}