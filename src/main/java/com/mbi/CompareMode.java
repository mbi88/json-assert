package com.mbi;

import org.skyscreamer.jsonassert.JSONCompareMode;

public enum CompareMode {

    NOT_ORDERED(false, false),

    ORDERED(true, false),

    NOT_ORDERED_EXTEBSIBLE_ARRAY(false, true),

    ORDERED_EXTENSIBLE_ARRAY(true, true);

    private final boolean ordered;
    private final boolean extensibleArray;

    CompareMode(boolean ordered, boolean extensibleArray) {
        this.ordered = ordered;
        this.extensibleArray = extensibleArray;
    }

    public boolean isOrdered() {
        return this.ordered;
    }

    public boolean isExtensibleArray() {
        return  this.extensibleArray;
    }

    public static JSONCompareMode getCompareMode(CompareMode mode) {
        JSONCompareMode compareMode = null;

        if (mode.isOrdered()) {
            compareMode = JSONCompareMode.STRICT_ORDER;
        } else if (!mode.isOrdered()) {
            compareMode = JSONCompareMode.LENIENT;
        }

        return compareMode;
    }
}
