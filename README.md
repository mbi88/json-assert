[![Java CI with Gradle](https://github.com/mbi88/json-assert/actions/workflows/gradle.yml/badge.svg)](https://github.com/mbi88/json-assert/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/mbi88/json-assert/branch/master/graph/badge.svg)](https://codecov.io/gh/mbi88/json-assert)
[![Latest Version](https://img.shields.io/github/v/tag/mbi88/json-assert?label=version)](https://github.com/mbi88/json-assert/releases)
[![jitpack](https://jitpack.io/v/mbi88/json-assert.svg)](https://jitpack.io/#mbi88/json-assert)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)


# json-assert

Fluent Java library for flexible and powerful JSON comparisons. Supports deep equality, order sensitivity, partial comparisons, and field-level filtering.

---

## Features

✅ Compare `JSONObject`, `JSONArray`, or REST-assured `Response`  
✅ Full support for nested structures and arrays  
✅ Match JSON arrays as extensible (subset) or exact  
✅ Ignore specific fields or JSON paths  
✅ Compare only selected fields or paths  
✅ Built-in comparison modes  
✅ Helpful failure messages (detailed diffs)

---

## Installation

<details>
<summary>Gradle (Kotlin DSL)</summary>

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    testImplementation("com.github.mbi88:json-assert:master-SNAPSHOT")
}
```

</details>

<details>
<summary>Gradle (Groovy DSL)</summary>

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    testImplementation 'com.github.mbi88:json-assert:master-SNAPSHOT'
}
```

</details>

---

## Example

```java
@Test
public void testJsonComparison() {
    JSONObject expected = new JSONObject().put("a", 1).put("b", 2);
    JSONObject actual = new JSONObject().put("a", 1).put("b", 2).put("c", 3);

    new JsonAssert()
        .ignore("c")
        .jsonEquals(actual, expected);
}
```

---

## Compare Modes

Available via `CompareMode` enum:

| Mode                             | Description |
|----------------------------------|-------------|
| `ORDERED`                        | Arrays must match exactly and in order |
| `NOT_ORDERED`                   | Arrays must contain same elements in any order |
| `ORDERED_EXTENSIBLE_ARRAY`      | Actual array may contain extra elements at the end |
| `NOT_ORDERED_EXTENSIBLE_ARRAY`  | Actual array may contain extra elements in any order |

```java
new JsonAssert()
    .withMode(CompareMode.NOT_ORDERED_EXTENSIBLE_ARRAY)
    .jsonEquals(actual, expected);
```

---

## Ignore fields

Use `ignore(String...)` to skip specific fields or JSON paths:

```java
new JsonAssert()
    .ignore("meta.timestamp", "user.password")
    .jsonEquals(actual, expected);
```

Supports:
- Nested fields: `"data.attributes.name"`
- Arrays: `"items[0].id"`, `"items[].id"`
- Partial matches on any level

---

## Compare only fields

Use `compareOnly(String...)` to restrict comparison to specific fields:

```java
new JsonAssert()
    .compareOnly("user.id", "user.email")
    .jsonEquals(actual, expected);
```

When using `compareOnly()`, all other fields are ignored. Can be combined with `ignore()` for edge cases.

---

## Array as multiple objects

You can also compare a `JSONArray` against a list of `JSONObject` instances:

```java
new JsonAssert().jsonEquals(array, obj1, obj2, obj3);
```

---

## Supports Rest-Assured

You can use `Response` directly from Rest-Assured:

```java
Response response = get("/api/user");
new JsonAssert().jsonEquals(response, expectedJson);
```

---

## See also

- [JSON-java](https://github.com/stleary/JSON-java)
- [JSONassert](https://github.com/skyscreamer/JSONassert)
- [Rest-Assured](https://github.com/rest-assured/rest-assured)

---

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file.
