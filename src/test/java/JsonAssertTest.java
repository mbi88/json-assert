import com.mbi.CompareMode;
import com.mbi.JsonAssert;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class TestClass {

    private JsonAssert assertion = new JsonAssert();

    @Test
    public void testObjectDefaultIgnoreIsNotSet() {
        boolean isPassed = false;
        JSONObject expected = new JSONObject("{\"q\": 1, \"w\":2}");
        JSONObject actual = new JSONObject("{\"q\": 1}");

        try {
            assertion.jsonEquals(expected, actual);
            assertion.jsonEquals(actual, expected);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testArrayDefaultIgnoreIsNotSet() {
        boolean isPassed = false;
        JSONArray expected = new JSONArray("[{\"q\": 1}, {\"w\":2}]");
        JSONArray actual = new JSONArray("[{\"q\": 1}]");

        try {
            assertion.jsonEquals(expected, actual);
            assertion.jsonEquals(actual, expected);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testObjectDefaultMode() {
        JSONObject expected = new JSONObject("{\"q\": 1, \"w\":2}");
        JSONObject actual = new JSONObject("{\"w\":2, \"q\": 1}");

        assertion.jsonEquals(expected, actual);
        assertion.jsonEquals(actual, expected);
    }

    @Test
    public void testArrayDefaultMode() {
        JSONArray expected = new JSONArray("[{\"q\": 1}, {\"w\":2}]");
        JSONArray actual = new JSONArray("[{\"w\":2}, {\"q\": 1}]");

        assertion.jsonEquals(expected, actual);
        assertion.jsonEquals(actual, expected);
    }

    @Test
    public void testObjectsNotEquals() {
        boolean isPassed = false;
        JSONObject expected = new JSONObject("{\"q\": 2}");
        JSONObject actual = new JSONObject("{\"q\": 1}");

        try {
            assertion.jsonEquals(expected, actual);
            assertion.jsonEquals(actual, expected);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testArraysNotEquals() {
        boolean isPassed = false;
        JSONArray expected = new JSONArray("[{\"q\": 1}]");
        JSONArray actual = new JSONArray("[{\"q\": 2}]");

        try {
            assertion.jsonEquals(expected, actual);
            assertion.jsonEquals(actual, expected);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testObjectsWithIgnore() {
        JSONObject expected = new JSONObject("{\"q\": 1, \"w\":2}");
        JSONObject actual = new JSONObject("{\"q\": 1}");

        assertion
                .ignore(new String[]{"w"})
                .jsonEquals(expected, actual);
        assertion
                .ignore(new String[]{"w"})
                .jsonEquals(actual, expected);
    }

    @Test
    public void testArraysWithIgnore() {
        JSONArray expected = new JSONArray("[{\"q\": 1, \"w\":2}]");
        JSONArray actual = new JSONArray("[{\"q\": 1}]");

        assertion
                .ignore(new String[]{"w"})
                .jsonEquals(expected, actual);
        assertion
                .ignore(new String[]{"w"})
                .jsonEquals(actual, expected);
    }

    @Test
    public void testObjectNotIgnoresTwice() {
        boolean isPassed = false;
        JSONObject expected = new JSONObject("{\"q\": 1, \"w\":2}");
        JSONObject actual = new JSONObject("{\"q\": 1}");

        assertion
                .ignore(new String[]{"w"})
                .jsonEquals(expected, actual);

        try {
            assertion
                    .jsonEquals(expected, actual);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testArrayNotIgnoresTwice() {
        boolean isPassed = false;
        JSONArray expected = new JSONArray("[{\"q\": 1, \"w\":2}]");
        JSONArray actual = new JSONArray("[{\"q\": 1}]");

        assertion
                .ignore(new String[]{"w"})
                .jsonEquals(expected, actual);

        try {
            assertion
                    .jsonEquals(expected, actual);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testNotOrderedMode() {
        JSONObject expected = new JSONObject("{\"q\": 1, \"w\":2}");
        JSONObject actual = new JSONObject("{\"w\":2, \"q\": 1}");

        assertion
                .withMode(CompareMode.NOT_ORDERED)
                .jsonEquals(expected, actual);

        assertion
                .withMode(CompareMode.NOT_ORDERED)
                .jsonEquals(actual, expected);
    }

    @Test
    public void testOrderedMode() {
        boolean isPassed = false;
        JSONArray expected = new JSONArray("[{\"q\": 1}, {\"w\":2}]");
        JSONArray actual = new JSONArray("[{\"w\":2}, {\"q\": 1}]");

        try {
            assertion
                    .withMode(CompareMode.ORDERED)
                    .jsonEquals(expected, actual);

            assertion
                    .withMode(CompareMode.ORDERED)
                    .jsonEquals(actual, expected);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testNotOrderedExtensibleArray() {
        boolean isPassed = false;
        JSONArray expected = new JSONArray("[{\"w\": 2}, {\"q\": 1}]");
        JSONArray actual = new JSONArray("[{\"q\": 1}, {\"w\":2}, {\"e\": 3}]");

        assertion
                .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                .jsonEquals(expected, actual);

        try {
            assertion
                    .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                    .jsonEquals(actual, expected);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testOrderedExtensibleArray() {
        boolean isPassed = false;
        JSONArray expected = new JSONArray("[{\"q\": 1}, {\"w\": 2}]");
        JSONArray expected2 = new JSONArray("[{\"w\": 2}, {\"q\": 1}]");
        JSONArray actual = new JSONArray("[{\"q\": 1}, {\"w\":2}, {\"e\": 3}]");

        assertion
                .withMode(CompareMode.ORDERED_EXTENSIBLE_ARRAY)
                .jsonEquals(expected, actual);

        try {
            assertion
                    .withMode(CompareMode.ORDERED_EXTENSIBLE_ARRAY)
                    .jsonEquals(expected2, actual);

            assertion
                    .withMode(CompareMode.ORDERED_EXTENSIBLE_ARRAY)
                    .jsonEquals(actual, expected);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testObjects() {
        boolean isPassed = false;
        JSONObject expected = new JSONObject("{\"q\": {\"a\":1, \"s\":2}, \"w\":2}");
        JSONObject actual = new JSONObject("{\"q\": {\"a\":1, \"s\":2}}");
        JSONObject actual2 = new JSONObject("{\"q\": {\"a\":1, \"s\":1}}");

        assertion
                .ignore(new String[]{"w"})
                .withMode(CompareMode.NOT_ORDERED)
                .jsonEquals(expected, actual);

        try {
            assertion
                    .ignore(new String[]{"w"})
                    .withMode(CompareMode.NOT_ORDERED)
                    .jsonEquals(expected, actual2);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testArrays() {
        boolean isPassed = false;
        JSONArray expected = new JSONArray("[{\"q\": {\"a\":1, \"s\":2}, \"w\":2}, {\"e\": 3}]");
        JSONArray actual = new JSONArray("[{\"q\": {\"a\":1, \"s\":2}}, {\"e\": 3}]");
        JSONArray actual2 = new JSONArray("[{\"q\": {\"a\":1, \"s\":2}}, {\"e\": 3}]");

        assertion
                .ignore(new String[]{"w"})
                .withMode(CompareMode.NOT_ORDERED)
                .jsonEquals(expected, actual);

        try {
            assertion
                    .ignore(new String[]{"w"})
                    .withMode(CompareMode.NOT_ORDERED)
                    .jsonEquals(expected, actual2);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }
}
