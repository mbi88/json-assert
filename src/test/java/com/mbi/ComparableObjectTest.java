package com.mbi;

import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ComparableObjectTest {

    private final JSONObject json = new JSONObject().put("a", 1);
    private ComparableObject comparableObject = new ComparableObject(json);

    @Test
    public void testObjectEquals() {
        assertTrue(comparableObject.equals(new ComparableObject(json)));
    }

    @Test
    public void testNotEqualsIfPassedInappropriateInstance() {
        assertFalse(comparableObject.equals(json));
    }

    @Test
    public void testNotEqualsIfIncorrectFieldType() {
        assertFalse(comparableObject.equals(new ComparableObject(new JSONObject().put("a", "1"))));
    }

    @Test
    public void testCantPassNull() {
        boolean passed;
        try {
            comparableObject.equals(null);
            passed = true;
        } catch (NullPointerException e) {
            passed = false;
            assertEquals(e.getMessage(), "Passed object can't be null");
        }
        assertFalse(passed);
    }
}