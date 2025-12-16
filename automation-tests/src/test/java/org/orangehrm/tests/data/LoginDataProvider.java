package org.orangehrm.tests.data;

import org.testng.annotations.DataProvider;

public class LoginDataProvider {

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][]{
                {"Admin", "WrongPass123"},
                {"admin", "admin123"},
                {"AdminWrong", "adminWrong"},
                {"AdminWrong", "admin123"}
                /*{"", "admin123"},
                {"Admin", ""},
                {"", ""},*/
        };
    }
}