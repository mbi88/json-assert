package com.mbi;

class AssertBuilder {

    private Object actual;
    private Object expected;
    private CompareMode mode;
    private String[] ignore;

    Builder newBuilder() {
        return this.new Builder();
    }

    private void assertion() {
        AssertDirector assertDirector = new AssertDirector(this);
        assertDirector.doAssertion();
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
            AssertBuilder.this.actual = actual;
            return this;
        }

        Builder setExpected(Object expected) {
            AssertBuilder.this.expected = expected;
            return this;
        }

        Builder setMode(CompareMode mode) {
            AssertBuilder.this.mode = mode;
            return this;
        }

        Builder setIgnore(String... ignore) {
            AssertBuilder.this.ignore = ignore;
            return this;
        }

        void build() {
            AssertBuilder.this.assertion();
        }
    }
}