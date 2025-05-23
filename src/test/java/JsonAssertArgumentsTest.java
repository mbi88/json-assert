import com.mbi.JsonAssert;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

public class JsonAssertArgumentsTest {

    private final JsonAssert assertion = new JsonAssert();
    private final Response responseArray = get("https://api.npoint.io/efb3f7b515781b5e1a7d");
    private final Response responseObject = get("https://api.npoint.io/a4600f1cd1c37a334ccf");
    private final JSONObject object1 = new JSONObject("""
            {
              "widget": {
                "debug": "on",
                "window": {"title": "Sample Konfabulator Widget1", "name": "main_window1", "width": 1500, "height": 1500},
                "image": {"src": "Images/Sun.png", "name": "sun1", "hOffset": 250, "vOffset": 250, "alignment": "center"},
                "text": {"data": "Click Here", "size": 36, "style": "bold", "name": "text1", "hOffset": 250, "vOffset": 100, "alignment": "center", "onMouseUp": "sun1.opacity = (sun1.opacity / 100) * 90;"}
              }
            }""");
    private final JSONObject object2 = new JSONObject("""
            {
              "widget": {
                "debug": "on",
                "window": {"title": "Sample Konfabulator Widget2", "name": "main_window2", "width": 2500, "height": 2500},
                "image": {"src": "Images/Sun.png", "name": "sun2", "hOffset": 250, "vOffset": 250, "alignment": "center"},
                "text": {"data": "Click Here", "size": 36, "style": "bold", "name": "text2", "hOffset": 250, "vOffset": 100, "alignment": "center", "onMouseUp": "sun2.opacity = (sun1.opacity / 100) * 90;"}
              }
            }""");
    private final JSONObject object3 = new JSONObject("""
            {
              "widget": {
                "debug": "on",
                "window": {"title": "Sample Konfabulator Widget3", "name": "main_window3", "width": 3500, "height": 3500},
                "image": {"src": "Images/Sun.png", "name": "sun3", "hOffset": 250, "vOffset": 250, "alignment": "center"},
                "text": {"data": "Click Here", "size": 36, "style": "bold", "name": "text3", "hOffset": 250, "vOffset": 100, "alignment": "center", "onMouseUp": "sun3.opacity = (sun1.opacity / 100) * 90;"}
              }
            }""");
    private final JSONArray array = new JSONArray("""
            [
              {
                "widget": {
                  "debug": "on",
                  "window": {"title": "Sample Konfabulator Widget1","name": "main_window1","width": 1500,"height": 1500},
                  "image": {"src": "Images/Sun.png","name": "sun1","hOffset": 250,"vOffset": 250,"alignment": "center"},
                  "text": {"data": "Click Here","size": 36,"style": "bold","name": "text1","hOffset": 250,"vOffset": 100,"alignment": "center","onMouseUp": "sun1.opacity = (sun1.opacity / 100) * 90;"}
                }
              },
              {
                "widget": {
                  "debug": "on",
                  "window": {"title": "Sample Konfabulator Widget2","name": "main_window2","width": 2500,"height": 2500},
                  "image": {"src": "Images/Sun.png","name": "sun2","hOffset": 250,"vOffset": 250,"alignment": "center"},
                  "text": {"data": "Click Here","size": 36,"style": "bold","name": "text2","hOffset": 250,"vOffset": 100,"alignment": "center","onMouseUp": "sun2.opacity = (sun1.opacity / 100) * 90;"}
                }
              },
              {
                "widget": {
                  "debug": "on",
                  "window": {"title": "Sample Konfabulator Widget3","name": "main_window3","width": 3500,"height": 3500},
                  "image": {"src": "Images/Sun.png","name": "sun3","hOffset": 250,"vOffset": 250,"alignment": "center"},
                  "text": {"data": "Click Here","size": 36,"style": "bold","name": "text3","hOffset": 250,"vOffset": 100,"alignment": "center","onMouseUp": "sun3.opacity = (sun1.opacity / 100) * 90;"}
                }
              }
            ]""");
    private final JSONObject[] objects = {object1, object2, object3};

    @Test
    public void testResponseWithObject() {
        assertion.jsonEquals(responseObject, object1);
    }

    @Test
    public void testResponseWithArray() {
        assertion.jsonEquals(responseArray, array);
    }

    @Test
    public void testResponseWithObjects() {
        assertion.jsonEquals(responseArray, objects);
    }

    @Test
    public void testObjectWithObject() {
        assertion.jsonEquals(object1, object1);
    }

    @Test
    public void testArrayWithArray() {
        assertion.jsonEquals(array, array);
    }

    @Test
    public void testArrayWithObjects() {
        assertion.jsonEquals(array, objects);
    }

    @Test
    public void testJsonObjectFieldsNotRemovedAfterIgnore() {
        var j = new JSONObject("""
                {"a": 1, "b": 1}}""");
        assertion
                .ignore("a")
                .jsonEquals(j, new JSONObject("""
                        {"a": 1, "b": 1}}"""));
        j.get("a");
    }

    @Test
    public void testJsonArraysObjectFieldsNotRemovedAfterIgnore() {
        var j = new JSONArray("""
                [{"a": 1, "b": 1}, {"a": 2, "b": 1}]""");
        assertion
                .ignore("a")
                .jsonEquals(j, new JSONArray("""
                        [{"a": 1,"b": 1}, {"a": 2,"b": 1}]"""));
        j.getJSONObject(0).getInt("a");
    }

    @Test
    public void testJsonEqualsResponseWithWrongObjectShouldFail() {
        var wrongObject = new JSONObject().put("some", "value");
        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(responseObject, wrongObject));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testJsonEqualsResponseWithWrongArrayShouldFail() {
        var wrongArray = new JSONArray().put(new JSONObject().put("q", 123));
        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(responseArray, wrongArray));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testJsonEqualsArrayWithWrongObjectsShouldFail() {
        var wrongObjects = new JSONObject[]{
                new JSONObject().put("x", 1),
                new JSONObject().put("y", 2)
        };
        var ex = expectThrows(AssertionError.class, () -> assertion.jsonEquals(array, wrongObjects));
        assertTrue(ex.getMessage().contains("But found"));
    }

    @Test
    public void testJsonNotEqualsWithDifferentObjectsShouldPass() {
        assertion.jsonNotEquals(object1, object2);
    }

    @Test
    public void testJsonNotEqualsWithSameObjectShouldFail() {
        var ex = expectThrows(AssertionError.class, () -> assertion.jsonNotEquals(object1, object1));
        assertTrue(ex.getMessage().contains("Objects are equal!"));
    }

    @Test
    public void testJsonEqualsEmptyArrayWithEmptyObjectShouldFail() {
        var ex = expectThrows(IllegalArgumentException.class, () -> assertion.jsonEquals(new JSONArray(), new JSONObject()));
        assertTrue(ex.getMessage().contains("You removed all fields from json!"));
    }

    @Test
    public void testJsonEqualsEmptyArrayWithEmptyArrayShouldPass() {
        assertion.jsonEquals(new JSONArray(), new JSONArray());
    }
}
