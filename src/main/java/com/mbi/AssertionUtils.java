package com.mbi;

import com.github.wnameless.json.flattener.FlattenMode;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.function.BiFunction;
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
     * Checks if field is present in flattened json.
     */
    private static BiPredicate<String, Set<String>> testKeyIsParent = (flattenedJsonKey, parentFields) -> parentFields
            .stream()
            .anyMatch(parentField -> flattenedJsonKey.startsWith(parentField.concat(FIELDS_SEPARATOR))
                    || flattenedJsonKey.equalsIgnoreCase(parentField));

    /**
     * Split field by fields separator.
     */
    private static Function<String, String[]> splitKeys = s -> s.split("\\.");

    /**
     * Returns children of parent from set.
     */
    private static BiFunction<Set<String>, String, List<String>> getChildren = (parentFields, parent) -> {
        final List<String> list = new ArrayList<>();
        parentFields.forEach(s -> {
            if (s.startsWith(parent)) {
                list.add(s);
            }
        });
        return list;
    };

    /**
     * Returns min dots count in string elements of list.
     * ["as.ew.er", "ds.sd', "asd.a"] -> 1
     * ["as.ew.er", "ds.sd', "asd"] -> 0
     */
    private static Function<List<String>, Integer> getMinDotsCount = children -> {
        final var ref = new Object() {
            private int minDotsCount = Integer.MAX_VALUE;

            public void setMinDotsCount(final int minDotsCount) {
                this.minDotsCount = minDotsCount;
            }
        };

        children.forEach(child ->
                ref.setMinDotsCount(Math.min(StringUtils.countMatches(child, FIELDS_SEPARATOR), ref.minDotsCount)));

        return ref.minDotsCount;
    };

    private static Function<List<String>, Set<String>> getParentInList = children -> {
        final Set<String> result = new HashSet<>();
        final int minDotsCount = getMinDotsCount.apply(children);

        for (var child : children) {
            final var keys = splitKeys.apply(child);
            final var value = new StringBuilder();

            for (int i = 0; i <= minDotsCount; i++) {
                value.append(keys[i]).append(FIELDS_SEPARATOR);
            }

            // Remove last field separator
            final String v = value.toString().substring(0, value.toString().length() - 1);
            result.add(v);
        }

        return result;
    };

    /**
     * Removes child fields from set.
     * Example: for set [a.b, a, a.b.c, b, c.d] result will be [a, b, c.d].
     */
    @SuppressWarnings("PMD.AvoidProtectedFieldInFinalClass")
    protected static Function<Set<String>, Set<String>> getParentFields = set -> {
        final Set<String> result = new HashSet<>();
        set.forEach(key -> {
            final var parent = splitKeys.apply(key)[0];
            final var children = getChildren.apply(set, parent);
            result.addAll(getParentInList.apply(children));
        });
        return result;
    };

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
    public static JSONObject filterFields(final JSONObject json, final Set<String> blackList,
                                          final Set<String> whiteList) {
        var result = new JSONObject(json.toString());
        // Flattened json
        final var flattenStr = new JsonFlattener(result.toString())
                .withFlattenMode(FlattenMode.KEEP_ARRAYS)
                .flatten();
        final var flattenJson = new JSONObject(flattenStr);
        // Flattened json fields
        final var flattenedJsonKeys = new JSONObject(flattenStr).keySet();

        for (var flattenedJsonKey : flattenedJsonKeys) {
            // Do not remove fields from white list
            if (whiteList.contains(flattenedJsonKey)) {
                continue;
            }

            // Remove all except white list
            if (!getParentFields.apply(whiteList).isEmpty()
                    && !testKeyIsParent.test(flattenedJsonKey, getParentFields.apply(whiteList))) {
                flattenJson.remove(flattenedJsonKey);
            }

            // Remove black list
            if (!getParentFields.apply(blackList).isEmpty()
                    && testKeyIsParent.test(flattenedJsonKey, getParentFields.apply(blackList))) {
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
    public static JSONArray filterFields(final JSONArray json, final Set<String> blackList,
                                         final Set<String> whiteList) {
        final var result = new JSONArray();
        for (int i = 0; i < json.length(); i++) {
            // Json array may consist of not json objects (e.g.: [1, 2, 5]).
            // In this case return original json array
            try {
                var tmpJson = new JSONObject(json.get(i).toString());
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
        final var expectedArray = new JSONArray();

        for (var j : jsonObjects) {
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
    public static String getErrorMessage(final AssertionError error, final JSONObject expected,
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
        final var commonArray = new JSONArray();

        // Get a set of expected objects that are common for actual
        final Set<ComparableObject> commonSet = new LinkedHashSet<>();
        for (var expectedObj : expected) {
            final var expectedJson = new ComparableObject(expectedObj);
            for (var actualObj : actual) {
                final var actualJson = new ComparableObject(actualObj);
                if (expectedJson.equals(actualJson)) {
                    commonSet.add(expectedJson);
                }
            }
        }

        for (var o : commonSet) {
            commonArray.put(o.toJsonObject());
        }

        return commonArray;
    }
}
