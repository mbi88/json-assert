import com.mbi.JsonAssert;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;

public class JsonAssertArgumentsTest {

    private final JsonAssert assertion = new JsonAssert();
    private final Response responseArray = get("https://api.npoint.io/efb3f7b515781b5e1a7d");
    private final Response responseObject = get("https://api.npoint.io/78e49d7b8b5c4b6c9690");
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
}
