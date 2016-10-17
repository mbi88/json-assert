package com.mbi;

/**
 * Use for jsons comparison.
 * <p>
 * Compares json objects or json arrays if they are equal.
 * <p>
 * Acceptable actual object:
 * {@link org.json.JSONObject}
 * {@link org.json.JSONArray}
 * {@link com.jayway.restassured.response.Response}
 * <p>
 * Acceptable expected object:
 * {@link org.json.JSONObject}
 * {@link org.json.JSONArray}
 * {@link org.json.JSONObject}[]
 * <p>
 * By default the comparison is performed on the full objects coincidence and objects shouldn't be sorted in arrays.
 * To set up a different compare mode see available compare mode list: {@link com.mbi.CompareMode}.
 * Example. We have following arrays:
 * actual - [{"id": 2, "name": "string", "structured": true}, {"id": 1, "name": "string", "structured": true}]
 * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
 * To check if arrays objects sorting is equal we should use:
 * JsonAssert assertion = new JsonAssert();
 * assertion
 * .withMode(CompareMode.ORDERED)
 * .jsonEquals(expected, actual);
 * As the result we catch AssertionError here.
 * If sorting equality is not necessary use CompareMode.NOT_ORDERED instead.
 * <p>
 * Sometimes there is no need to compare all fields in objects, some fields can be ignored. Use {@link com.mbi.Assert#ignore(String...)}
 * to ignore fields.
 * Example. We have following arrays:
 * actual - [{"id": 1, "name": "string", "structured": false}, {"id": 2, "name": "string", "structured": true}]
 * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
 * To check if jsons are equal without checking "structured" field we should use:
 * JsonAssert assertion = new JsonAssert();
 * assertion
 * .ignore("structured")
 * .jsonEquals(expected, actual);
 * The assertion is passed.
 * <p>
 * Sometimes we have an array as an actual result but expected result consists of a group of json objects. In this case
 * we are able to use JSONObject[] as an expected result:
 * new JsonAssert().jsonEquals(actualJsonArray, expectedJsonObject1, expectedJsonObject2, expectedJsonObject3);
 * <p>
 * Sometimes expected json array may not contain all the actual json array objects.
 * Use CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY or CompareMode.ORDERED_EXTENSIBLE_ARRAY.
 * <p>
 * Use a {@link com.jayway.restassured.response.Response} as an "actual" argument.
 * <p>
 * For more usages see src/test/java/JsonAssertTest class
 */
interface Assert {

    /**
     * Asserts two objects are equal.
     * <p>
     * Default mode and ignore list:
     * CompareMode mode = CompareMode.NOT_ORDERED;
     * String[] ignore = new String[]{""};
     * <p>
     * Expects that:
     * - actual object is instance of: {@link org.json.JSONObject}, {@link org.json.JSONArray}, {@link com.jayway.restassured.response.Response}
     * - expected object is instance of: {@link org.json.JSONObject}, {@link org.json.JSONArray}, {@link org.json.JSONObject}[]
     * <p>
     * Available compare mode list see {@link com.mbi.CompareMode}
     *
     * @param expected expected object
     * @param actual   actual object
     * @param <T>      {@link org.json.JSONObject}, {@link org.json.JSONArray}, {@link com.jayway.restassured.response.Response}
     * @param <U>      {@link org.json.JSONObject}, {@link org.json.JSONArray}, {@link org.json.JSONObject}[]
     */
    <T, U> void jsonEquals(T actual, U expected);

    /**
     * Available compare mode list see {@link com.mbi.CompareMode}
     *
     * @param mode compare mode {@link com.mbi.CompareMode}
     * @return this
     */
    JsonAssert withMode(CompareMode mode);

    /**
     * Fields to be ignored on comparison
     *
     * @param ignore fields array
     * @return this
     */
    JsonAssert ignore(String... ignore);
}
