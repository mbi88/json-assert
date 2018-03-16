package com.mbi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Utils class.
 */
final class AssertionUtils {

    /**
     * Prohibits init.
     */
    private AssertionUtils() {
    }

    /**
     * Returns json object without passed fields.
     *
     * @param json   json with redundant fields
     * @param fields redundant fields to be removed
     * @return result json without redundant fields
     */
    static JSONObject cutFields(final JSONObject json, final String... fields) {
        for (String field : fields) {
            json.remove(field);
        }

        return json;
    }

    /**
     * Returns json array without passed fields.
     *
     * @param json   json with redundant fields.
     * @param fields redundant fields to be removed.
     * @return result json without redundant fields
     */
    static JSONArray cutFields(final JSONArray json, final String... fields) {
        final JSONArray result = new JSONArray();
        for (int i = 0; i < json.length(); i++) {
            // Json array may consist of not json objects (e.g.: [1, 2, 5]).
            // In this case return original json array
            try {
                final JSONObject tmpJson = new JSONObject(json.get(i).toString());
                for (String field : fields) {
                    tmpJson.remove(field);
                }

                result.put(tmpJson);
            } catch (JSONException je) {
                return json;
            }
        }

        return result;
    }

    /**
     * Transforms json objects array to json array.
     *
     * @param jsonObjects json objects.
     * @return json array.
     */
    static JSONArray objectsToArray(final JSONObject[] jsonObjects) {
        final JSONArray expectedArray = new JSONArray();

        for (JSONObject j : jsonObjects) {
            expectedArray.put(j);
        }

        return expectedArray;
    }

    /**
     * Returns error message.
     *
     * @param error    assertion error.
     * @param expected expected json object.
     * @param actual   actual json object.
     * @return formatted error message.
     */
    static String getErrorMessage(final AssertionError error, final JSONObject expected, final JSONObject actual) {
        return formatMessage(error, expected.toString(4), actual.toString(4));
    }

    /**
     * Returns error message.
     *
     * @param error    assertion error.
     * @param expected expected json array.
     * @param actual   actual json array.
     * @return formatted error message.
     */
    static String getErrorMessage(final AssertionError error, final JSONArray expected, final JSONArray actual) {
        return formatMessage(error, expected.toString(4), actual.toString(4));
    }

    /**
     * Formats error message.
     *
     * @param error    assertion error.
     * @param expected expected json as string with indentations.
     * @param actual   actual json as string with indentations.
     * @return error message.
     */
    private static String formatMessage(final AssertionError error, final String expected, final String actual) {
        return String.format("%s%n%nExpected: %s%n%nBut found: %s", error.getMessage(), expected, actual);
    }


    /**
     * Returns json array with objects which are included in both arrays.
     *
     * @param expected expected json array.
     * @param actual   actual json array.
     * @return json array with common objects.
     */
    static JSONArray getCommonArray(final JSONArray expected, final JSONArray actual) {
        final JSONArray commonArray = new JSONArray();

        // Get a set of expected objects that are common for actual
        final HashSet<CompareObject> commonSet = new LinkedHashSet<>();
        for (Object eo : expected) {
            final CompareObject expectedJson = new CompareObject(eo);
            for (Object ao : actual) {
                final CompareObject actualJson = new CompareObject(ao);
                if (expectedJson.equals(actualJson)) {
                    commonSet.add(expectedJson);
                }
            }
        }

        for (CompareObject o : commonSet) {
            commonArray.put(o.toJsonObject());
        }

        return commonArray;
    }
}
