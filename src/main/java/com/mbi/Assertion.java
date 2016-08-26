package com.mbi;

public class Assertion implements Assert {

    private CompareMode mode;
    private String[] ignore;

    public Assertion() {
        // Default mode
        mode = CompareMode.NOT_ORDERED;
        // Default fields to ignore
        ignore = new String[]{""};
    }

    public <T, U> void jsonEquals(T expected, U actual) {
        AssertionBuilder.Builder builder = new AssertionBuilder().newBuilder();

        builder
                .setActual(actual)
                .setExpected(expected)
                .setMode(mode)
                .setIgnore(ignore)
                .build();
    }

    public Assertion ignore(String[] ignore) {
        this.ignore = ignore;

        return this;
    }

    public Assertion withMode(CompareMode mode) {
        this.mode = mode;

        return this;
    }
}