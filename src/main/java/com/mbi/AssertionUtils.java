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
 * Utility class for advanced JSON comparison and transformation logic.
 * <p>
 * It helps apply field-level filters (include/ignore), normalize JSON for comparison,
 * and extract shared elements between JSON arrays.
 */
@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
final class AssertionUtils {

    /**
     * Fields separator in flattened json.
     */
    private static final String FIELD_SEPARATOR = ".";

    /**
     * Predicate that checks if a given flattened JSON key belongs to any of the provided parent paths.
     * Used for applying include/exclude rules based on JSON field hierarchy.
     */
    private static final BiPredicate<String, Set<String>> IS_CHILD_FIELD = (flattenedJsonKey, parents) ->
            parents.stream().anyMatch(parent ->
                    flattenedJsonKey.startsWith(parent + FIELD_SEPARATOR)
                            || flattenedJsonKey.equalsIgnoreCase(parent)
                            || isArrayFieldMatch(parent, flattenedJsonKey));

    /**
     * Split field by field separator.
     */
    private static final Function<String, String[]> SPLIT_KEYS = s -> s.split("\\.");

    /**
     * Given a parent key, returns all children fields from the given key set that belong to that parent.
     */
    private static final BiFunction<Set<String>, String, List<String>> CHILD_KEYS = (parentFields, parent) -> {
        final var list = new ArrayList<String>();
        for (final var key : parentFields) {
            final var regex = parent.replace("[", "\\[").replace("]", "\\]") + "\\..*";
            if (key.equalsIgnoreCase(parent) || key.matches(regex)) {
                list.add(key);
            }
        }
        return list;
    };

    /**
     * Returns the minimum number of dot-separated segments across all children field names.
     * Used to determine the shallowest level of nesting among a group of fields.
     * ["as.ew.er", "ds.sd', "asd.a"] -> 1
     * ["as.ew.er", "ds.sd', "asd"] -> 0
     */
    private static final Function<List<String>, Integer> MIN_DOT_DEPTH = children ->
            children.stream()
                    .mapToInt(child -> StringUtils.countMatches(child, FIELD_SEPARATOR))
                    .min()
                    .orElse(0);

    /**
     * Reduces a list of deeply nested fields into their top-level path up to the shallowest shared depth.
     * Example: ["a.b.c", "a.b.d"] → ["a.b"]
     */
    private static final Function<List<String>, Set<String>> TOP_LEVEL_FIELDS = children -> {
        final var result = new HashSet<String>();
        final int depth = MIN_DOT_DEPTH.apply(children);

        for (final var key : children) {
            final var parts = SPLIT_KEYS.apply(key);
            result.add(String.join(FIELD_SEPARATOR, Arrays.copyOf(parts, depth + 1)));
        }

        return result;
    };

    /**
     * Reduces a full set of field names to their minimal distinguishable top-level segments.
     * Helps simplify field path matching.
     */
    private static final Function<Set<String>, Set<String>> REDUCE_TO_TOP_LEVEL = keys -> {
        final var result = new HashSet<String>();
        for (final var key : keys) {
            final var top = SPLIT_KEYS.apply(key)[0];
            result.addAll(TOP_LEVEL_FIELDS.apply(CHILD_KEYS.apply(keys, top)));
        }
        return result;
    };

    /**
     * Prevent instantiation.
     */
    private AssertionUtils() {
    }

    /**
     * Filters a JSONObject based on a blackList (ignore fields) and whiteList (include only).
     * The object is flattened first, filtered, and then rebuilt into nested form.
     *
     * @param input     Original JSON object.
     * @param blackList Fields to exclude (ignored).
     * @param whiteList Fields to include (whiteList).
     * @return Filtered JSON object.
     */
    public static JSONObject filterFields(final JSONObject input, final Set<String> blackList,
                                          final Set<String> whiteList) {
        final var original = new JSONObject(input.toString());

        // Flattened json
        final var flattened = new JsonFlattener(original.toString())
                .withFlattenMode(FlattenMode.NORMAL)
                .flatten();

        final var flat = new JSONObject(flattened);

        final Set<String> keys = flat.keySet();
        final Set<String> whitelistTop = REDUCE_TO_TOP_LEVEL.apply(whiteList);
        final Set<String> blacklistTop = REDUCE_TO_TOP_LEVEL.apply(blackList);

        for (final var key : new HashSet<>(keys)) {
            if (!whiteList.isEmpty() && !IS_CHILD_FIELD.test(key, whitelistTop)) {
                flat.remove(key); // remove fields not listed in whiteList
            }

            if (!blackList.isEmpty() && IS_CHILD_FIELD.test(key, blacklistTop)) {
                flat.remove(key); // remove blacklisted fields
            }
        }

        final var result = new JSONObject(JsonUnflattener.unflatten(flat.toString()));
        // Check result != {} to avoid removing all fields from json
        Validate.isTrue(!result.similar(new JSONObject()),
                "You removed all fields from json!\nOriginal:\n" + input.toString(2));

        return result;
    }

