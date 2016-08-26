import com.mbi.Assertion;
import com.mbi.CompareMode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class TestClass {

    @Test
    public void testAssertJsonObjectJsonObject() {
        JSONObject j1 = new JSONObject("{\"q\": 1}");
        JSONObject j2 = new JSONObject("{\"q\": 1}");
        JSONObject j3 = new JSONObject("{\"q\": 2}");

        new Assertion()
                .ignore(new String[]{"aaa"})
                .jsonEquals(j1, j2);

        try {
            new Assertion().jsonEquals(j1, j3);
        } catch (AssertionError assertionError) {
            assertTrue(assertionError.getMessage().contains("Was expected"));
        }
    }

    @Test
    public void testAssertJsonArrayJsonArray() {
        JSONArray j1 = new JSONArray("[{\"q\": 1}]");
        JSONArray j2 = new JSONArray("[{\"q\": 1}]");
        JSONArray j3 = new JSONArray("[{\"q\": 2}]");

        new Assertion().jsonEquals(j1, j2);

        try {
            new Assertion().jsonEquals(j1, j3);
        } catch (AssertionError assertionError) {
            assertTrue(assertionError.getMessage().contains("Was expected"));
        }
    }

    @Test
    public void testAssertWithIgnore() {
        JSONObject j1 = new JSONObject("{\"q\": 2, \"w\":3}");
        JSONObject j2 = new JSONObject("{\"q\": 2}");

        new Assertion()
                .ignore(new String[]{"w"})
                .jsonEquals(j1, j2);

        new Assertion()
                .ignore(new String[]{"w"})
                .jsonEquals(j2, j1);

        try {
            new Assertion().jsonEquals(j1, j2);
        } catch (AssertionError assertionError) {
            assertTrue(assertionError.getMessage().contains("Was expected"));
        }
    }

    @Test
    public void testAssertArraysWithMode() {
        JSONArray j1 = new JSONArray("[{\"w\": 3}, {\"q\": 2}]");
        JSONArray j4 = new JSONArray("[{\"q\": 2}, {\"w\": 3}]");
        JSONArray j3 = new JSONArray("[{\"q\": 2}, {\"w\": 3}, {\"e\":4}]");


        new Assertion()
                .withMode(CompareMode.NOT_ORDERED)
                .jsonEquals(j1, j4);

        try {
            new Assertion()
                    .withMode(CompareMode.ORDERED)
                    .jsonEquals(j1, j4);
        } catch (AssertionError assertionError) {
            assertTrue(assertionError.getMessage().contains("Was expected"));
        }

        try {
            new Assertion()
                    .withMode(CompareMode.NOT_ORDERED_EXTEBSIBLE_ARRAY)
                    .jsonEquals(j3, j4);
        } catch (AssertionError assertionError) {
            assertTrue(assertionError.getMessage().contains("Was expected"));
        }

        new Assertion()
                .withMode(CompareMode.NOT_ORDERED_EXTEBSIBLE_ARRAY)
                .jsonEquals(j4, j3);

        try {
            new Assertion()
                    .withMode(CompareMode.ORDERED_EXTENSIBLE_ARRAY)
                    .jsonEquals(j1, j3);
        } catch (AssertionError assertionError) {
            assertTrue(assertionError.getMessage().contains("Was expected"));
        }

        new Assertion()
                .withMode(CompareMode.ORDERED_EXTENSIBLE_ARRAY)
                .jsonEquals(j4, j3);
    }

    @Test
    public void testAssertObjectsWithMode() {
        JSONObject j1 = new JSONObject("{\"q\": 2, \"w\":3}");
        JSONObject j2 = new JSONObject("{\"w\":3, \"q\": 2}");

        new Assertion()
                .withMode(CompareMode.NOT_ORDERED)
                .jsonEquals(j1, j2);

        try {
            new Assertion()
                    .withMode(CompareMode.ORDERED)
                    .jsonEquals(j1, j2);
        } catch (AssertionError assertionError) {
            assertTrue(assertionError.getMessage().contains("Was expected"));
        }
    }
}
