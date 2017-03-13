import com.jayway.restassured.response.Response;
import com.mbi.JsonAssert;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.get;
import static org.testng.Assert.assertFalse;

public class JsonAssertArgumentsTest {

    private JsonAssert assertion = new JsonAssert();
    private Response responseArray = get("http://www.mocky.io/v2/580e019b120000880d078776");
    private Response responseObject = get("http://www.mocky.io/v2/580e0867120000e20d07878c");
    private JSONObject object1 = new JSONObject("{\"widget\": { \"debug\": \"on\", \"window\": { \"title\": \"Sample Konfabulator Widget1\", \"name\": \"main_window1\", \"width\": 1500, \"height\": 1500 }, \"image\": { \"src\": \"Images/Sun.png\", \"name\": \"sun1\", \"hOffset\": 250, \"vOffset\": 250, \"alignment\": \"center\" }, \"text\": { \"data\": \"Click Here\", \"size\": 36, \"style\": \"bold\", \"name\": \"text1\", \"hOffset\": 250, \"vOffset\": 100, \"alignment\": \"center\", \"onMouseUp\": \"sun1.opacity = (sun1.opacity / 100) * 90;\" } }}");
    private JSONObject object2 = new JSONObject("{\"widget\": { \"debug\": \"on\", \"window\": { \"title\": \"Sample Konfabulator Widget2\", \"name\": \"main_window2\", \"width\": 2500, \"height\": 2500 }, \"image\": { \"src\": \"Images/Sun.png\", \"name\": \"sun2\", \"hOffset\": 250, \"vOffset\": 250, \"alignment\": \"center\" }, \"text\": { \"data\": \"Click Here\", \"size\": 36, \"style\": \"bold\", \"name\": \"text2\", \"hOffset\": 250, \"vOffset\": 100, \"alignment\": \"center\", \"onMouseUp\": \"sun2.opacity = (sun1.opacity / 100) * 90;\" } }}");
    private JSONObject object3 = new JSONObject("{\"widget\": { \"debug\": \"on\", \"window\": { \"title\": \"Sample Konfabulator Widget3\", \"name\": \"main_window3\", \"width\": 3500, \"height\": 3500 }, \"image\": { \"src\": \"Images/Sun.png\", \"name\": \"sun3\", \"hOffset\": 250, \"vOffset\": 250, \"alignment\": \"center\" }, \"text\": { \"data\": \"Click Here\", \"size\": 36, \"style\": \"bold\", \"name\": \"text3\", \"hOffset\": 250, \"vOffset\": 100, \"alignment\": \"center\", \"onMouseUp\": \"sun3.opacity = (sun1.opacity / 100) * 90;\" } }}");
    private JSONArray array = new JSONArray("[ {\"widget\": { \"debug\": \"on\", \"window\": { \"title\": \"Sample Konfabulator Widget1\", \"name\": \"main_window1\", \"width\": 1500, \"height\": 1500 }, \"image\": { \"src\": \"Images/Sun.png\", \"name\": \"sun1\", \"hOffset\": 250, \"vOffset\": 250, \"alignment\": \"center\" }, \"text\": { \"data\": \"Click Here\", \"size\": 36, \"style\": \"bold\", \"name\": \"text1\", \"hOffset\": 250, \"vOffset\": 100, \"alignment\": \"center\", \"onMouseUp\": \"sun1.opacity = (sun1.opacity / 100) * 90;\" } }}, {\"widget\": { \"debug\": \"on\", \"window\": { \"title\": \"Sample Konfabulator Widget2\", \"name\": \"main_window2\", \"width\": 2500, \"height\": 2500 }, \"image\": { \"src\": \"Images/Sun.png\", \"name\": \"sun2\", \"hOffset\": 250, \"vOffset\": 250, \"alignment\": \"center\" }, \"text\": { \"data\": \"Click Here\", \"size\": 36, \"style\": \"bold\", \"name\": \"text2\", \"hOffset\": 250, \"vOffset\": 100, \"alignment\": \"center\", \"onMouseUp\": \"sun2.opacity = (sun1.opacity / 100) * 90;\" } }}, {\"widget\": { \"debug\": \"on\", \"window\": { \"title\": \"Sample Konfabulator Widget3\", \"name\": \"main_window3\", \"width\": 3500, \"height\": 3500 }, \"image\": { \"src\": \"Images/Sun.png\", \"name\": \"sun3\", \"hOffset\": 250, \"vOffset\": 250, \"alignment\": \"center\" }, \"text\": { \"data\": \"Click Here\", \"size\": 36, \"style\": \"bold\", \"name\": \"text3\", \"hOffset\": 250, \"vOffset\": 100, \"alignment\": \"center\", \"onMouseUp\": \"sun3.opacity = (sun1.opacity / 100) * 90;\" } }} ]");
    private JSONObject[] objects = {object1, object2, object3};
    private String string = "Strong";

