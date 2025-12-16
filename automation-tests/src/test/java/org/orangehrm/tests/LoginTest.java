package org.orangehrm.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.logging.log4j.Logger;
import org.orangehrm.core.logging.LoggerFactory;
import org.orangehrm.pages.DashboardPage;
import org.orangehrm.pages.LoginPage;
import org.orangehrm.tests.data.LoginDataProvider;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.orangehrm.tests.utils.LoginConstants.*;

@Epic("Authentication")
@Feature("Login")
@Listeners({io.qameta.allure.testng.AllureTestNg.class})
public class LoginTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(LoginTest.class);

    @Story("Valid login attempts should show dashboard")
    @Description("Tests valid login with correct credentials")
    @Test
    public void loginWithValidCredentials() {

        log.info("Starting test: loginWithValidCredentials");
        LoginPage login = new LoginPage(driver);
        login.open();
        login.login(VALID_USERNAME, VALID_PASSWORD);
        log.info("Login submitted. Validating Dashboard");

        DashboardPage dashboard = new DashboardPage(driver);
        Assert.assertTrue(dashboard.isDashboardDisplayed(), "Dashboard is not visible.");

        log.info("Test passed: loginWithValidCredentials");
    }

    @Story("Invalid login attempts")
    @Description("User cannot log with invalid credentials")
    @Test(dataProvider = "invalidCredentials", dataProviderClass = LoginDataProvider.class)
    public void loginWithInvalidCredentials(String user, String pass) {

        log.info("Executing loginWithInvalidCredentials");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login(user, pass);

        String error = loginPage.getErrorMessage();

        Assert.assertEquals(error, ERROR_INVALID_CREDENTIALS, "Error message is incorrect");
        Assert.assertTrue(loginPage.isLoginButtonVisible(), "User should remain on login page after failed login");

        DashboardPage dashboard = new DashboardPage(driver);
        Assert.assertFalse(dashboard.isDashboardDisplayed(), "Dashboard should not be visible after invalid login");

        log.info("Invalid login validation completed.");
    }

    @Story("Account lock after multiple failures")
    @Description("User is blocked after multiple failed login attempts")
    @Test
    public void loginShouldBeBlockedAfterMultipleFailedAttempts() {

        log.info("Starting test: loginShouldBeBlockedAfterMultipleFailedAttempts");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        for (int attempt = 1; attempt <= MAX_FAILED_ATTEMPTS; attempt++) {
            loginPage.login(VALID_USERNAME, INVALID_PASSWORD);
        }

        loginPage.login(VALID_USERNAME, INVALID_PASSWORD);

        Assert.assertEquals(
                loginPage.getErrorMessage(),
                ERROR_TOO_MANY_ATTEMPTS,
                "Expected lock message was not shown"
        );

        DashboardPage dashboardPage = new DashboardPage(driver);
        Assert.assertFalse(
                dashboardPage.isDashboardDisplayed(),
                "Dashboard should not be visible after blocked"
        );

        log.info("User correctly blocked after multiple failures");
    }

    @Story("Account remains locked even after correct credentials")
    @Description("After too many failed attempts, user should remain blocked even if correct credentials are used.")
    @Test//(dependsOnMethods = "loginShouldBeBlockedAfterMultipleFailedAttempts")
    public void loginShouldRemainBlockedEvenWithCorrectCredentials() {

        log.info("Starting test: loginShouldRemainBlockedEvenWithCorrectCredentials");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        for (int attempt = 1; attempt <= 10; attempt++) {
            loginPage.login(VALID_USERNAME, INVALID_PASSWORD);
        }

        log.info("Trying one more invalid login (should remain blocked)");
        loginPage.login(VALID_USERNAME, INVALID_PASSWORD);

        log.info("Trying valid credentials while blocked (should still be blocked)");
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        Assert.assertEquals(
                loginPage.getErrorMessage(),
                ERROR_TOO_MANY_ATTEMPTS,
                "Dashboard should not be visible after too many invalid logins"
        );

        DashboardPage dashboardPage = new DashboardPage(driver);
        Assert.assertFalse(
                dashboardPage.isDashboardDisplayed(),
                "Dashboard should not be visible after too many invalid logins"
        );

        log.info("Test passed: user remains blocked even with correct credentials");
    }
}
