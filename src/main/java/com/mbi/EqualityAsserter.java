package com.mbi;

import org.json.JSONArray;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Set;

import static com.mbi.AssertionUtils.getCommonArray;
import static com.mbi.AssertionUtils.getErrorMessage;

/**
 * Compares json equality.
 */
final class EqualityAsserter {

    private static final String NOT_EQUALS_ERROR_MESSAGE = "Objects are equal!";

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

        // Compare
        try {
            JSONAssert.assertEquals(expectedFiltered, actualFiltered, CompareMode.getCompareMode(mode));
        } catch (AssertionError error) {
            throw new AssertionError(getErrorMessage(error, expected, actual), error);
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

        // Creates common objects array of expected and actual arrays if compare mode assumes extensibility
        // of actual array. For cases when it is needed to check if actual array contains expected array.
        final JSONArray actualCommon = mode.isExtensibleArray()
                ? getCommonArray(expectedFiltered, actualFiltered)
                : actualFiltered;

        // Compare
        try {
            JSONAssert.assertEquals(expectedFiltered, actualCommon, CompareMode.getCompareMode(mode));
        } catch (AssertionError error) {
            throw new AssertionError(getErrorMessage(error, expected, actual), error);
        }
    }

    /**
     * Asserts two json objects are not equal.
     *
     * @param actual    actual json object.
     * @param expected  expected json object.
     * @param mode      compare mode.
     * @param blackList fields to be ignored on assertion.
     * @param whiteList fields to be only included on assertion.
     * @throws AssertionError if assertion failed.
     */
    public void assertNotEquals(
            final JSONObject actual,
            final JSONObject expected,
            final CompareMode mode,
            final Set<String> blackList,
            final Set<String> whiteList) {
        // Remove redundant fields
        final JSONObject actualFiltered = AssertionUtils.filterFields(actual, blackList, whiteList);
        final JSONObject expectedFiltered = AssertionUtils.filterFields(expected, blackList, whiteList);

        // Compare
        try {
            JSONAssert.assertNotEquals(NOT_EQUALS_ERROR_MESSAGE,
                    expectedFiltered,
                    actualFiltered,
                    CompareMode.getCompareMode(mode));
        } catch (AssertionError error) {
            throw new AssertionError(getErrorMessage(error, expected, actual), error);
        }
    }

    /**
     * Asserts two json arrays are not equal.
     *
     * @param actual    actual json array.
     * @param expected  expected json array.
     * @param mode      compare mode.
     * @param blackList fields to be ignored on assertion.
     * @param whiteList fields to be only included on assertion.
     * @throws AssertionError if assertion failed.
     */
    public void assertNotEquals(
            final JSONArray actual,
            final JSONArray expected,
            final CompareMode mode,
            final Set<String> blackList,
            final Set<String> whiteList) {
        // Remove redundant fields
        final JSONArray actualFiltered = AssertionUtils.filterFields(actual, blackList, whiteList);
        final JSONArray expectedFiltered = AssertionUtils.filterFields(expected, blackList, whiteList);

        // Creates common objects array of expected and actual arrays if compare mode assumes extensibility
        // of actual array. For cases when it is needed to check if actual array contains expected array.
        final JSONArray actualCommon = mode.isExtensibleArray()
                ? getCommonArray(expectedFiltered, actualFiltered)
                : actualFiltered;

        // Compare
        try {
            JSONAssert.assertNotEquals(NOT_EQUALS_ERROR_MESSAGE,
                    expectedFiltered,
                    actualCommon,
                    CompareMode.getCompareMode(mode));
        } catch (AssertionError error) {
            throw new AssertionError(getErrorMessage(error, expected, actual), error);
        }
    }
}
