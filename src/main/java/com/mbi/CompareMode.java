package com.mbi;

import org.skyscreamer.jsonassert.JSONCompareMode;

public enum CompareMode {

    /**
     * Used by default. Not ordered, not extensible.
     * <p>
     * Examples for json objects assertion:
     * <p>
     * 1.
     * actual - {"id": 1, "name": "string"}
     * expected - {"id": 1, "name": "string", "structured": true}
     * Assertion result: failed
     * <p>
     * 2.
     * actual - {"id": 1, "name": "string", "structured": true}
     * expected - {"id": 1, "name": "string"}
     * Assertion result: failed
     * <p>
     * 3.
     * actual - {"id": 1, "name": "string", "structured": true}
     * expected - {"id": 1, "name": "string", "structured": true}
     * Assertion result: succeed
     * <p>
     * Examples for json arrays assertion:
     *
     * 1.
     * actual - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string"}]
     * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * Assertion result: failed
     * <p>
     * 2.
     * actual - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * expected -[{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string"}]
     * Assertion result: failed
     * <p>
     * 3.
     * actual - [{"id": 2, "name": "string", "structured": true}, {"id": 1, "name": "string", "structured": true}]
     * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * Assertion result: succeed
     * <p>
     * 4.
     * actual - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * Assertion result: succeed
     */
    NOT_ORDERED(false, false),

    /**
     * Ordered, not extensible.
     * <p>
     * Examples for json objects assertion:
     * <p>
     * 1.
     * actual - {"id": 1, "name": "string", "structured": true}
     * expected - {"id": 1, "name": "string", "structured": true}
     * Assertion result: true
     * <p>
     * 2.
     * actual - {"structured": true, "id": 1, "name": "string"}
     * expected - {"id": 1, "name": "string", "structured": true}
     * Assertion result: succeed
     * <p>
     * 3.
     * actual - {"structured": true, "id": 1}
     * expected - {"id": 1, "name": "string", "structured": true}
     * Assertion result: failed
     * <p>
     * 4.
     * actual - {"id": 1, "name": "string", "structured": true}
     * expected - {"structured": true, "id": 1}
     * Assertion result: failed
     * <p>
     * Examples for json arrays assertion:
     * <p>
     * 1.
     * actual - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string"}]
     * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * Assertion result: failed
     * <p>
     * 2.
     * actual - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string"}]
     * Assertion result: failed
     * <p>
     * 3.
     * actual - [{"id": 2, "name": "string", "structured": true}, {"id": 1, "name": "string", "structured": true}]
     * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * Assertion result: failed
     * <p>
     * 4.
     * actual - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * Assertion result: succeed
     */
    ORDERED(true, false),

    /**
     * Not ordered, extensible array.
     * <p>
     * Examples for json arrays assertion:
     * <p>
     * 1.
     * actual - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * expected - [{"id": 1, "name": "string", "structured": true}]
     * Assertion result: succeed
     * <p>
     * 2.
     * actual - [{"id": 1, "name": "string", "structured": true}]
     * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * Assertion result: failed
     * <p>
     * 3.
     * actual - [{"id": 2, "name": "string", "structured": true}, {"id": 1, "name": "string", "structured": true}, {"id": 3, "name": "string", "structured": false}]
     * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}, {"id": 3, "name": "string", "structured": false}]
     * Assertion result: succeed
     * <p>
     * 4.
     * actual - [{"id": 2, "name": "string", "structured": true}, {"id": 1, "name": "string", "structured": true}, {"id": 3, "name": "string", "structured": false}]
     * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * Assertion result: succeed
     */
    NOT_ORDERED_EXTENSIBLE_ARRAY(false, true),

    /**
     * Ordered, extensible array.
     * <p>
     * Examples for json arrays assertion:
     * <p>
     * 1.
     * actual - [{"id": 2, "name": "string", "structured": true}, {"id": 1, "name": "string", "structured": true}, {"id": 3, "name": "string", "structured": false}]
     * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}, {"id": 3, "name": "string", "structured": false}]
     * Assertion result: failed
     * <p>
     * 2.
     * actual - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}, {"id": 3, "name": "string", "structured": false}]
     * Assertion result: failed
     * <p>
     * 3.
     * actual - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}, {"id": 3, "name": "string", "structured": false}]
     * expected - [{"id": 1, "name": "string", "structured": true}, {"id": 2, "name": "string", "structured": true}]
     * Assertion result: true
     */
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
        return this.extensibleArray;
    }

    /**
     * Method to transform custom compare mode to JSONCompareMode
     *
     * @param mode compare mode
     * @return json compare mode
     */
    // TODO: 10/13/16 refactoring is needed
    protected static JSONCompareMode getCompareMode(CompareMode mode) {
        JSONCompareMode jsonCompareMode = null;

        if (mode.isOrdered()) {
            jsonCompareMode = JSONCompareMode.STRICT;
        } else if (!mode.isOrdered()) {
            jsonCompareMode = JSONCompareMode.NON_EXTENSIBLE;
        }

        return jsonCompareMode;
    }
}
