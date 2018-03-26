import com.mbi.CompareMode;
import com.mbi.JsonAssert;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class JsonAssertTest {

    private JsonAssert assertion = new JsonAssert();

    @Test
    public void testObjectDefaultIgnoreIsNotSet() {
        boolean isPassed = false;
        JSONObject expected = new JSONObject("{\"q\": 1, \"w\":2}");
        JSONObject actual = new JSONObject("{\"q\": 1}");

        try {
            assertion.jsonEquals(expected, actual);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        try {
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
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        try {
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
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        try {
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
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        try {
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
                .ignore("w")
                .jsonEquals(expected, actual);
        assertion
                .ignore("w")
                .jsonEquals(actual, expected);
    }

    @Test
    public void testArraysWithIgnore() {
        JSONArray expected = new JSONArray("[{\"q\": 1, \"w\":2}]");
        JSONArray actual = new JSONArray("[{\"q\": 1}]");

        assertion
                .ignore("w")
                .jsonEquals(expected, actual);
        assertion
                .ignore("w")
                .jsonEquals(actual, expected);
    }

    @Test
    public void testObjectNotIgnoresTwice() {
        boolean isPassed = false;
        JSONObject expected = new JSONObject("{\"q\": 1, \"w\":2}");
        JSONObject actual = new JSONObject("{\"q\": 1}");

        assertion
                .ignore("w")
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
                .ignore("w")
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
        JSONArray actual2 = new JSONArray("[{\"q\": 1}, {\"w\":2}, {\"e\": 3}]");

        try {
            assertion
                    .withMode(CompareMode.ORDERED)
                    .jsonEquals(expected, actual);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        try {
            assertion
                    .withMode(CompareMode.ORDERED)
                    .jsonEquals(actual, expected);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        try {
            assertion
                    .withMode(CompareMode.ORDERED)
                    .jsonEquals(expected, actual2);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testNotOrderedExtensibleArray() {
        boolean isPassed = false;
        JSONArray expected = new JSONArray(
                "[{\"id\": 2, \"name\": \"string\", \"structured\": true}, {\"id\": 1, \"name\": \"string\", \"structured\": true}]");
        JSONArray actual = new JSONArray(
                "[{\"id\": 1, \"name\": \"string\", \"structured\": true}, {\"id\": 2, \"name\": \"string\", \"structured\": true}, {\"id\": 3, \"name\": \"string\", \"structured\": false}]");

        assertion
                .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                .jsonEquals(actual, expected);

        try {
            assertion
                    .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                    .jsonEquals(expected, actual);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testNoCommonObjects() {
        boolean passed = false;
        JSONArray expected = new JSONArray(
                "[{\"id\": 3, \"name\": \"string\", \"structured\": true}, {\"id\": 2, \"name\": \"string\", \"structured\": true}]");
        JSONArray actual = new JSONArray(
                "[{\"id\": 1, \"name\": \"string\", \"structured\": true}, {\"id\": 2, \"name\": \"string\", \"structured\": true}, {\"id\": 3, \"name\": \"string\", \"structured\": false}]");

        try {
            assertion
                    .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                    .jsonEquals(actual, expected);
            passed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(passed);
    }

    @Test
    public void testOrderedExtensibleArray() {
        boolean isPassed = false;
        JSONArray expected = new JSONArray("[{\"q\": 1}, {\"w\": 2}]");
        JSONArray expected2 = new JSONArray("[{\"w\": 2}, {\"q\": 1}]");
        JSONArray actual = new JSONArray("[{\"q\": 1}, {\"w\":2}, {\"e\": 3}]");

        assertion
                .withMode(CompareMode.ORDERED_EXTENSIBLE_ARRAY)
                .jsonEquals(actual, expected);

        try {
            assertion
                    .withMode(CompareMode.ORDERED_EXTENSIBLE_ARRAY)
                    .jsonEquals(expected2, actual);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        try {
            assertion
                    .withMode(CompareMode.ORDERED_EXTENSIBLE_ARRAY)
                    .jsonEquals(expected, actual);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testObjectsAssertion() {
        boolean isPassed = false;
        JSONObject expected = new JSONObject("{\"q\": {\"a\":1, \"s\":2}, \"w\":2}");
        JSONObject actual = new JSONObject("{\"q\": {\"a\":1, \"s\":2}}");
        JSONObject actual2 = new JSONObject("{\"q\": {\"a\":1, \"s\":1}}");

        assertion
                .ignore("w")
                .withMode(CompareMode.NOT_ORDERED)
                .jsonEquals(expected, actual);

        try {
            assertion
                    .ignore("w")
                    .withMode(CompareMode.NOT_ORDERED)
                    .jsonEquals(expected, actual2);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testArraysAssertion() {
        boolean isPassed = false;
        JSONArray expected = new JSONArray("[{\"q\": {\"a\":1, \"s\":2}, \"w\":2}, {\"e\": 3}]");
        JSONArray actual = new JSONArray("[{\"q\": {\"a\":1, \"s\":2}}, {\"e\": 3}]");
        JSONArray actual2 = new JSONArray("[{\"q\": {\"a\":1, \"s\":1}}, {\"e\": 3}]");

        assertion
                .ignore("w")
                .withMode(CompareMode.NOT_ORDERED)
                .jsonEquals(expected, actual);

        try {
            assertion
                    .ignore("w")
                    .withMode(CompareMode.NOT_ORDERED)
                    .jsonEquals(expected, actual2);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }

        assertFalse(isPassed);
    }

    @Test
    public void testArraysNotOfJsonObjects() {
        JSONArray a1 = new JSONArray("[18,17,16,15,14]");
        JSONArray a2 = new JSONArray("[18,17,16,15,14]");

        assertion.jsonEquals(a1, a2);
    }


    @Test
    public void testNotEqualArraysNotOfJsonObjects() {
        boolean isPassed = false;
        JSONArray a1 = new JSONArray("[18,17,16,15,14]");
        JSONArray a2 = new JSONArray("[18,17,16,15,1]");

        try {
            assertion.jsonEquals(a1, a2);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }
        assertFalse(isPassed);
    }

    @Test
    public void testJsonPathInIgnoreForJsonObjects() {
        JSONObject j1 = new JSONObject().put("a", new JSONObject().put("b", 1)).put("d", 5);
        JSONObject j2 = new JSONObject().put("a", new JSONObject().put("b", 1).put("c", 3));

        assertion
                .ignore("a.c", "d")
                .jsonEquals(j1, j2);
    }

    @Test
    public void testJsonPathInIgnoreForJsonArrays() {
        JSONArray j1 = new JSONArray()
                .put(new JSONObject().put("a", new JSONObject().put("b", 1)).put("d", 5))
                .put(new JSONObject().put("a", new JSONObject().put("b", 1)).put("d", 5));
        JSONArray j2 = new JSONArray()
                .put(new JSONObject().put("a", new JSONObject().put("b", 1).put("c", 3)))
                .put(new JSONObject().put("a", new JSONObject().put("b", 1)).put("d", 5));

        assertion
                .ignore("a.c", "d")
                .jsonEquals(j1, j2);
    }

    @Test
    public void testFailIfJsonPathPointsToNonExistentField() {
        boolean isPassed = false;
        JSONObject j1 = new JSONObject().put("a", new JSONObject().put("b", 1)).put("d", 5);
        JSONObject j2 = new JSONObject().put("a", new JSONObject().put("b", 1).put("c", 3));

        try {
            assertion
                    .ignore("a.e", "d")
                    .jsonEquals(j1, j2);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }
        assertFalse(isPassed);
    }

    @Test
    public void testNullAsObjectOfArray() {
        boolean isPassed = false;
        JSONArray j = new JSONArray("[{\"a\": 1, \"b\": 1}, {\"a\": 2, \"b\": 1}]");
        JSONArray j2 = new JSONArray().put(new JSONObject().put("b", JSONObject.NULL));
        try {
            assertion
                    .ignore("a")
                    .withMode(CompareMode.ORDERED_EXTENSIBLE_ARRAY)
                    .jsonEquals(j, j2);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }
        assertFalse(isPassed);
    }

    @Test
    public void testCompareOnly1() {
        JSONObject json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        JSONObject json2 = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 3).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1)))
                .put("d", 6);

        assertion
                .ignore("c.cc.ccc2")
                .compareOnly("c.cc")
                .jsonEquals(json, json2);
    }

    @Test
    public void testCompareOnly2() {
        JSONObject json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        JSONObject json2 = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 3).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1)))
                .put("d", 6);

        boolean isPassed = false;
        try {
            assertion
                    .compareOnly("c.cc")
                    .jsonEquals(json, json2);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }
        assertFalse(isPassed);
    }

    @Test
    public void testCompareOnly3() {
        JSONObject json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        JSONObject json2 = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 3).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1)))
                .put("d", 6);

        assertion
                .compareOnly("a")
                .jsonEquals(json, json2);
    }

    @Test
    public void testCompareOnly4() {
        JSONObject json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        JSONObject json2 = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 3).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1)))
                .put("d", 6);

        boolean isPassed = false;
        try {
            assertion
                    .compareOnly("b")
                    .jsonEquals(json, json2);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }
        assertFalse(isPassed);
    }

    @Test
    public void testIgnore1() {
        JSONObject json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        JSONObject json2 = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 3).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1)))
                .put("d", 6);

        assertion
                .ignore("d", "c.cc.ccc2", "b")
                .jsonEquals(json, json2);
    }

    @Test
    public void testIgnore2() {
        boolean isPassed = false;
        JSONObject json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        JSONObject json2 = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 3).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1)))
                .put("d", 6);

        try {
            assertion
                    .ignore("c.cc.ccc2", "b")
                    .jsonEquals(json, json2);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }
        assertFalse(isPassed);
    }

    @Test
    public void testIgnore3() {
        boolean isPassed = false;
        JSONObject json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        JSONObject json2 = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 3).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1)))
                .put("d", 6);

        try {
            assertion
                    .ignore("d", "b")
                    .jsonEquals(json, json2);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }
        assertFalse(isPassed);
    }

    @Test
    public void testIgnore4() {
        boolean isPassed = false;
        JSONObject json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        JSONObject json2 = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 3).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1)))
                .put("d", 6);

        try {
            assertion
                    .ignore("d", "c.cc.ccc2")
                    .jsonEquals(json, json2);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }
        assertFalse(isPassed);
    }

    @Test
    public void testRemoveAllFields() {
        boolean isPassed = false;
        JSONObject json = new JSONObject().put("a", 1);
        JSONObject json2 = new JSONObject().put("a", 1);

        try {
            assertion
                    .ignore("a")
                    .jsonEquals(json, json2);
            isPassed = true;
        } catch (IllegalArgumentException ie) {
            assertTrue(ie.getMessage().contains("You removed all fields from json"));
        }
        assertFalse(isPassed);
    }
}
