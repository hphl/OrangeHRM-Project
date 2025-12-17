package org.orangehrm.tests.utils;

public class TestDataUtil {

    public static String uniqueEmployeeId() {
        return "EMP" + System.currentTimeMillis() % 10000000;
    }
}
