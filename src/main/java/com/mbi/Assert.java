package com.mbi;

interface Assert {

    /**
     *
     * @param actual
     * @param expected
     * @param <T>
     * @param <U>
     */
    <T, U> void jsonEquals(T actual, U expected);

    /**
     *
     * @param mode
     * @return
     */
    JsonAssert withMode(CompareMode mode);

    /**
     *
     * @param ignore
     * @return
     */
    JsonAssert ignore(String[] ignore);
}
