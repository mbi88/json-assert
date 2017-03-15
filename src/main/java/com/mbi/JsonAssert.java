package com.mbi;

public class JsonAssert implements Assert {

    private CompareMode mode;
    private String[] ignore;

    public JsonAssert() {
        // Set default mode, ignore
        setDefault();
    }

    private void setDefault() {
        // Default mode
        mode = CompareMode.NOT_ORDERED;
        // Default fields to ignore
        ignore = new String[]{""};
    }
    public <T, U> void jsonEquals(T actual, U expected) {
        AssertBuilder.Builder builder = new AssertBuilder().newBuilder();

        builder
                .setActual(actual)
                .setExpected(expected)
                .setMode(mode)
                .setIgnore(ignore)
                .build();

        // Set default mode, ignore
        setDefault();
    }

    public JsonAssert ignore(String... ignore) {
        this.ignore = ignore;
        return this;
    }

    public JsonAssert withMode(CompareMode mode) {
        this.mode = mode;
        return this;
    }
}