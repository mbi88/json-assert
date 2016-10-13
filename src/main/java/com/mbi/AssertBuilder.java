package com.mbi;

/**
 * Created by mbi on 8/19/16.
 */

class AssertionBuilder {

    private Object actual;
    private Object expected;
    private CompareMode mode;
    private String[] ignore;

    Builder newBuilder() {
        return this.new Builder();
    }

    private void construct() {
        AssertionDirector assertionDirector = new AssertionDirector(this);
        assertionDirector.doAssertion();
    }

    Object getActual() {
        return actual;
    }

    Object getExpected() {
        return expected;
    }

    CompareMode getMode() {
        return mode;
    }

    String[] getIgnore() {
        return ignore;
    }

    class Builder {

        private Builder() {
        }

        Builder setActual(Object actual) {
            AssertionBuilder.this.actual = actual;
            return this;
        }

        Builder setExpected(Object expected) {
            AssertionBuilder.this.expected = expected;
            return this;
        }

        Builder setMode(CompareMode mode) {
            AssertionBuilder.this.mode = mode;
            return this;
        }

        Builder setIgnore(String[] ignore) {
            AssertionBuilder.this.ignore = ignore;
            return this;
        }

        AssertionBuilder build() {
            construct();
            return AssertionBuilder.this;
        }
    }
}