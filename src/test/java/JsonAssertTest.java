import com.mbi.CompareMode;
import com.mbi.JsonAssert;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class JsonAssertTest {

    private final JsonAssert assertion = new JsonAssert();

    @Test
    public void testObjectDefaultIgnoreIsNotSet() {
        boolean isPassed = false;
        var expected = new JSONObject("{\"q\": 1, \"w\":2}");
        var actual = new JSONObject("{\"q\": 1}");

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
        var expected = new JSONArray("""
                [{"q": 1}, {"w":2}]""");
        var actual = new JSONArray("""
                [{"q": 1}]""");

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
        var expected = new JSONObject("""
                {"q": 1, "w":2}""");
        var actual = new JSONObject("""
                {"w":2, "q": 1}""");

        assertion.jsonEquals(expected, actual);
        assertion.jsonEquals(actual, expected);
    }

    @Test
    public void testArrayDefaultMode() {
        var expected = new JSONArray("""
                [{"q": 1}, {"w":2}]""");
        var actual = new JSONArray("""
                [{"w":2}, {"q": 1}]""");

        assertion.jsonEquals(expected, actual);
        assertion.jsonEquals(actual, expected);
    }

    @Test
    public void testObjectsNotEquals() {
        boolean isPassed = false;
        var expected = new JSONObject("""
                {"q": 2}""");
        var actual = new JSONObject("""
                {"q": 1}""");

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
        var expected = new JSONArray("""
                [{"q": 1}]""");
        var actual = new JSONArray("""
                [{"q": 2}]""");

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
        var expected = new JSONObject("""
                {"q": 1, "w":2}""");
        var actual = new JSONObject("""
                {"q": 1}""");

        assertion
                .ignore("w")
                .jsonEquals(expected, actual);
        assertion
                .ignore("w")
                .jsonEquals(actual, expected);
    }

    @Test
    public void testArraysWithIgnore() {
        var expected = new JSONArray("""
                [{"q": 1, "w":2}]""");
        var actual = new JSONArray("""
                [{"q": 1}]""");

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
        var expected = new JSONObject("""
                {"q": 1, "w":2}""");
        var actual = new JSONObject("""
                {"q": 1}""");

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
        var expected = new JSONArray("""
                [{"q": 1, "w":2}]""");
        var actual = new JSONArray("""
                [{"q": 1}]""");

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
        var expected = new JSONObject("""
                {"q": 1, "w":2}""");
        var actual = new JSONObject("""
                {"w":2, "q": 1}""");

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
        var expected = new JSONArray("""
                [{"q": 1}, {"w":2}]""");
        var actual = new JSONArray("""
                [{"w":2}, {"q": 1}]""");
        var actual2 = new JSONArray("""
                [{"q": 1}, {"w":2}, {"e": 3}]""");

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
        var expected = new JSONArray("""
                [{"id": 2, "name": "string", "structured": true}, {"id": 1, "name": "string", "structured": true}]""");
        var actual = new JSONArray("""
                [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}, {"id": 3, "name": "string", "structured": false}]""");

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
        var expected = new JSONArray("""
                [{"id": 3, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]""");
        var actual = new JSONArray("""
                [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}, {"id": 3, "name": "string", "structured": false}]""");

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
        var expected = new JSONArray("""
                [{"q": 1}, {"w": 2}]""");
        var expected2 = new JSONArray("""
                [{"w": 2}, {"q": 1}]""");
        var actual = new JSONArray("""
                [{"q": 1}, {"w":2}, {"e": 3}]""");

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
        var expected = new JSONObject("""
                {"q": {"a":1, "s":2}, "w":2}""");
        var actual = new JSONObject("""
                {"q": {"a":1, "s":2}}""");
        var actual2 = new JSONObject("""
                {"q": {"a":1, "s":1}}""");

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
        var expected = new JSONArray("""
                [{"q": {"a":1, "s":2}, "w":2}, {"e": 3}]""");
        var actual = new JSONArray("""
                [{"q": {"a":1, "s":2}}, {"e": 3}]""");
        var actual2 = new JSONArray("""
                [{"q": {"a":1, "s":1}}, {"e": 3}]""");

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
        var a1 = new JSONArray("[18,17,16,15,14]");
        var a2 = new JSONArray("[18,17,16,15,14]");

        assertion.jsonEquals(a1, a2);
    }

    @Test
    public void testNotEqualArraysNotOfJsonObjects() {
        boolean isPassed = false;
        var a1 = new JSONArray("[18,17,16,15,14]");
        var a2 = new JSONArray("[18,17,16,15,1]");

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
        var j1 = new JSONObject().put("a", new JSONObject().put("b", 1)).put("d", 5);
        var j2 = new JSONObject().put("a", new JSONObject().put("b", 1).put("c", 3));

        assertion
                .ignore("a.c", "d")
                .jsonEquals(j1, j2);
    }

    @Test
    public void testJsonPathInIgnoreForJsonArrays() {
        var j1 = new JSONArray()
                .put(new JSONObject().put("a", new JSONObject().put("b", 1)).put("d", 5))
                .put(new JSONObject().put("a", new JSONObject().put("b", 1)).put("d", 5));
        var j2 = new JSONArray()
                .put(new JSONObject().put("a", new JSONObject().put("b", 1).put("c", 3)))
                .put(new JSONObject().put("a", new JSONObject().put("b", 1)).put("d", 5));

        assertion
                .ignore("a.c", "d")
                .jsonEquals(j1, j2);
    }

    @Test
    public void testFailIfJsonPathPointsToNonExistentField() {
        var isPassed = false;
        var j1 = new JSONObject().put("a", new JSONObject().put("b", 1)).put("d", 5);
        var j2 = new JSONObject().put("a", new JSONObject().put("b", 1).put("c", 3));

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
        var j = new JSONArray("""
                [{"a": 1, "b": 1}, {"a": 2, "b": 1}]""");
        var j2 = new JSONArray().put(new JSONObject().put("b", JSONObject.NULL));
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
        var json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        var json2 = new JSONObject()
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
        var json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        var json2 = new JSONObject()
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
        var json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        var json2 = new JSONObject()
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
        var json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        var json2 = new JSONObject()
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
    public void testCompareOnly5() {
        var js = new JSONObject("""
                {"data": {"id": "1","email": "m@s.com","name": "m"}}""");
        var jsonObject = new JSONObject().put("data",
                new JSONObject().put("email", "m@s.com").put("id", "1"));

        assertion
                .compareOnly("data.id", "data.email")
                .jsonEquals(js, jsonObject);

        boolean isPassed = false;
        try {
            assertion
                    .compareOnly("data.name", "data.email")
                    .jsonEquals(js, jsonObject);
            isPassed = true;
        } catch (AssertionError ae) {
            assertTrue(ae.getMessage().contains("But found"));
        }
        assertFalse(isPassed);
    }

    @Test
    public void testIgnore1() {
        var json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        var json2 = new JSONObject()
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
        var json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        var json2 = new JSONObject()
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
        var json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        var json2 = new JSONObject()
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
        var json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        var json2 = new JSONObject()
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
    public void testIgnore5() {
        var js = new JSONObject("""
                {"data": {"id": "1","email": "m@s.com","name": "m"}}""");
        var jsonObject = new JSONObject().put("data",
                new JSONObject().put("email", "m@s.com"));

        assertion
                .compareOnly("data.email", "data")
                .ignore("data.id", "data.name")
                .jsonEquals(js, jsonObject);
    }

    @Test
    public void testRemoveAllFields() {
        boolean isPassed = false;
        var json = new JSONObject().put("a", 1);
        var json2 = new JSONObject().put("a", 1);

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

    @Test
    public void testFailIfMissedFieldInInnerObject() {
        boolean isPassed = false;
        var actual = new JSONArray("""
                [
                   {
                     "id": 68056,
                     "accountId": 40194,
                     "sharedBy": {
                       "providerId": 40196,
                       "license": "Standard",
                       "status": "Accepted"
                     }
                   }
                 ]""");

        var expected = new JSONObject("""
                {
                   "audienceId": 4019,
                   "accountId": 40194,
                   "sharedBy": {
                     "providerId": 40196,
                     "status": "Accepted"
                   }
                 }""");

        try {
            assertion
                    .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                    .compareOnly("sharedBy", "accountId")
                    .jsonEquals(actual, new JSONArray().put(expected));
            isPassed = true;
        } catch (AssertionError e) {
            assertTrue(e.getMessage().contains("Expected 1 values but got 0"));
        }
        assertFalse(isPassed);
    }

    @Test
    public void testSuccessIfIgnoreMissedFieldInInnerObject() {
        var actual = new JSONArray("""
                [
                  {
                    "id": 68056,
                    "accountId": 40194,
                    "sharedBy": {
                      "providerId": 40196,
                      "license": "Standard",
                      "status": "Accepted"
                    }
                  }
                ]""");

        var expected = new JSONObject("""
                {
                   "audienceId": 4019,
                   "accountId": 40194,
                   "sharedBy": {
                     "providerId": 40196,
                     "status": "Accepted"
                   }
                 }""");

        assertion
                .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                .compareOnly("sharedBy", "accountId")
                .ignore("sharedBy.license")
                .jsonEquals(actual, new JSONArray().put(expected));
    }

    @Test
    public void testJsonObjectsNotEqualsSuccess() {
        assertion.jsonNotEquals(new JSONObject().put("a", 1), new JSONObject().put("a", 2));
    }

    @Test
    public void testJsonObjectsNotEqualsFailed() {
        boolean passed;
        try {
            assertion.jsonNotEquals(new JSONObject().put("a", 1), new JSONObject().put("a", 1));
            passed = true;
        } catch (AssertionError e) {
            passed = false;
            assertTrue(e.getMessage().contains("Objects are equal!"));
        }
        assertFalse(passed);
    }

    @Test
    public void testJsonArraysNotEqualsSuccess() {
        assertion.jsonNotEquals(new JSONArray().put(new JSONObject().put("a", 1)), new JSONArray().put(new JSONObject().put("a", 2)));
    }

    @Test
    public void testJsonArraysNotEqualsFailedWithExtensibleArray() {
        var jsonArray1 = new JSONArray().put(new JSONObject().put("a", 1));
        var jsonArray2 = new JSONArray().put(new JSONObject().put("a", 2)).put(new JSONObject().put("a", 1));

        boolean passed;
        try {
            assertion
                    .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                    .jsonNotEquals(jsonArray2, jsonArray1);
            passed = true;
        } catch (AssertionError e) {
            passed = false;
            assertTrue(e.getMessage().contains("Objects are equal!"));
        }
        assertFalse(passed);
    }

    @Test
    public void testJsonArraysNotEqualsFailed() {
        boolean passed;
        try {
            assertion.jsonNotEquals(new JSONArray().put(new JSONObject().put("a", 1)), new JSONArray().put(new JSONObject().put("a", 1)));
            passed = true;
        } catch (AssertionError e) {
            passed = false;
            assertTrue(e.getMessage().contains("Objects are equal!"));
        }
        assertFalse(passed);
    }

    @Test
    public void testEqualityNotDependsOnInnerArraySortOrderOnExtensibleArrayMode() {
        var jj1 = new JSONArray();
        jj1.put("daec7f0f-f077-415d-82d2-bd55d4e9c4cb").put("70fd7d50-c784-489c-8152-1ec5c932a08b");
        var jj2 = new JSONArray();
        jj2.put("70fd7d50-c784-489c-8152-1ec5c932a08b").put("daec7f0f-f077-415d-82d2-bd55d4e9c4cb");

        var jsonObject1 = new JSONObject();
        jsonObject1.put("users", jj1);
        var jsonObject2 = new JSONObject();
        jsonObject2.put("users", jj2);

        assertion
                .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                .jsonEquals(new JSONArray().put(jsonObject2), new JSONArray().put(jsonObject1));
    }

    @Test
    public void testCanIgnoreFieldsInObjectFromArray() {
        var json1 = new JSONObject("""
                {"data": [{"a":1, "b":2}, {"a":1, "b":2}], "c": 1}""");
        var json2 = new JSONObject("""
                {"data": [{"a":1, "b":1}, {"a":1, "b":2}], "c": 2}""");

        assertion
                .ignore("data[0].b", "c")
                .jsonEquals(json1, json2);
    }

    @Test
    public void testFailedIfFieldInObjectFromArrayNotEqual() {
        var passed = true;

        try {
            var json1 = new JSONObject("""
                    {"data": [{"a":1, "b":2}, {"a":1, "b":2}], "c": 1}""");
            var json2 = new JSONObject("""
                    {"data": [{"a":1, "b":1}, {"a":1, "b":2}], "c": 2}""");

            assertion
                    .ignore("c")
                    .jsonEquals(json1, json2);
        } catch (AssertionError error) {
            passed = false;
        }

        assertFalse(passed);
    }

    @Test
    public void testCanIgnoreFieldsInArray() {
        var json1 = new JSONObject("""
                {"data": [1, 2]}""");
        var json2 = new JSONObject("""
                {"data": [3, 2]}""");

        assertion
                .ignore("data[0]")
                .jsonEquals(json1, json2);
    }
}
