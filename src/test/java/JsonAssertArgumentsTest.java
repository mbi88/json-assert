import com.jayway.restassured.response.Response;
import com.mbi.JsonAssert;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.get;

public class JsonAssertArgumentsTest {

    private JsonAssert assertion = new JsonAssert();
    private Response responseArray = get("http://www.mocky.io/v2/580e019b120000880d078776");
    private Response responseObject = get("http://www.mocky.io/v2/580e0867120000e20d07878c");
    private JSONObject object1 = new JSONObject(
            "{\"widget\": { \"debug\": \"on\", \"window\": { \"title\": \"Sample Konfabulator Widget1\", \"name\": \"main_window1\", \"width\": 1500, \"height\": 1500 }, \"image\": { \"src\": \"Images/Sun.png\", \"name\": \"sun1\", \"hOffset\": 250, \"vOffset\": 250, \"alignment\": \"center\" }, \"text\": { \"data\": \"Click Here\", \"size\": 36, \"style\": \"bold\", \"name\": \"text1\", \"hOffset\": 250, \"vOffset\": 100, \"alignment\": \"center\", \"onMouseUp\": \"sun1.opacity = (sun1.opacity / 100) * 90;\" } }}");
    private JSONObject object2 = new JSONObject(
            "{\"widget\": { \"debug\": \"on\", \"window\": { \"title\": \"Sample Konfabulator Widget2\", \"name\": \"main_window2\", \"width\": 2500, \"height\": 2500 }, \"image\": { \"src\": \"Images/Sun.png\", \"name\": \"sun2\", \"hOffset\": 250, \"vOffset\": 250, \"alignment\": \"center\" }, \"text\": { \"data\": \"Click Here\", \"size\": 36, \"style\": \"bold\", \"name\": \"text2\", \"hOffset\": 250, \"vOffset\": 100, \"alignment\": \"center\", \"onMouseUp\": \"sun2.opacity = (sun1.opacity / 100) * 90;\" } }}");
    private JSONObject object3 = new JSONObject(
            "{\"widget\": { \"debug\": \"on\", \"window\": { \"title\": \"Sample Konfabulator Widget3\", \"name\": \"main_window3\", \"width\": 3500, \"height\": 3500 }, \"image\": { \"src\": \"Images/Sun.png\", \"name\": \"sun3\", \"hOffset\": 250, \"vOffset\": 250, \"alignment\": \"center\" }, \"text\": { \"data\": \"Click Here\", \"size\": 36, \"style\": \"bold\", \"name\": \"text3\", \"hOffset\": 250, \"vOffset\": 100, \"alignment\": \"center\", \"onMouseUp\": \"sun3.opacity = (sun1.opacity / 100) * 90;\" } }}");
    private JSONArray array = new JSONArray(
            "[ {\"widget\": { \"debug\": \"on\", \"window\": { \"title\": \"Sample Konfabulator Widget1\", \"name\": \"main_window1\", \"width\": 1500, \"height\": 1500 }, \"image\": { \"src\": \"Images/Sun.png\", \"name\": \"sun1\", \"hOffset\": 250, \"vOffset\": 250, \"alignment\": \"center\" }, \"text\": { \"data\": \"Click Here\", \"size\": 36, \"style\": \"bold\", \"name\": \"text1\", \"hOffset\": 250, \"vOffset\": 100, \"alignment\": \"center\", \"onMouseUp\": \"sun1.opacity = (sun1.opacity / 100) * 90;\" } }}, {\"widget\": { \"debug\": \"on\", \"window\": { \"title\": \"Sample Konfabulator Widget2\", \"name\": \"main_window2\", \"width\": 2500, \"height\": 2500 }, \"image\": { \"src\": \"Images/Sun.png\", \"name\": \"sun2\", \"hOffset\": 250, \"vOffset\": 250, \"alignment\": \"center\" }, \"text\": { \"data\": \"Click Here\", \"size\": 36, \"style\": \"bold\", \"name\": \"text2\", \"hOffset\": 250, \"vOffset\": 100, \"alignment\": \"center\", \"onMouseUp\": \"sun2.opacity = (sun1.opacity / 100) * 90;\" } }}, {\"widget\": { \"debug\": \"on\", \"window\": { \"title\": \"Sample Konfabulator Widget3\", \"name\": \"main_window3\", \"width\": 3500, \"height\": 3500 }, \"image\": { \"src\": \"Images/Sun.png\", \"name\": \"sun3\", \"hOffset\": 250, \"vOffset\": 250, \"alignment\": \"center\" }, \"text\": { \"data\": \"Click Here\", \"size\": 36, \"style\": \"bold\", \"name\": \"text3\", \"hOffset\": 250, \"vOffset\": 100, \"alignment\": \"center\", \"onMouseUp\": \"sun3.opacity = (sun1.opacity / 100) * 90;\" } }} ]");
    private JSONObject[] objects = {object1, object2, object3};

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
