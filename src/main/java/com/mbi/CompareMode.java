package com.mbi;

import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 * Supported comparison modes for JSON equality checks.
 * <p>
 * Controls how strictly JSON objects and arrays are compared.
 * Modes can be:
 * - Ordered / Not ordered
 * - Extensible (allowing extra fields in arrays) or not
 */
public enum CompareMode {

    /**
     * Default mode.
     * Objects and arrays must match exactly in fields and order.
     */
    NOT_ORDERED(false, false),

    /**
     * Enforces order in arrays but doesn't allow extra elements.
     */
    ORDERED(true, false),

    /**
     * Ignores order and allows extra elements in the actual array.
     */
    NOT_ORDERED_EXTENSIBLE_ARRAY(false, true),

    /**
     * Enforces order but allows extra elements in the actual array.
     */
    ORDERED_EXTENSIBLE_ARRAY(true, true);

    /**
     * Objects in array should be ordered.
     */
    private final boolean ordered;

    /**
     * Array may contain extra objects.
     */
    private final boolean extensibleArray;

    /**
     * Constructor.
     *
     * @param ordered         if objects in array should be ordered.
     * @param extensibleArray if array may contain extra objects.
     */
    CompareMode(final boolean ordered, final boolean extensibleArray) {
        this.ordered = ordered;
        this.extensibleArray = extensibleArray;
    }

    /**
     * Maps custom CompareMode to the closest JSONCompareMode.
     *
     * @param mode compare mode
     * @return json compare mode
     */
    protected static JSONCompareMode getCompareMode(final CompareMode mode) {
        return mode.isOrdered()
                ? JSONCompareMode.STRICT
                : JSONCompareMode.NON_EXTENSIBLE;
    }

    /**
     * Returns if objects in array should be ordered.
     *
     * @return if objects in array should be ordered.
     */
    public boolean isOrdered() {
        return this.ordered;
    }

    /**
     * Returns if array may contain extra objects.
     *
     * @return if array may contain extra objects.
     */
    public boolean isExtensibleArray() {
        return this.extensibleArray;
    }
}
