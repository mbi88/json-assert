package com.mbi;

import org.testng.annotations.Test;

import java.util.Set;

import static com.mbi.AssertionUtils.getParentFields;
import static org.testng.Assert.assertEquals;

public class AssertionUtilsTest {

    @Test
    public void testName() {
        var list = Set.of("a.b", "a", "a.b.c", "b", "c.d");

        assertEquals(getParentFields.apply(list), Set.of("a", "b", "c.d"));
    }

}