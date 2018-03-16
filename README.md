[![Build Status](https://travis-ci.org/mbi88/json-assert.svg?branch=master)](https://travis-ci.org/mbi88/json-assert)

## About
Based on
 - <a href="https://github.com/skyscreamer/JSONassert">JSONAssert</a>.
 - <a href="https://github.com/stleary/JSON-java">JSON-java</a>.

 Use for json comparison.
 <p>
 Compares json objects or json arrays if they are equal.
 <p>
 
 Acceptable actual object:
 - org.json.JSONObject
 - org.json.JSONArray
 - io.restassured.response.Response
 <p>
 
 Acceptable expected object:
 - org.json.JSONObject
 - org.json.JSONArray
 - org.json.JSONObject[]
 <p>
 
 By default the comparison is performed on the full objects coincidence and objects shouldn't be sorted in arrays.
 To set up a different compare mode see available compare mode list: com.mbi.CompareMode.
 
 Example. We have following arrays:
 actual - [{"id": 2, "name": "string", "structured": true}, {"id": 1, "name": "string", "structured": true}]
 expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
 
 To check if arrays objects sorting is equal we should use:
 
 `JsonAssert assertion = new JsonAssert();
  assertion
        .withMode(CompareMode.ORDERED)
        .jsonEquals(expected, actual);`
 
 As the result we catch AssertionError here.
 If sorting equality is not necessary use CompareMode.NOT_ORDERED instead.

 Sometimes there is no need to compare all fields in objects, some fields can be ignored. Use
 com.mbi.JsonAssert#ignore(String...) to ignore fields.
 
 Example. We have following arrays:
 actual - [{"id": 1, "name": "string", "structured": false}, {"id": 2, "name": "string", "structured": true}]
 expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
 
 To check if jsons are equal without checking "structured" field we should use:
 
 `JsonAssert assertion = new JsonAssert();
  assertion
    .ignore("structured")
    .jsonEquals(expected, actual);`
   
 The assertion is passed.
 <p>
 Sometimes we have an array as an actual result but expected result consists of a group of json objects. In this case
 we are able to use JSONObject[] as an expected result:
 
 `new JsonAssert().jsonEquals(actualJsonArray, expectedJsonObject1, expectedJsonObject2, expectedJsonObject3);`
 <p>
 
 Sometimes expected json array may not contain all the actual json array objects.
 Use `CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY` or `CompareMode.ORDERED_EXTENSIBLE_ARRAY`.
 
 <p>
 Use a io.restassured.response.Response1 as an "actual" argument.
 <p>
 
 For more usages see tests.
  
## Example

```java
import com.mbi.CompareMode;
import com.mbi.JsonAssert;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

public class JsonAssertTest {

    private final JsonAssert assertion = new JsonAssert();

    @Test
    public void test1() {
        // Set expected json 
        JSONObject expected = new JSONObject()
                .put("a", 1)
                .put("b", 2);
        // Set actual json
        JSONObject actual = new JSONObject()
                .put("a", 1)
                .put("b", 2)
                .put("c", 3);

        assertion
                .ignore("c")
                .jsonEquals(actual, expected);
    }

    @Test
    public void test2() {
        // Set expected json 
        JSONArray expected = new JSONArray()
                .put(new JSONObject().put("a", 1))
                .put(new JSONObject().put("a", 2));
        // Set actual json
        JSONArray actual = new JSONArray()
                .put(new JSONObject().put("a", 1))
                .put(new JSONObject().put("a", 2).put("b", "extra"))
                .put(new JSONObject().put("a", 3));

        assertion
                .ignore("b")
                .withMode(CompareMode.ORDERED_EXTENSIBLE_ARRAY)
                .jsonEquals(actual, expected);
    }
}
```
