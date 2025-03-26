package com.mbi;

import org.json.JSONArray;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Set;

import static com.mbi.AssertionUtils.getCommonArray;
import static com.mbi.AssertionUtils.getErrorMessage;

/**
 * Low-level engine for asserting JSON equality and inequality.
 * <p>
 * Provides filtering logic (ignore/include fields), supports array extensibility and ordering options
 * via {@link CompareMode}. All comparisons are delegated to JSONAssert.
 */
final class EqualityAsserter {

    private static final String NOT_EQUALS_ERROR_MESSAGE = "Objects are equal!";

    /**
     * Asserts that two JSON objects are equal based on the specified comparison mode and field filters.
     *
     * @param actual    actual JSON object
     * @param expected  expected JSON object
     * @param mode      comparison mode (e.g., strict, non-extensible)
     * @param blackList field names to ignore
     * @param whiteList field names to compare only
     * @throws AssertionError if the objects are not equal
     */
    public void assertEquals(
            final JSONObject actual,
            final JSONObject expected,
            final CompareMode mode,
            final Set<String> blackList,
            final Set<String> whiteList) {
        // Remove redundant fields
        final var actualFiltered = AssertionUtils.filterFields(actual, blackList, whiteList);
        final var expectedFiltered = AssertionUtils.filterFields(expected, blackList, whiteList);

        // Compare
        try {
            JSONAssert.assertEquals(expectedFiltered, actualFiltered, CompareMode.getCompareMode(mode));
        } catch (AssertionError error) {
            throw new AssertionError(getErrorMessage(error, expected, actual), error);
        }
    }

    /**
     * Asserts that two JSON arrays are equal based on the comparison mode and filters.
     * <p>
     * Supports array extensibility (partial matching) if allowed by mode.
     *
     * @param actual    actual JSON array
     * @param expected  expected JSON array
     * @param mode      comparison mode (ordered, extensible)
     * @param blackList fields to ignore
     * @param whiteList fields to include
     * @throws AssertionError if the arrays are not equal
     */
    public void assertEquals(
            final JSONArray actual,
            final JSONArray expected,
            final CompareMode mode,
            final Set<String> blackList,
            final Set<String> whiteList) {
        // Remove redundant fields
        final var actualFiltered = AssertionUtils.filterFields(actual, blackList, whiteList);
        final var expectedFiltered = AssertionUtils.filterFields(expected, blackList, whiteList);

        // Creates common objects array of expected and actual arrays if compare mode assumes extensibility
        // of actual array. For cases when it is needed to check if actual array contains expected array.
        final var actualToCompare = mode.isExtensibleArray()
                ? getCommonArray(expectedFiltered, actualFiltered)
                : actualFiltered;

        // Compare
        try {
            JSONAssert.assertEquals(expectedFiltered, actualToCompare, CompareMode.getCompareMode(mode));
        } catch (AssertionError error) {
            throw new AssertionError(getErrorMessage(error, expected, actual), error);
        }
    }

    /**
     * Asserts that two JSON objects are NOT equal.
     *
     * @param actual    actual JSON object
     * @param expected  expected JSON object
     * @param mode      comparison mode
     * @param blackList fields to ignore
     * @param whiteList fields to include
     * @throws AssertionError if the objects are equal
     */
    public void assertNotEquals(
            final JSONObject actual,
            final JSONObject expected,
            final CompareMode mode,
            final Set<String> blackList,
            final Set<String> whiteList) {
        // Remove redundant fields
        final var actualFiltered = AssertionUtils.filterFields(actual, blackList, whiteList);
        final var expectedFiltered = AssertionUtils.filterFields(expected, blackList, whiteList);

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
     * Asserts that two JSON arrays are NOT equal.
     *
     * @param actual    actual JSON array
     * @param expected  expected JSON array
     * @param mode      comparison mode
     * @param blackList fields to ignore
     * @param whiteList fields to include
     * @throws AssertionError if the arrays are equal
     */
    public void assertNotEquals(
            final JSONArray actual,
            final JSONArray expected,
            final CompareMode mode,
            final Set<String> blackList,
            final Set<String> whiteList) {
        // Remove redundant fields
        final var actualFiltered = AssertionUtils.filterFields(actual, blackList, whiteList);
        final var expectedFiltered = AssertionUtils.filterFields(expected, blackList, whiteList);

        // Creates common objects array of expected and actual arrays if compare mode assumes extensibility
        // of actual array. For cases when it is needed to check if actual array contains expected array.
        final var actualToCompare = mode.isExtensibleArray()
                ? getCommonArray(expectedFiltered, actualFiltered)
                : actualFiltered;

        // Compare
        try {
            JSONAssert.assertNotEquals(NOT_EQUALS_ERROR_MESSAGE,
                    expectedFiltered,
                    actualToCompare,
                    CompareMode.getCompareMode(mode));
        } catch (AssertionError error) {
            throw new AssertionError(getErrorMessage(error, expected, actual), error);
        }
    }
}