    /**
     * Filters a JSONArray of JSONObjects using the same include/exclude logic as `filterFields(JSONObject)`.
     * If any item is not a JSONObject, the original array is returned.
     *
     * @param input     Original array.
     * @param blacklist Fields to exclude.
     * @param whitelist Fields to include.
     * @return Filtered array.
     */
    public static JSONArray filterFields(final JSONArray input, final Set<String> blacklist,
                                         final Set<String> whitelist) {
        final var result = new JSONArray();

        for (int i = 0; i < input.length(); i++) {
            // Json array may consist of not json objects (e.g.: [1, 2, 5]).
            // In this case return original json array
            try {
                final var obj = new JSONObject(input.get(i).toString());
                result.put(filterFields(obj, blacklist, whitelist));
            } catch (JSONException ignored) {
                return input; // skip filtering if any item is not a JSONObject
            }
        }

        return result;
    }

    /**
     * Converts one or more JSONObject instances into a JSONArray.
     *
     * @param objects json objects.
     * @return json array.
     */
    public static JSONArray objectsToArray(final JSONObject... objects) {
        final var array = new JSONArray();
        for (final var obj : objects) {
            array.put(obj);
        }
        return array;
    }

    /**
     * Builds a formatted error message for object comparison failure.
     * Includes expected and actual JSON formatted with indentation.
     *
     * @param error    assertion error.
     * @param expected expected json object.
     * @param actual   actual json object.
     * @return formatted error message.
     */
    public static String getErrorMessage(final AssertionError error, final JSONObject expected,
                                         final JSONObject actual) {
        return formatError(error, expected.toString(4), actual.toString(4));
    }

    /**
     * Builds a formatted error message for array comparison failure.
     *
     * @param error    assertion error.
     * @param expected expected json array.
     * @param actual   actual json array.
     * @return formatted error message.
     */
    public static String getErrorMessage(final AssertionError error, final JSONArray expected, final JSONArray actual) {
        return formatError(error, expected.toString(4), actual.toString(4));
    }

    /**
     * Formats error message.
     *
     * @param error    assertion error.
     * @param expected expected json as string with indentations.
     * @param actual   actual json as string with indentations.
     * @return error message.
     */
    private static String formatError(final AssertionError error, final String expected, final String actual) {
        return String.format("%s%n%nExpected: %s%n%nBut found: %s", error.getMessage(), expected, actual);
    }

    /**
     * Returns a JSONArray of objects that are present in both expected and actual arrays
     * using strict (non-extensible) JSON equality.
     *
     * @param expected expected json array.
     * @param actual   actual json array.
     * @return json array with common objects.
     */
    public static JSONArray getCommonArray(final JSONArray expected, final JSONArray actual) {
        final Set<ComparableObject> common = new LinkedHashSet<>();

        // Get a set of expected objects that are common for actual
        for (final var exp : expected) {
            final var expectedObj = new ComparableObject(exp);
            for (final var act : actual) {
                if (expectedObj.equals(new ComparableObject(act))) {
                    common.add(expectedObj);
                }
            }
        }

        final var result = new JSONArray();
        for (final var obj : common) {
            result.put(obj.toJsonObject());
        }

        return result;
    }

    /**
     * Determines whether a flattened array reference should match the key.
     * Example:
     * parentField = "array", key = "array[0].id" => true
     */
    private static boolean isArrayFieldMatch(final String parent, final String key) {
        final boolean parentIsNotIndex = !parent.matches("^\\[\\d+].*");
        final boolean keyStartsWithParent = key.startsWith(parent);

        if (!keyStartsWithParent || !parentIsNotIndex) {
            return false;
        }

        final var suffix = key.substring(parent.length());
        return suffix.matches("^\\[\\d+].*");
    }
}
