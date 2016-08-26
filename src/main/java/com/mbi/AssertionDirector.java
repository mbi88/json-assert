package com.mbi;

import com.jayway.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Created by mbi on 8/19/16.
 */
class AssertionDirector {

    private final Object actual;
    private final Object expected;
    private final CompareMode mode;
    private final String[] ignore;

    AssertionDirector(AssertionBuilder builder) {
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

    private void assertEquals(JSONObject expected, JSONObject actual, CompareMode mode, String[] ignore) {
        actual = Cutter.cutFields(actual, ignore);
        expected = Cutter.cutFields(expected, ignore);
        JSONCompareMode jsonCompareMode = CompareMode.getCompareMode(mode);

        try {
            JSONAssert.assertEquals(expected, actual, jsonCompareMode);
        } catch (AssertionError e) {
            String message = e.getMessage()
                    .concat("\n\n")
                    .concat("Was expected: " + expected.toString(4))
                    .concat("\n\n")
                    .concat("But found:    " + actual.toString(4));

            throw new AssertionError(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void assertEquals(JSONArray expected, JSONArray actual, CompareMode mode, String[] ignore) {
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
                    .concat("Was expected: " + expected.toString(4))
                    .concat("\n\n")
                    .concat("But found:    " + actual.toString(4));

            throw new AssertionError(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray objectsToArray(JSONObject[] jsonObjects) {
        JSONArray expectedArray = new JSONArray();

        for (JSONObject j : jsonObjects) {
            expectedArray.put(j);
        }

        return expectedArray;
    }

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

        @Override
        public boolean equals(Object obj) {
            return obj != null
                    && obj instanceof CustomJSONObject
                    && ((CustomJSONObject) obj).o.toString().equals(this.o.toString());
        }

        @Override
        public String toString() {
            return this.o.toString();
        }
    }
}