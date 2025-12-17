package org.orangehrm.tests.data;

import org.testng.annotations.DataProvider;

import static org.orangehrm.tests.utils.LoginConstants.*;

public class LoginDataProvider {

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][]{
                {VALID_USERNAME, INVALID_PASSWORD},
                {"admin", VALID_PASSWORD},
                {INVALID_USERNAME, INVALID_PASSWORD},
                {INVALID_USERNAME, VALID_PASSWORD}
        };
    }

    @DataProvider(name = "mandatoryFields")
    public Object[][] mandatoryFields() {
        return new Object[][]{
                {"",  VALID_PASSWORD, 1},
                {VALID_USERNAME, "", 1},
                {"", "", 2}
        };
    }
}