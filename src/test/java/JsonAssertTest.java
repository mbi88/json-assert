import com.mbi.CompareMode;
import com.mbi.JsonAssert;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.testng.Assert.*;

public class JsonAssertTest {

    private final JsonAssert assertion = new JsonAssert();

    @Test
    public void testObjectDefaultIgnoreIsNotSet() {
        var expected = new JSONObject("""
                {"q": 1, "w": 2}""");
        var actual = new JSONObject("""
                {"q": 1}""");
        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(expected, actual));
        assertTrue(ex.getMessage().contains("But found"));

        var ex2 = expectThrows(AssertionError.class, () -> assertion.jsonEquals(actual, expected));
        assertTrue(ex2.getMessage().contains("But found"));
    }

    @Test
    public void testArrayDefaultIgnoreIsNotSet() {
        var expected = new JSONArray("""
                [{"q": 1}, {"w":2}]""");
        var actual = new JSONArray("""
                [{"q": 1}]""");

        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(expected, actual));
        assertTrue(ex.getMessage().contains("But found"));

        var ex2 = expectThrows(AssertionError.class, () -> assertion.jsonEquals(actual, expected));
        assertTrue(ex2.getMessage().contains("But found"));
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
        var expected = new JSONObject("""
                {"q": 2}""");
        var actual = new JSONObject("""
                {"q": 1}""");

        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(expected, actual));
        assertTrue(ex.getMessage().contains("But found"));

        var ex2 = expectThrows(AssertionError.class, () -> assertion.jsonEquals(actual, expected));
        assertTrue(ex2.getMessage().contains("But found"));
    }

    @Test
    public void testArraysNotEquals() {
        var expected = new JSONArray("""
                [{"q": 1}]""");
        var actual = new JSONArray("""
                [{"q": 2}]""");

        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(expected, actual));
        assertTrue(ex.getMessage().contains("But found"));

        var ex2 = expectThrows(AssertionError.class, () -> assertion.jsonEquals(actual, expected));
        assertTrue(ex2.getMessage().contains("But found"));
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
        var expected = new JSONObject("""
                {"q": 1, "w":2}""");
        var actual = new JSONObject("""
                {"q": 1}""");

        assertion
                .ignore("w")
                .jsonEquals(expected, actual);

        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(expected, actual));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testArrayNotIgnoresTwice() {
        var expected = new JSONArray("""
                [{"q": 1, "w":2}]""");
        var actual = new JSONArray("""
                [{"q": 1}]""");

        assertion
                .ignore("w")
                .jsonEquals(expected, actual);

        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(expected, actual));
        assertTrue(ex.getMessage().contains("But found"));
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
        var expected = new JSONArray("""
                [{"q": 1}, {"w":2}]""");
        var actual = new JSONArray("""
                [{"w":2}, {"q": 1}]""");
        var actual2 = new JSONArray("""
                [{"q": 1}, {"w":2}, {"e": 3}]""");

        var ex = expectThrows(AssertionError.class, () -> assertion
                .withMode(CompareMode.ORDERED)
                .jsonEquals(expected, actual));
        assertTrue(ex.getMessage().contains("But found"));

        var ex2 = expectThrows(AssertionError.class, () -> assertion
                .withMode(CompareMode.ORDERED)
                .jsonEquals(actual, expected));
        assertTrue(ex2.getMessage().contains("But found"));

        var ex3 = expectThrows(AssertionError.class, () -> assertion
                .withMode(CompareMode.ORDERED)
                .jsonEquals(expected, actual2));
        assertTrue(ex3.getMessage().contains("But found"));
    }

    @Test
    public void testNotOrderedExtensibleArray() {
        var expected = new JSONArray("""
                [{"id": 2, "name": "string", "structured": true}, {"id": 1, "name": "string", "structured": true}]""");
        var actual = new JSONArray("""
                [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}, {"id": 3, "name": "string", "structured": false}]""");

        assertion
                .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                .jsonEquals(actual, expected);

        var ex = expectThrows(AssertionError.class, () -> assertion
                .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                .jsonEquals(expected, actual));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testNoCommonObjects() {
        var expected = new JSONArray("""
                [{"id": 3, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]""");
        var actual = new JSONArray("""
                [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}, {"id": 3, "name": "string", "structured": false}]""");

        var ex = expectThrows(AssertionError.class, () -> assertion
                .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                .jsonEquals(expected, actual));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testOrderedExtensibleArray() {
        var expected = new JSONArray("""
                [{"q": 1}, {"w": 2}]""");
        var expected2 = new JSONArray("""
                [{"w": 2}, {"q": 1}]""");
        var actual = new JSONArray("""
                [{"q": 1}, {"w":2}, {"e": 3}]""");

        assertion
                .withMode(CompareMode.ORDERED_EXTENSIBLE_ARRAY)
                .jsonEquals(actual, expected);

        var ex = expectThrows(AssertionError.class, () -> assertion
                .withMode(CompareMode.ORDERED_EXTENSIBLE_ARRAY)
                .jsonEquals(expected, actual));
        assertTrue(ex.getMessage().contains("But found"));

        var ex2 = expectThrows(AssertionError.class, () -> assertion
                .withMode(CompareMode.ORDERED_EXTENSIBLE_ARRAY)
                .jsonEquals(expected2, actual));
        assertTrue(ex2.getMessage().contains("But found"));
    }

    @Test
    public void testObjectsAssertion() {
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

        var ex = expectThrows(AssertionError.class, () -> assertion
                .ignore("w")
                .withMode(CompareMode.NOT_ORDERED)
                .jsonEquals(expected, actual2));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testArraysAssertion() {
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

        var ex = expectThrows(AssertionError.class, () -> assertion
                .ignore("w")
                .withMode(CompareMode.NOT_ORDERED)
                .jsonEquals(expected, actual2));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testArraysNotOfJsonObjects() {
        var a1 = new JSONArray("[18,17,16,15,14]");
        var a2 = new JSONArray("[18,17,16,15,14]");

        assertion.jsonEquals(a1, a2);
    }

    @Test
    public void testNotEqualArraysNotOfJsonObjects() {
        var a1 = new JSONArray("[18,17,16,15,14]");
        var a2 = new JSONArray("[18,17,16,15,1]");

        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(a1, a2));
        assertTrue(ex.getMessage().contains("But found"));
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
        var j1 = new JSONObject().put("a", new JSONObject().put("b", 1)).put("d", 5);
        var j2 = new JSONObject().put("a", new JSONObject().put("b", 1).put("c", 3));

        var ex = expectThrows(AssertionError.class, () -> assertion
                .ignore("a.e", "d")
                .jsonEquals(j1, j2));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testNullAsObjectOfArray() {
        var j = new JSONArray("""
                [{"a": 1, "b": 1}, {"a": 2, "b": 1}]""");
        var j2 = new JSONArray().put(new JSONObject().put("b", JSONObject.NULL));

        var ex = expectThrows(AssertionError.class, () -> assertion
                .ignore("a")
                .withMode(CompareMode.ORDERED_EXTENSIBLE_ARRAY)
                .jsonEquals(j, j2));
        assertTrue(ex.getMessage().contains("But found"));
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

        var ex = expectThrows(AssertionError.class, () -> assertion
                .compareOnly("c.cc")
                .jsonEquals(json, json2));
        assertTrue(ex.getMessage().contains("But found"));
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

        var ex = expectThrows(AssertionError.class, () -> assertion
                .compareOnly("b")
                .jsonEquals(json, json2));
        assertTrue(ex.getMessage().contains("But found"));
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

        var ex = expectThrows(AssertionError.class, () -> assertion
                .compareOnly("data.name", "data.email")
                .jsonEquals(js, jsonObject));
        assertTrue(ex.getMessage().contains("But found"));
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
        var json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        var json2 = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 3).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1)))
                .put("d", 6);

        var ex = expectThrows(AssertionError.class, () -> assertion
                .ignore("c.cc.ccc2", "b")
                .jsonEquals(json, json2));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testIgnore3() {
        var json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        var json2 = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 3).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1)))
                .put("d", 6);

        var ex = expectThrows(AssertionError.class, () -> assertion
                .ignore("d", "b")
                .jsonEquals(json, json2));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testIgnore4() {
        var json = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 1).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1).put("ccc2", 2)));
        var json2 = new JSONObject()
                .put("a", 1)
                .put("b", new JSONObject().put("b1", 3).put("b2", 2).put("b3", 3))
                .put("c", new JSONObject().put("cc", new JSONObject().put("ccc1", 1)))
                .put("d", 6);

        var ex = expectThrows(AssertionError.class, () -> assertion
                .ignore("d", "c.cc.ccc2")
                .jsonEquals(json, json2));
        assertTrue(ex.getMessage().contains("But found"));
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
        var json = new JSONObject().put("a", 1);
        var json2 = new JSONObject().put("a", 1);

        var ex = expectThrows(IllegalArgumentException.class, () -> assertion
                .ignore("a")
                .jsonEquals(json, json2));
        assertTrue(ex.getMessage().contains("You removed all fields from json"));
    }

    @Test
    public void testFailIfMissedFieldInInnerObject() {
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

        var ex = expectThrows(AssertionError.class, () -> assertion
                .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                .compareOnly("sharedBy", "accountId")
                .jsonEquals(actual, new JSONArray().put(expected)));
        assertTrue(ex.getMessage().contains("Expected 1 values but got 0"));
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
        var ex = expectThrows(AssertionError.class, () -> assertion
                .jsonNotEquals(new JSONObject().put("a", 1), new JSONObject().put("a", 1)));
        assertTrue(ex.getMessage().contains("Objects are equal!"));
    }

    @Test
    public void testJsonArraysNotEqualsSuccess() {
        assertion.jsonNotEquals(new JSONArray().put(new JSONObject().put("a", 1)), new JSONArray().put(new JSONObject().put("a", 2)));
    }

    @Test
    public void testJsonArraysNotEqualsFailedWithExtensibleArray() {
        var jsonArray1 = new JSONArray().put(new JSONObject().put("a", 1));
        var jsonArray2 = new JSONArray().put(new JSONObject().put("a", 2)).put(new JSONObject().put("a", 1));

        var ex = expectThrows(AssertionError.class, () -> assertion
                .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                .jsonNotEquals(jsonArray2, jsonArray1));
        assertTrue(ex.getMessage().contains("Objects are equal!"));
    }

    @Test
    public void testJsonArraysNotEqualsFailed() {
        var ex = expectThrows(AssertionError.class, () -> assertion
                .jsonNotEquals(new JSONArray().put(new JSONObject().put("a", 1)), new JSONArray().put(new JSONObject().put("a", 1))));
        assertTrue(ex.getMessage().contains("Objects are equal!"));
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
        var json1 = new JSONObject("""
                {"data": [{"a":1, "b":2}, {"a":1, "b":2}], "c": 1}""");
        var json2 = new JSONObject("""
                {"data": [{"a":1, "b":1}, {"a":1, "b":2}], "c": 2}""");

        var ex = expectThrows(AssertionError.class, () -> assertion
                .ignore("c")
                .jsonEquals(json1, json2));
        assertTrue(ex.getMessage().contains("But found"));
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

    @Test
    public void testFailIfArraysNotEqual() {
        var json1 = new JSONObject("""
                {"data": [1, 2]}""");
        var json2 = new JSONObject("""
                {"data": [3, 2]}""");

        var ex = expectThrows(AssertionError.class, () -> assertion
                .ignore("data[1]")
                .jsonEquals(json1, json2));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testCanCompareOnlyFieldsInArray() {
        var json1 = new JSONObject("""
                {"data": [1, 2]}""");
        var json2 = new JSONObject("""
                {"data": [3, 2]}""");

        assertion
                .compareOnly("data[1]")
                .jsonEquals(json1, json2);
    }

    @Test
    public void testFailIfArraysNotEqualOnCompareOnly() {
        var json1 = new JSONObject("""
                {"data": [1, 2]}""");
        var json2 = new JSONObject("""
                {"data": [3, 2]}""");

        var ex = expectThrows(AssertionError.class, () -> assertion
                .compareOnly("data[0]")
                .jsonEquals(json1, json2));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testSkipWholeArray() {
        var actual = new JSONArray("""
                [{"incomeBrackets":[0,20000,40000,75000,100000],"countryCode":"FI"},{"incomeBrackets":[0,20000,40000,75000,100000],"countryCode":"GB"}]""");
        var expected = new JSONArray("""
                [{"countryCode":"FI"},{"countryCode":"GB"}]""");

        assertion
                .ignore("incomeBrackets")
                .jsonEquals(actual, expected);
    }

    @Test
    public void testComparisonFailedIfCompareNoEqualArrays() {
        var actual = new JSONArray("""
                [{"incomeBrackets":[0,20000,40000,75000,100000],"countryCode":"FI"},{"incomeBrackets":[0,20000,40000,75000,100000],"countryCode":"GB"}]""");
        var expected = new JSONArray("""
                [{"incomeBrackets":[0,20000,40000,75000,100001], "countryCode":"FI"},{"incomeBrackets":[0,20000,40000,75000,100001], "countryCode":"GB"}]""");

        var ex = expectThrows(AssertionError.class, () -> assertion
                .compareOnly("incomeBrackets")
                .jsonEquals(actual, expected));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testCanIgnoreWholeArrayOfObjects() {
        var j1 = new JSONArray("""
                [
                    {"createdAt": 1, "updatedAt": 2},
                    {"createdAt": 1, "updatedAt": 2, "subItems": [{"createdAt": 1,"updatedAt": 2}]}
                ]""");
        var j2 = new JSONArray("""
                [{"createdAt": 1, "updatedAt": 2},{"createdAt": 1, "updatedAt": 2}]""");

        assertion
                .ignore("subItems")
                .jsonEquals(j1, j2);
    }

    @Test
    public void testFailIfCompareWholeArrayOfObjects() {
        var j1 = new JSONArray("""
                [
                    {"createdAt": 1, "updatedAt": 2},
                    {"createdAt": 1, "updatedAt": 2, "subItems": [{"createdAt": 1,"updatedAt": 2}]}
                ]""");
        var j2 = new JSONArray("""
                [{"createdAt": 1, "updatedAt": 2},{"createdAt": 1, "updatedAt": 2}]""");

        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(j1, j2));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testCanCompareOnlyWholeArrayOfObjects() {
        var j1 = new JSONArray("""
                [
                    {"createdAt": 1, "updatedAt": 2, "subItems": [{"createdAt": 1,"updatedAt": 2}]},
                    {"createdAt": 1, "updatedAt": 2, "subItems": [{"createdAt": 1,"updatedAt": 2}]}
                ]""");
        var j2 = new JSONArray("""
                [
                    {"createdAt": 2, "updatedAt": 2, "subItems": [{"createdAt": 1,"updatedAt": 2}]},
                    {"createdAt": 1, "updatedAt": 2, "subItems": [{"createdAt": 1,"updatedAt": 2}]}
                ]""");

        assertion
                .compareOnly("subItems")
                .jsonEquals(j1, j2);
    }

    @Test
    public void testFailIfCompareOnlyWholeArrayOfObjects() {
        var j1 = new JSONArray("""
                [
                    {"createdAt": 1, "updatedAt": 2, "subItems": [{"createdAt": 1,"updatedAt": 2}]},
                    {"createdAt": 1, "updatedAt": 2, "subItems": [{"createdAt": 1,"updatedAt": 2}]}
                ]""");
        var j2 = new JSONArray("""
                [
                    {"createdAt": 2, "updatedAt": 2, "subItems": [{"createdAt": 1,"updatedAt": 2}]},
                    {"createdAt": 1, "updatedAt": 2, "subItems": [{"createdAt": 1,"updatedAt": 2}]}
                ]""");

        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(j1, j2));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testCanIgnoreArrayInArray() {
        var json1 = new JSONObject("""
                {"data":[{"q":1, "w":[1]}]}""");
        var json2 = new JSONObject("""
                {"data":[{"q":1}]}""");

        assertion
                .ignore("data[0].w")
                .jsonEquals(json1, json2);
    }

    @Test
    public void testFailIfArrayInArrayNotEquals() {
        var json1 = new JSONObject("""
                {"data":[{"q":1, "w":[1]}]}""");
        var json2 = new JSONObject("""
                {"data":[{"q":1, "w":[2]}]}""");

        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(json1, json2));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testCanCompareArrayInArray() {
        var json1 = new JSONObject("""
                {"data":[{"q":1, "w":[1]}]}""");
        var json2 = new JSONObject("""
                {"data":[{"q":2, "w":[1]}]}""");

        assertion
                .compareOnly("data[0].w")
                .jsonEquals(json1, json2);
    }

    @Test
    public void testFailIfCompareArrayInArray() {
        var json1 = new JSONObject("""
                {"data":[{"q":1, "w":[1]}]}""");
        var json2 = new JSONObject("""
                {"data":[{"q":2, "w":[1]}]}""");

        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(json1, json2));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testInnerArrayCanBeIgnored() {
        var j1 = new JSONObject("""
                {"data": {"sys": {
                  "a": 1,
                  "b": ["test1"],
                }}}""");
        var j2 = new JSONObject("""
                {"data": {"sys": {
                  "a": 1,
                  "b": ["test2"],
                }}}""");

        assertion
                .ignore("data.sys.b")
                .jsonEquals(j1, j2);
    }

    @Test
    public void testInnerArrayFail() {
        var j1 = new JSONObject("""
                {"data": {"sys": {
                  "a": 1,
                  "b": ["test1"],
                }}}""");
        var j2 = new JSONObject("""
                {"data": {"sys": {
                  "a": 1,
                  "b": ["test2"],
                }}}""");

        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(j1, j2));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testCanCompareInnerArray() {
        var j1 = new JSONObject("""
                {"data": {"sys": {
                  "a": 1,
                  "b": ["test1"],
                }}}""");
        var j2 = new JSONObject("""
                {"data": {"sys": {
                  "a": 1,
                  "b": ["test2"],
                }}}""");

        var ex = expectThrows(AssertionError.class, () -> assertion
                .compareOnly("data.sys.b")
                .jsonEquals(j1, j2));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testCanCompareIfExistInnerArray() {
        var j1 = new JSONObject("""
                {"data": {"sys": {
                  "a": 1,
                  "b": ["test1"],
                }}}""");
        var j2 = new JSONObject("""
                {"data": {"sys": {
                  "a": 1,
                  "b": ["test2"],
                }}}""");

        assertion
                .compareOnly("data.sys.a")
                .jsonEquals(j1, j2);
    }

    @Test
    public void testCanCompareElementsInInnerArray() {
        var j1 = new JSONObject("""
                {"data": {"sys": {
                  "a": 1,
                  "b": ["test1", "a"],
                }}}""");
        var j2 = new JSONObject("""
                {"data": {"sys": {
                  "a": 1,
                  "b": ["test2", "a"],
                }}}""");

        var ex = expectThrows(AssertionError.class, () -> assertion
                .compareOnly("data.sys.b[0]")
                .jsonEquals(j1, j2));
        assertTrue(ex.getMessage().contains("But found"));

        assertion
                .compareOnly("data.sys.b[1]")
                .jsonEquals(j1, j2);
    }

    @Test
    public void testCanIgnoreElementsInInnerArray() {
        var j1 = new JSONObject("""
                {"data": {"sys": {
                  "a": 1,
                  "b": ["test1", "a"],
                }}}""");
        var j2 = new JSONObject("""
                {"data": {"sys": {
                  "a": 1,
                  "b": ["test2", "a"],
                }}}""");

        var ex = expectThrows(AssertionError.class, () -> assertion
                .ignore("data.sys.b[1]")
                .jsonEquals(j1, j2));
        assertTrue(ex.getMessage().contains("But found"));


        assertion
                .ignore("data.sys.b[0]")
                .jsonEquals(j1, j2);
    }

    @Test
    public void testCanIgnoreIfFieldsNamesStartTheSame() {
        var json1 = new JSONArray("""
                [
                  {
                    "customFieldId": "9bd4ffac-f618-4579-8c6f-0792b2ed9d76",
                    "customField": {
                      "values": [{"name": "name1", "rank": 0, "id": "51abfed0-8b4f-4117-82bb-ec3ca9aa75b9" }],
                      "name": "name1",
                      "id": "9bd4ffac-f618-4579-8c6f-0792b2ed9d76"
                    }
                  },
                  {
                    "customFieldId": "349bcda1-5ec0-4118-a1e8-e059d9739390",
                    "customField": {
                      "values": [{"name": "name2", "rank": 0, "id": "5cfc6d7c-24b2-495e-9e11-0cbaebfd0160" }],
                      "name": "name2",
                      "id": "349bcda1-5ec0-4118-a1e8-e059d9739390"
                    }
                  }
                ]""");
        var json2 = new JSONArray("""
                [
                  {"customField": {
                    "values": [{"name": "name1", "rank": 1}],
                    "name": "name1",
                    "id": "2978df16-54b8-4e05-92ab-4e751217a399",
                  }},
                  {"customField": {
                    "values": [{"name": "name2","rank": 0}],
                    "name": "name2"
                  }}
                ]""");

        assertion
                .ignore("customField.values[0].id", "customField.id", "customFieldId")
                .jsonEquals(json1, json2);
    }

    @Test
    public void testFailedIfFieldsNamesStartTheSame() {
        var json1 = new JSONArray("""
                [
                  {
                    "customFieldId": "9bd4ffac-f618-4579-8c6f-0792b2ed9d76",
                    "customField": {
                      "values": [{"name": "name1", "rank": 0, "id": "51abfed0-8b4f-4117-82bb-ec3ca9aa75b9" }],
                      "name": "name1",
                      "id": "9bd4ffac-f618-4579-8c6f-0792b2ed9d76"
                    }
                  },
                  {
                    "customFieldId": "349bcda1-5ec0-4118-a1e8-e059d9739390",
                    "customField": {
                      "values": [{"name": "name2", "rank": 0, "id": "5cfc6d7c-24b2-495e-9e11-0cbaebfd0160" }],
                      "name": "name2",
                      "id": "349bcda1-5ec0-4118-a1e8-e059d9739390"
                    }
                  }
                ]""");
        var json2 = new JSONArray("""
                [
                  {"customField": {
                    "values": [{"name": "name1", "rank": 1}],
                    "name": "name1",
                    "id": "2978df16-54b8-4e05-92ab-4e751217a399",
                  }},
                  {"customField": {
                    "values": [{"name": "name2","rank": 0}],
                    "name": "name2"
                  }}
                ]""");

        var ex = expectThrows(AssertionError.class, () -> assertion
                .ignore("customField.values[0].id", "customField.id")
                .jsonEquals(json1, json2));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testStateResetBetweenAssertions() {
        var expected = new JSONObject().put("a", 1);
        var actual = new JSONObject().put("a", 1);

        assertion
                .ignore("nonExistent")
                .jsonEquals(expected, actual);

        var different = new JSONObject().put("a", 2);
        var ex = expectThrows(AssertionError.class, () ->
                assertion.jsonEquals(expected, different));

        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testNullActualObject() {
        var expected = new JSONObject().put("a", 1);
        var ex = expectThrows(NullPointerException.class, () ->
                assertion.jsonEquals((JSONObject) null, expected));
        assertTrue(ex.getMessage().contains("is null"));
    }

    @Test
    public void testJsonArraysNotEqualsExtensibleModeNegation() {
        var actual = new JSONArray("""
                [{"id": 1}, {"id": 2}, {"id": 3}]""");
        var expected = new JSONArray("""
                [{"id": 1}, {"id": 2}]""");

        var ex = expectThrows(AssertionError.class, () ->
                assertion
                        .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
                        .jsonNotEquals(actual, expected));
        assertTrue(ex.getMessage().contains("Objects are equal!"));
    }

    @Test
    public void testCompareOnlyAndIgnoreTogether() {
        var json1 = new JSONObject("""
                {"a": {"b": {"c": 1, "d": 2}}, "x": 5}""");
        var json2 = new JSONObject("""
                {"a": {"b": {"c": 1, "d": 999}}, "x": 7}""");

        assertion
                .compareOnly("a.b")
                .ignore("a.b.d")
                .jsonEquals(json1, json2);
    }

    @Test
    public void testCompareOnlyWithNullsInArray() {
        var j1 = new JSONArray().put(JSONObject.NULL).put(new JSONObject().put("a", 1));
        var j2 = new JSONArray().put(JSONObject.NULL).put(new JSONObject().put("a", 1));

        assertion.compareOnly("[1].a").jsonEquals(j1, j2);
    }

    @Test
    public void testIsArrayFieldMatch_MissingBranch() throws Exception {
        // Get class and method using reflection
        final Class<?> clazz = Class.forName("com.mbi.AssertionUtils");
        final Method method = clazz.getDeclaredMethod("isArrayFieldMatch", String.class, String.class);
        method.setAccessible(true);

        // Create input values that cover the missing branch:
        // parent = "[0]id", key = "other[0]id"
        // -> key does not start with parent
        // -> parent is an index (i.e. !parentIsNotIndex == false)
        boolean result = (boolean) method.invoke(null, "[0]id", "other[0]id");

        assertFalse(result);
    }

    @Test
    public void testIsArrayFieldMatchMissingBranch() throws Exception {
        // parent = "array[0]", key = "something"
        // -> parentIsNotIndex = false (it is array), keyStartsWithParent = false
        // -> !keyStartsWithParent || !parentIsNotIndex == true
        // → return false
        // Get class and method using reflection
        final Class<?> clazz = Class.forName("com.mbi.AssertionUtils");
        final Method method = clazz.getDeclaredMethod("isArrayFieldMatch", String.class, String.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null, "array[0]", "something");
        assertFalse(result);
    }

    @Test
    public void testIsArrayFieldMatchKeyDoesNotStartWithParent() throws Exception {
        // parent does NOT end with index (=> parentIsNotIndex = true)
        // key does NOT start with parent (=> keyStartsWithParent = false)
        // Get class and method using reflection
        final Class<?> clazz = Class.forName("com.mbi.AssertionUtils");
        final Method method = clazz.getDeclaredMethod("isArrayFieldMatch", String.class, String.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null, "array", "different[0].id");
        assertFalse(result);
    }

    @Test
    public void testIsArrayFieldMatchParentIsIndexKeyStartsWithParent() throws Exception {
        // parent = "[0]id", key = "[0]id.something"
        // parent is index (matches ^\\[\\d+].*) → parentIsNotIndex = false
        // key starts with parent → keyStartsWithParent = true
        // !keyStartsWithParent = false
        // !parentIsNotIndex = true
        // => if (false || true) → enters → return false
        // Get class and method using reflection
        final Class<?> clazz = Class.forName("com.mbi.AssertionUtils");
        final Method method = clazz.getDeclaredMethod("isArrayFieldMatch", String.class, String.class);
        method.setAccessible(true);
        var result = (boolean) method.invoke(null, "[0]id", "[0]id.something");
        assertFalse(result);
    }
}
