package com.mbi;

import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Use for json comparison.
 * <p>
 * Compares json objects or json arrays if they are equal.
 * <p>
 * Acceptable actual object:
 * {@link org.json.JSONObject}
 * {@link org.json.JSONArray}
 * {@link io.restassured.response.Response}
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
 * Sometimes there is no need to compare all fields in objects, some fields can be ignored. Use
 * {@link com.mbi.JsonAssert#ignore(String...)} to ignore fields.
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
 * Use a {@link io.restassured.response.Response} as an "actual" argument.
 * <p>
 * For more usages see tests.
 */
public interface Assert {

    /**
     * Asserts two objects are equal.
     * <p>
     * Default mode and ignore list:
     * CompareMode mode = CompareMode.NOT_ORDERED;
     * String[] blackList = new String[]{""};
     * String[] whiteList = new String[]{""};
     * </p>
     *
     * @param actual   actual object {@link org.json.JSONObject}
     * @param expected expected object {@link org.json.JSONObject}
     * @throws AssertionError if assertion failed.
     */
    void jsonEquals(JSONObject actual, JSONObject expected);

    /**
     * Asserts two objects are equal.
     * <p>
     * Default mode and ignore list:
     * CompareMode mode = CompareMode.NOT_ORDERED;
     * String[] blackList = new String[]{""};
     * String[] whiteList = new String[]{""};
     * </p>
     *
     * @param actual   actual object {@link org.json.JSONArray}
     * @param expected expected object {@link org.json.JSONArray}
     * @throws AssertionError if assertion failed.
     */
    void jsonEquals(JSONArray actual, JSONArray expected);

    /**
     * Asserts two objects are equal.
     * <p>
     * Default mode and ignore list:
     * CompareMode mode = CompareMode.NOT_ORDERED;
     * String[] blackList = new String[]{""};
     * String[] whiteList = new String[]{""};
     * </p>
     *
     * @param actual   actual object {@link org.json.JSONArray}
     * @param expected expected object {@link org.json.JSONObject}
     * @throws AssertionError if assertion failed.
     */
    void jsonEquals(JSONArray actual, JSONObject... expected);

    /**
     * Asserts two objects are equal.
     * <p>
     * Default mode and ignore list:
     * CompareMode mode = CompareMode.NOT_ORDERED;
     * String[] blackList = new String[]{""};
     * String[] whiteList = new String[]{""};
     * </p>
     *
     * @param actual   actual object {@link io.restassured.response.Response}
     * @param expected expected object {@link org.json.JSONArray}
     * @throws AssertionError if assertion failed.
     */
    void jsonEquals(Response actual, JSONArray expected);

    /**
     * Asserts two objects are equal.
     * <p>
     * Default mode and ignore list:
     * CompareMode mode = CompareMode.NOT_ORDERED;
     * String[] blackList = new String[]{""};
     * String[] whiteList = new String[]{""};
     * </p>
     *
     * @param actual   actual object {@link io.restassured.response.Response}
     * @param expected expected object {@link org.json.JSONObject}
     * @throws AssertionError if assertion failed.
     */
    void jsonEquals(Response actual, JSONObject expected);

    /**
     * Asserts two objects are equal.
     * <p>
     * Default mode and ignore list:
     * CompareMode mode = CompareMode.NOT_ORDERED;
     * String[] blackList = new String[]{""};
     * String[] whiteList = new String[]{""};
     * </p>
     *
     * @param actual   actual object {@link io.restassured.response.Response}
     * @param expected expected object {@link org.json.JSONObject}
     * @throws AssertionError if assertion failed.
     */
    void jsonEquals(Response actual, JSONObject... expected);

    /**
     * Asserts two objects are equal.
     * <p>
     * Default mode and ignore list:
     * CompareMode mode = CompareMode.NOT_ORDERED;
     * String[] blackList = new String[]{""};
     * String[] whiteList = new String[]{""};
     * </p>
     *
     * @param actual   actual object {@link org.json.JSONObject}
     * @param expected expected object {@link org.json.JSONObject}
     * @throws AssertionError if assertion failed.
     */
    void jsonNotEquals(JSONObject actual, JSONObject expected);

    /**
     * Asserts two objects are not equal.
     * <p>
     * Default mode and ignore list:
     * CompareMode mode = CompareMode.NOT_ORDERED;
     * String[] blackList = new String[]{""};
     * String[] whiteList = new String[]{""};
     * </p>
     *
     * @param actual   actual object {@link org.json.JSONArray}
     * @param expected expected object {@link org.json.JSONArray}
     * @throws AssertionError if assertion failed.
     */
    void jsonNotEquals(JSONArray actual, JSONArray expected);

    /**
     * Mode objects will be compared with.
     * Available compare mode list see {@link com.mbi.CompareMode}
     *
     * @param mode compare mode {@link com.mbi.CompareMode}
     * @return JsonAssert.class
     */
    Assert withMode(CompareMode mode);

    /**
     * Fields to be ignored on comparison.
     *
     * @param ignoreFieldNames fields array
     * @return JsonAssert.class
     */
    Assert ignore(String... ignoreFieldNames);

    /**
     * Set fields that will be compared.
     *
     * @param compareFieldNames fields to be checked.
     * @return JsonAssert.class.
     */
    Assert compareOnly(String... compareFieldNames);
}
