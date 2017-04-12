package com.mbi;

import com.jayway.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonAssert implements Assert {

    private CompareMode mode;
    private String[] ignore;

    public JsonAssert() {
        // Set default mode, ignore
        setDefault();
    }

    private void setDefault() {
        // Default mode
        mode = CompareMode.NOT_ORDERED;
        // Default fields to ignore
        ignore = new String[]{""};
    }

    public void jsonEquals(JSONObject actual, JSONObject expected) {
        EqualityAsserter asserter = new EqualityAsserter();
        asserter.assertEquals(actual, expected, mode, ignore);
        // Set default mode, ignore
        setDefault();
    }

    public void jsonEquals(JSONArray actual, JSONArray expected) {
        EqualityAsserter asserter = new EqualityAsserter();
        asserter.assertEquals(actual, expected, mode, ignore);
        // Set default mode, ignore
        setDefault();
    }

    public void jsonEquals(JSONArray actual, JSONObject[] expected) {
        EqualityAsserter asserter = new EqualityAsserter();
        asserter.assertEquals(actual, asserter.objectsToArray(expected), mode, ignore);
        // Set default mode, ignore
        setDefault();
    }

    public void jsonEquals(Response actual, JSONArray expected) {
        EqualityAsserter asserter = new EqualityAsserter();
        asserter.assertEquals(new JSONArray(actual.asString()), expected, mode, ignore);
        // Set default mode, ignore
        setDefault();
    }

    public void jsonEquals(Response actual, JSONObject expected) {
        EqualityAsserter asserter = new EqualityAsserter();
        asserter.assertEquals(new JSONObject(actual.asString()), expected, mode, ignore);
        // Set default mode, ignore
        setDefault();
    }

    public void jsonEquals(Response actual, JSONObject[] expected) {
        EqualityAsserter asserter = new EqualityAsserter();
        asserter.assertEquals(new JSONArray(actual.asString()), asserter.objectsToArray(expected), mode, ignore);
        // Set default mode, ignore
        setDefault();
    }

    public JsonAssert ignore(String... ignore) {
        this.ignore = ignore;
        return this;
    }

    public JsonAssert withMode(CompareMode mode) {
        this.mode = mode;
        return this;
    }
}