    // response
    @Test
    public void testResponseWithResponse() {
        boolean failed = false;
        try {
            assertion.jsonEquals(responseArray, responseArray);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

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
    public void testResponseWithString() {
        boolean failed = false;
        try {
            assertion.jsonEquals(responseArray, string);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    // object
    @Test
    public void testObjectWithResponse() {
        boolean failed = false;
        try {
            assertion.jsonEquals(object1, responseObject);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    @Test
    public void testObjectWithObject() {
        assertion.jsonEquals(object1, object1);
    }

    @Test
    public void testObjectWithArray() {
        boolean failed = false;
        try {
            assertion.jsonEquals(object1, array);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    @Test
    public void testObjectWithObjects() {
        boolean failed = false;
        try {
            assertion.jsonEquals(object1, objects);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    @Test
    public void testObjectWithString() {
        boolean failed = false;
        try {
            assertion.jsonEquals(object1, string);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    // array
    @Test
    public void testArrayWithResponse() {
        boolean failed = false;
        try {
            assertion.jsonEquals(array, responseArray);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    @Test
    public void testArrayWithObject() {
        boolean failed = false;
        try {
            assertion.jsonEquals(array, object1);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
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
    public void testArrayWithString() {
        boolean failed = false;
        try {
            assertion.jsonEquals(array, string);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    // objects
    @Test
    public void testObjectsWithResponse() {
        boolean failed = false;
        try {
            assertion.jsonEquals(objects, responseArray);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    @Test
    public void testObjectsWithObject() {
        boolean failed = false;
        try {
            assertion.jsonEquals(objects, object1);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    @Test
    public void testObjectsWithArray() {
        boolean failed = false;
        try {
            assertion.jsonEquals(objects, array);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    @Test
    public void testObjectsWithObjects() {
        boolean failed = false;
        try {
            assertion.jsonEquals(objects, objects);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    @Test
    public void testObjectsWithString() {
        boolean failed = false;
        try {
            assertion.jsonEquals(objects, string);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    // string
    @Test
    public void testStringWithString() {
        boolean failed = false;
        try {
            assertion.jsonEquals(string, string);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    @Test
    public void testStringWithObjects() {
        boolean failed = false;
        try {
            assertion.jsonEquals(string, objects);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    @Test
    public void testStringWithArray() {
        boolean failed = false;
        try {
            assertion.jsonEquals(string, array);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    @Test
    public void testStringWithObject() {
        boolean failed = false;
        try {
            assertion.jsonEquals(string, object1);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    @Test
    public void testStringWithResponse() {
        boolean failed = false;
        try {
            assertion.jsonEquals(string, responseObject);
            failed = true;
        } catch (IllegalArgumentException ignored) {
        }

        assertFalse(failed);
    }

    @Test
    public void testJsonObjectFieldsNotRemovedAfterIgnore() {
        JSONObject j = new JSONObject("{\"a\": 1}");
        assertion
                .ignore("a")
                .jsonEquals(j, new JSONObject("{\"a\": 1}"));
        j.get("a");
    }

    @Test
    public void testJsonArraysObjectFieldsNotRemovedAfterIgnore() {
        JSONArray j = new JSONArray("[{\"a\": 1}, {\"a\": 2}]");
        assertion
                .ignore("a")
                .jsonEquals(j, new JSONArray("[{\"a\": 1}, {\"a\": 2}]"));
        j.getJSONObject(0).getInt("a");
    }
}
