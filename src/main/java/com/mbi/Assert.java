package com.mbi;

import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Assertion interface for verifying JSON equality or inequality.
 * <p>
 * Supports flexible comparison of JSON objects and arrays,
 * with support for:
 * - Comparison modes (ordered, extensible, etc.)
 * - Ignoring specific fields
 * - Comparing only specific fields
 */
public interface Assert {

    /**
     * Asserts two objects are equal.
     * <p>
     * Default mode and ignore list:
     * CompareMode mode = CompareMode.NOT_ORDERED;
     * String[] blackList = new String[]{""};
     * String[] whiteList = new String[]{""};
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
