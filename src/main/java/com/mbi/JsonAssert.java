package com.mbi;

import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.mbi.AssertionUtils.objectsToArray;

/**
 * Performs JSON equality and inequality assertions.
 * <p>
 * Supports different comparison modes ({@link CompareMode}) and filtering via ignore/include fields.
 * Can compare {@link JSONObject}, {@link JSONArray} or {@link Response} bodies.
 * Automatically resets its state after each assertion.
 * Designed for use in test scenarios where JSON structure or partial matching is required.
 */
public final class JsonAssert implements Assert {

    /**
     * Assertion engine that handles low-level equality logic.
     */
    private final EqualityAsserter asserter = new EqualityAsserter();

    /**
     * Comparison mode. Defaults to {@link CompareMode#NOT_ORDERED}.
     */
    private CompareMode mode = CompareMode.NOT_ORDERED;

    /**
     * Fields to exclude from comparison.
     */
    private Set<String> blackList = new HashSet<>();

    /**
     * Fields to include in comparison (others are ignored).
     */
    private Set<String> whiteList = new HashSet<>();

    /**
     * Compares two JSON objects for equality.
     */
    @Override
    public void jsonEquals(final JSONObject actual, final JSONObject expected) {
        perform(() -> asserter.assertEquals(actual, expected, mode, blackList, whiteList));
    }

    /**
     * Compares two JSON arrays for equality.
     */
    @Override
    public void jsonEquals(final JSONArray actual, final JSONArray expected) {
        perform(() -> asserter.assertEquals(actual, expected, mode, blackList, whiteList));
    }

    /**
     * Compares a JSON array with one or more expected JSON objects.
     * Treats expected objects as array elements.
     */
    @Override
    public void jsonEquals(final JSONArray actual, final JSONObject... expected) {
        perform(() -> asserter.assertEquals(actual, objectsToArray(expected), mode, blackList, whiteList));
    }

    /**
     * Compares a REST-assured response (array body) with expected JSON array.
     */
    @Override
    public void jsonEquals(final Response actual, final JSONArray expected) {
        perform(() -> asserter.assertEquals(new JSONArray(actual.asString()), expected, mode, blackList, whiteList));
    }

    /**
     * Compares a REST-assured response (object body) with expected JSON object.
     */
    @Override
    public void jsonEquals(final Response actual, final JSONObject expected) {
        perform(() -> asserter.assertEquals(new JSONObject(actual.asString()), expected, mode, blackList, whiteList));
    }

    /**
     * Compares a REST-assured response (array body) with expected JSON objects (as array).
     */
    @Override
    public void jsonEquals(final Response actual, final JSONObject... expected) {
        perform(() -> asserter.assertEquals(
                new JSONArray(actual.asString()),
                objectsToArray(expected),
                mode, blackList, whiteList));
    }

    /**
     * Asserts that two JSON objects are not equal.
     */
    @Override
    public void jsonNotEquals(final JSONObject actual, final JSONObject expected) {
        perform(() -> asserter.assertNotEquals(actual, expected, mode, blackList, whiteList));
    }

    /**
     * Asserts that two JSON arrays are not equal.
     */
    @Override
    public void jsonNotEquals(final JSONArray actual, final JSONArray expected) {
        perform(() -> asserter.assertNotEquals(actual, expected, mode, blackList, whiteList));
    }

    /**
     * Sets the comparison mode (e.g., ordered vs. unordered, extensible vs. strict).
     *
     * @param mode comparison mode
     * @return this for method chaining
     */
    @Override
    public JsonAssert withMode(final CompareMode mode) {
        this.mode = mode;
        return this;
    }

    /**
     * Sets field names to ignore during comparison.
     *
     * @param ignoreFieldNames field paths to ignore
     * @return this for method chaining
     */
    @Override
    public JsonAssert ignore(final String... ignoreFieldNames) {
        this.blackList = new HashSet<>(Arrays.asList(ignoreFieldNames));
        return this;
    }

    /**
     * Sets field names to exclusively compare (ignore all others).
     *
     * @param compareFieldNames field paths to include
     * @return this for method chaining
     */
    @Override
    public JsonAssert compareOnly(final String... compareFieldNames) {
        this.whiteList = new HashSet<>(Arrays.asList(compareFieldNames));
        return this;
    }

    /**
     * Executes assertion logic and resets internal state.
     *
     * @param assertion lambda that performs the actual comparison
     */
    private void perform(final Runnable assertion) {
        try {
            assertion.run();
        } finally {
            reset();
        }
    }

    /**
     * Resets compare mode and field filters to their default state.
     * Ensures assertions are stateless between uses, every new comparison starts from scratch: previous compare mode,
     * ignore and compare fields should be reinstalled.
     */
    private void reset() {
        // Default mode
        this.mode = CompareMode.NOT_ORDERED;
        // Default fields to ignore
        this.blackList.clear();
        // Default fields to compare
        this.whiteList.clear();
    }
}
