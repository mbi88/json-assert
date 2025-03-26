package com.mbi;

import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ComparableObjectTest {

    private final JSONObject json = new JSONObject().put("a", 1);
    private final ComparableObject comparableObject = new ComparableObject(json);

    @Test
    public void testObjectEquals() {
        assertEquals(new ComparableObject(json), comparableObject);
    }

    @Test
    public void testNotEqualsIfPassedInappropriateInstance() {
        assertNotEquals(json, comparableObject);
        assertNotEquals(comparableObject, json);
    }

    @Test
    public void testNotEqualsIfIncorrectFieldType() {
        assertNotEquals(new ComparableObject(new JSONObject().put("a", "1")), comparableObject);
    }

    @Test
    public void testThrowsIfNullPassedToEquals() {
        var ex = expectThrows(NullPointerException.class, () -> comparableObject.equals(new ComparableObject(null)));
        assertEquals(ex.getMessage(), "Passed object can't be null");
    }

    @Test
    public void testHashCodeIsConsistent() {
        assertEquals(new ComparableObject(json).hashCode(), comparableObject.hashCode());
    }

    @Test
    public void testToStringMatchesOriginalJson() {
        assertEquals(comparableObject.toString(), json.toString());
    }
}