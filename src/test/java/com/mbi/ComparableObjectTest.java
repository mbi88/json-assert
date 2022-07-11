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
    }

    @Test
    public void testNotEqualsIfIncorrectFieldType() {
        assertNotEquals(new ComparableObject(new JSONObject().put("a", "1")), comparableObject);
    }

    @Test
    public void testCantPassNull() {
        boolean passed;
        try {
            comparableObject.equals(null);
            comparableObject.equals(null);
            passed = true;
        } catch (NullPointerException e) {
            passed = false;
            assertEquals(e.getMessage(), "Passed object can't be null");
        }
        assertFalse(passed);
    }
}