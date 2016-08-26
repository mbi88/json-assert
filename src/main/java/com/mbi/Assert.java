package com.mbi;

interface Assert {

    <T, U> void jsonEquals(T actual, U expected);

    Assertion ignore(String[] ignore);

    Assertion withMode(CompareMode mode);
}
