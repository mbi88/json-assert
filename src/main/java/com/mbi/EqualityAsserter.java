package com.mbi;

import org.json.JSONArray;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static com.mbi.AssertionUtils.getCommonArray;
import static com.mbi.AssertionUtils.getErrorMessage;

/**
 * Compares json equality.
 */
final class EqualityAsserter {

    /**
     * Method to assert two json objects are equal.
     *
     * @param actual   actual json object.
     * @param expected expected json object.
     * @param mode     compare mode.
     * @param ignore   array of fields to be ignored on assertion.
     * @throws AssertionError if assertion failed.
     */
    void assertEquals(
            final JSONObject actual,
            final JSONObject expected,
            final CompareMode mode,
            final String... ignore) {
        // Remove redundant fields
        // Initialize new expected/actual objects to avoid removing fields from objects while assertion with ignore
        JSONObject actualWithoutNotNeededFields = AssertionUtils.cutFields(new JSONObject(actual.toString()), ignore);
        JSONObject expWithoutNotNeededFields = AssertionUtils.cutFields(new JSONObject(expected.toString()), ignore);

        // Get compare mode
        JSONCompareMode jsonCompareMode = CompareMode.getCompareMode(mode);

        // Compare
        try {
            JSONAssert.assertEquals(expWithoutNotNeededFields, actualWithoutNotNeededFields, jsonCompareMode);
        } catch (AssertionError error) {
            throw new AssertionError(getErrorMessage(error, expected, actual));
        }
    }

    /**
     * Method to assert two json arrays are equal.
     *
     * @param actual   actual json array.
     * @param expected expected json array.
     * @param mode     compare mode.
     * @param ignore   array of fields to be ignored on assertion.
     * @throws AssertionError if assertion failed.
     */
    void assertEquals(
            final JSONArray actual,
            final JSONArray expected,
            final CompareMode mode,
            final String... ignore) {
        // Remove redundant fields
        // Initialize new expected/actual arrays to avoid removing fields from objects while assertion with ignore
        JSONArray actualWithoutNotNeededFields = AssertionUtils.cutFields(new JSONArray(actual.toString()), ignore);
        JSONArray expWithoutNotNeededFields = AssertionUtils.cutFields(new JSONArray(expected.toString()), ignore);

        // Get compare mode
        JSONCompareMode jsonCompareMode = CompareMode.getCompareMode(mode);

        // Creates common objects array of expected and actual arrays if compare mode assumes extensibility
        // of actual array. For cases when it is needed to check if actual array contains expected array.
        JSONArray actualCommon = mode.isExtensibleArray()
                ? getCommonArray(expWithoutNotNeededFields, actualWithoutNotNeededFields)
                : actualWithoutNotNeededFields;

        // Compare
        try {
            JSONAssert.assertEquals(expWithoutNotNeededFields, actualCommon, jsonCompareMode);
        } catch (AssertionError error) {
            throw new AssertionError(getErrorMessage(error, expected, actual));
        }
    }
}