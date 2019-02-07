package com.mbi;

import com.github.wnameless.json.flattener.FlattenMode;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import org.apache.commons.lang3.Validate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Utils class.
 */
@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
final class AssertionUtils {

    /**
     * Fields separator in flattened json.
     */
    private static final String FIELDS_SEPARATOR = ".";

    /**
     * Removes child fields from set.
     * Example: for set [a.b, a, a.b.c, b, c.d] result will be [a, b, c.d].
     */
    private static Function<Set<String>, Set<String>> getParentFields = set -> {
        final Set<String> result = new HashSet<>();

        for (String key : set) {
            // Get parent field name
            final String parent = key.split("\\.")[0];

            // Add to result only parents or add original field
            if (set.contains(parent)) {
                result.add(parent);
            } else {
                result.add(key);
            }
        }

        return result;
    };

    /**
     * Checks if field is present in flattened json.
     */
    private static BiPredicate<String, Set<String>> isParent = (flattenedJsonKey, parentFields) -> parentFields
            .stream()
            .anyMatch(parentField -> flattenedJsonKey.startsWith(parentField.concat(FIELDS_SEPARATOR))
                    || flattenedJsonKey.equalsIgnoreCase(parentField));

    /**
     * Prohibits init.
     */
    private AssertionUtils() {
    }

    /**
     * Returns json object without fields from black list and with only fields from white list.
     * Method supports jay way json path as a field name.
     *
     * @param json      json with redundant fields.
     * @param blackList redundant fields to be removed.
     * @param whiteList fields to be kept.
     * @return result json without redundant fields
     * @throws IllegalArgumentException if result json = {}.
     */
    public static JSONObject filterFields(
            final JSONObject json,
            final Set<String> blackList,
            final Set<String> whiteList) {
        JSONObject result = new JSONObject(json.toString());
        // Flattened json
        final String flattenStr = new JsonFlattener(result.toString())
                .withFlattenMode(FlattenMode.KEEP_ARRAYS)
                .flatten();
        final JSONObject flattenJson = new JSONObject(flattenStr);
        // Flattened json fields
        final Set<String> flattenedJsonKeys = new JSONObject(flattenStr).keySet();

        for (String flattenedJsonKey : flattenedJsonKeys) {
            // Do not remove fields from white list
            if (whiteList.contains(flattenedJsonKey)) {
                continue;
            }

            // Remove all except white list
            if (!isParent.test(flattenedJsonKey, getParentFields.apply(whiteList))
                    && !getParentFields.apply(whiteList).isEmpty()) {
                flattenJson.remove(flattenedJsonKey);
            }

            // Remove black list
            if (isParent.test(flattenedJsonKey, getParentFields.apply(blackList))
                    && !getParentFields.apply(blackList).isEmpty()) {
                flattenJson.remove(flattenedJsonKey);
            }
        }

        result = new JSONObject(JsonUnflattener.unflatten(flattenJson.toString()));
        // Check result != {}
        Validate.isTrue(!result.similar(new JSONObject()), "You removed all fields from json!"
                + "\nOriginal: \n" + json.toString(2));

        return result;
    }

    /**
     * Returns json array without fields from black list and with only fields from white list.
     *
     * @param json      json with redundant fields.
     * @param blackList redundant fields to be removed.
     * @param whiteList fields to be kept.
     * @return result json without redundant fields
     */
    public static JSONArray filterFields(
            final JSONArray json,
            final Set<String> blackList,
            final Set<String> whiteList) {
        final JSONArray result = new JSONArray();
        for (int i = 0; i < json.length(); i++) {
            // Json array may consist of not json objects (e.g.: [1, 2, 5]).
            // In this case return original json array
            try {
                JSONObject tmpJson = new JSONObject(json.get(i).toString());
                tmpJson = filterFields(tmpJson, blackList, whiteList);
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
    public static JSONArray objectsToArray(final JSONObject... jsonObjects) {
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
    public static String getErrorMessage(
            final AssertionError error,
            final JSONObject expected,
            final JSONObject actual) {
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
    public static String getErrorMessage(final AssertionError error, final JSONArray expected, final JSONArray actual) {
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
    public static JSONArray getCommonArray(final JSONArray expected, final JSONArray actual) {
        final JSONArray commonArray = new JSONArray();

        // Get a set of expected objects that are common for actual
        final HashSet<ComparableObject> commonSet = new LinkedHashSet<>();
        for (Object eo : expected) {
            final ComparableObject expectedJson = new ComparableObject(eo);
            for (Object ao : actual) {
                final ComparableObject actualJson = new ComparableObject(ao);
                if (expectedJson.equals(actualJson)) {
                    commonSet.add(expectedJson);
                }
            }
        }

        for (ComparableObject o : commonSet) {
            commonArray.put(o.toJsonObject());
        }

        return commonArray;
    }
}
