package com.mbi;

import com.jayway.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.HashSet;
import java.util.LinkedHashSet;

class AssertDirector {

    private final Object actual;
    private final Object expected;
    private final CompareMode mode;
    private final String[] ignore;

    AssertDirector(AssertBuilder builder) {
        this.actual = builder.getActual();
        this.expected = builder.getExpected();
        this.mode = builder.getMode();
        this.ignore = builder.getIgnore();
    }

    void doAssertion() {
        // JSONArray - JSONArray
        if (actual instanceof JSONArray && expected instanceof JSONArray) {
            assertEquals((JSONArray) expected, (JSONArray) actual, mode, ignore);
        }
        // JSONObject - JSONObject
        else if (actual instanceof JSONObject && expected instanceof JSONObject) {
            assertEquals((JSONObject) expected, (JSONObject) actual, mode, ignore);
        }
        // JSONArray - JSONObject[]
        else if (actual instanceof JSONArray && expected instanceof JSONObject[]) {
            assertEquals(objectsToArray((JSONObject[]) expected), (JSONArray) actual, mode, ignore);
        }
        // Response - JSONArray
        else if (actual instanceof Response && expected instanceof JSONArray) {
            assertEquals((JSONArray) expected, new JSONArray(actual.toString()), mode, ignore);
        }
        // Response - JSONObject
        else if (actual instanceof Response && expected instanceof JSONObject) {
            assertEquals((JSONObject) expected, new JSONObject(actual.toString()), mode, ignore);
        }
        // Response - JSONObject[]
        else if (actual instanceof Response && expected instanceof JSONObject[]) {
            assertEquals(objectsToArray((JSONObject[]) expected), new JSONArray(actual.toString()), mode, ignore);
        } else {
            throw new IllegalArgumentException("Error arguments passed");
        }
    }

    /**
     * Method to assert two json objects are equal
     *
     * @param actual   actual json object
     * @param expected expected json object
     * @param mode     compare mode
     * @param ignore   array of fields to be ignored on assertion
     */
    private void assertEquals(JSONObject actual, JSONObject expected, CompareMode mode, String... ignore) {
        actual = Cutter.cutFields(actual, ignore);
        expected = Cutter.cutFields(expected, ignore);
        JSONCompareMode jsonCompareMode = CompareMode.getCompareMode(mode);

        try {
            JSONAssert.assertEquals(expected, actual, jsonCompareMode);
        } catch (AssertionError e) {
            String message = e.getMessage()
                    .concat("\n\n")
                    .concat("Expected:  " + expected.toString(4))
                    .concat("\n\n")
                    .concat("But found: " + actual.toString(4));

            throw new AssertionError(message);
        } catch (JSONException e) {
            e.printStackTrace();
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
    private void assertEquals(JSONArray actual, JSONArray expected, CompareMode mode, String... ignore) {
        actual = Cutter.cutFields(actual, ignore);
        expected = Cutter.cutFields(expected, ignore);
        JSONCompareMode jsonCompareMode = CompareMode.getCompareMode(mode);

        if (mode.isExtensibleArray()) {
            actual = getEntryArray(expected, actual);
        }

        try {
            JSONAssert.assertEquals(expected, actual, jsonCompareMode);
        } catch (AssertionError e) {
            String message = e.getMessage()
                    .concat("\n\n")
                    .concat("Expected:  " + expected.toString(4))
                    .concat("\n\n")
                    .concat("But found: " + actual.toString(4));

            throw new AssertionError(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transforms json objects array to json array
     *
     * @param jsonObjects json objects array
     * @return json array
     */
    private JSONArray objectsToArray(JSONObject[] jsonObjects) {
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