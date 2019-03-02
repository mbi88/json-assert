package com.mbi;

import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.mbi.AssertionUtils.objectsToArray;

/**
 * Compares json objects or json arrays if they are equal.
 */
public final class JsonAssert implements Assert {

    /**
     * Compare mode.
     */
    private CompareMode mode;

    /**
     * Fields to be ignored.
     */
    private Set<String> blackList;

    /**
     * Fields to be compared.
     */
    private Set<String> whiteList;

    /**
     * Sets default state before usage.
     */
    public JsonAssert() {
        // Set default mode, ignore, compare fields
        this.setDefaultState();
    }

    /**
     * Sets default state after every comparing. Every new comparison starts from scratch: previous compare mode,
     * ignore and compare fields should be reinstalled.
     */
    private void setDefaultState() {
        // Default mode
        this.mode = CompareMode.NOT_ORDERED;
        // Default fields to ignore
        this.blackList = new HashSet<>();
        // Default fields to compare
        this.whiteList = new HashSet<>();
    }

    @Override
    public void jsonEquals(final JSONObject actual, final JSONObject expected) {
        try {
            final var asserter = new EqualityAsserter();
            asserter.assertEquals(actual, expected, mode, blackList, whiteList);
        } finally {
            // Set default mode, ignore
            setDefaultState();
        }
    }

    @Override
    public void jsonEquals(final JSONArray actual, final JSONArray expected) {
        try {
            final var asserter = new EqualityAsserter();
            asserter.assertEquals(actual, expected, mode, blackList, whiteList);
        } finally {
            // Set default mode, ignore
            setDefaultState();
        }
    }

    @Override
    public void jsonEquals(final JSONArray actual, final JSONObject... expected) {
        try {
            final var asserter = new EqualityAsserter();
            asserter.assertEquals(actual, objectsToArray(expected), mode, blackList, whiteList);
        } finally {
            // Set default mode, ignore
            setDefaultState();
        }
    }

    @Override
    public void jsonEquals(final Response actual, final JSONArray expected) {
        try {
            final var asserter = new EqualityAsserter();
            asserter.assertEquals(new JSONArray(actual.asString()), expected, mode, blackList, whiteList);
        } finally {
            // Set default mode, ignore
            setDefaultState();
        }
    }

    @Override
    public void jsonEquals(final Response actual, final JSONObject expected) {
        try {
            final var asserter = new EqualityAsserter();
            asserter.assertEquals(new JSONObject(actual.asString()), expected, mode, blackList, whiteList);
        } finally {
            // Set default mode, ignore
            setDefaultState();
        }
    }

    @Override
    public void jsonEquals(final Response actual, final JSONObject... expected) {
        try {
            final var asserter = new EqualityAsserter();
            asserter.assertEquals(new JSONArray(actual.asString()), objectsToArray(expected),
                    mode, blackList, whiteList);
        } finally {
            // Set default mode, ignore
            setDefaultState();
        }
    }

    @Override
    public void jsonNotEquals(final JSONObject actual, final JSONObject expected) {
        try {
            final var asserter = new EqualityAsserter();
            asserter.assertNotEquals(actual, expected, mode, blackList, whiteList);
        } finally {
            // Set default mode, ignore
            setDefaultState();
        }
    }

    @Override
    public void jsonNotEquals(final JSONArray actual, final JSONArray expected) {
        try {
            final var asserter = new EqualityAsserter();
            asserter.assertNotEquals(actual, expected, mode, blackList, whiteList);
        } finally {
            // Set default mode, ignore
            setDefaultState();
        }
    }

    @Override
    public JsonAssert withMode(final CompareMode mode) {
        this.mode = mode;
        return this;
    }

    @Override
    public JsonAssert ignore(final String... ignoreFieldNames) {
        this.blackList = new HashSet<>(Arrays.asList(ignoreFieldNames));
        return this;
    }

    @Override
    public JsonAssert compareOnly(final String... compareFieldNames) {
        this.whiteList = new HashSet<>(Arrays.asList(compareFieldNames));
        return this;
    }
}
