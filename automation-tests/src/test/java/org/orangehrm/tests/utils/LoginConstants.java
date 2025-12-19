package org.orangehrm.tests.utils;

public class LoginConstants {
    public static final String VALID_USERNAME = "Admin";
    public static final String VALID_PASSWORD = "admin123";

    public static final String INVALID_PASSWORD = "WrongPassword123";
    public static final String INVALID_USERNAME = "NoSuchUser";

    public static final String ERROR_INVALID_CREDENTIALS = "Invalid credentials";
    public static final String ERROR_TOO_MANY_ATTEMPTS = "Too many attempts, please try again later";

    public static final int MAX_FAILED_ATTEMPTS = 5;
}
