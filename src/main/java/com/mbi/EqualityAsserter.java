package com.mbi;

import org.json.JSONArray;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.Set;

import static com.mbi.AssertionUtils.getCommonArray;
import static com.mbi.AssertionUtils.getErrorMessage;

/**
 * Compares json equality.
 */
final class EqualityAsserter {

    /**
     * Asserts two json objects are equal.
     *
     * @param actual    actual json object.
     * @param expected  expected json object.
     * @param mode      compare mode.
     * @param blackList fields to be ignored on assertion.
     * @param whiteList fields to be only included on assertion.
     * @throws AssertionError if assertion failed.
     */
    public void assertEquals(
            final JSONObject actual,
            final JSONObject expected,
            final CompareMode mode,
            final Set<String> blackList,
            final Set<String> whiteList) {
        // Remove redundant fields
        final JSONObject actualFiltered = AssertionUtils.filterFields(actual, blackList, whiteList);
        final JSONObject expectedFiltered = AssertionUtils.filterFields(expected, blackList, whiteList);

        // Get compare mode
        final JSONCompareMode jsonCompareMode = CompareMode.getCompareMode(mode);

        // Compare
        try {
            JSONAssert.assertEquals(expectedFiltered, actualFiltered, jsonCompareMode);
        } catch (AssertionError error) {
            throw new AssertionError(getErrorMessage(error, expectedFiltered, actualFiltered));
        }
    }

    /**
     * Asserts two json arrays are equal.
     *
     * @param actual    actual json array.
     * @param expected  expected json array.
     * @param mode      compare mode.
     * @param blackList fields to be ignored on assertion.
     * @param whiteList fields to be only included on assertion.
     * @throws AssertionError if assertion failed.
     */
    public void assertEquals(
            final JSONArray actual,
            final JSONArray expected,
            final CompareMode mode,
            final Set<String> blackList,
            final Set<String> whiteList) {
        // Remove redundant fields
        final JSONArray actualFiltered = AssertionUtils.filterFields(actual, blackList, whiteList);
        final JSONArray expectedFiltered = AssertionUtils.filterFields(expected, blackList, whiteList);

        // Get compare mode
        final JSONCompareMode jsonCompareMode = CompareMode.getCompareMode(mode);

        // Creates common objects array of expected and actual arrays if compare mode assumes extensibility
        // of actual array. For cases when it is needed to check if actual array contains expected array.
        final JSONArray actualCommon = mode.isExtensibleArray()
                ? getCommonArray(expectedFiltered, actualFiltered)
                : actualFiltered;

        // Compare
        try {
            JSONAssert.assertEquals(expectedFiltered, actualCommon, jsonCompareMode);
        } catch (AssertionError error) {
            throw new AssertionError(getErrorMessage(error, expectedFiltered, actualCommon));
        }
    }
}
