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

        login.enterUsername(VALID_USERNAME);
        login.enterPassword(VALID_PASSWORD);
        login.clickLogin();
        log.info("Login submitted. Validating Dashboard…");

        DashboardPage dashboard = new DashboardPage(driver);
        Assert.assertTrue(dashboard.isDashboardDisplayed(), "Dashboard is not visible.");

        log.info("Test passed: loginWithValidCredentials");
    }

    @Story("Invalid login attempts should show an error")
    @Description("Tests multiple invalid credentials and validates error handling.")
    @Test(dataProvider = "invalidCredentials", dataProviderClass = LoginDataProvider.class)
    public void loginWithInvalidCredentials(String user, String pass) {

        log.info("Executing loginWithInvalidCredentials");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login(user, pass);

        String error = loginPage.getErrorMessage();

        log.info("Validating error message...");
        Assert.assertEquals(error, ERROR_INVALID_CREDENTIALS, "Error message is incorrect");

        Assert.assertTrue(loginPage.isLoginButtonVisible(),
                "User should remain on login page after failed login");

        DashboardPage dashboard = new DashboardPage(driver);
        Assert.assertFalse(dashboard.isDashboardDisplayed(),
                "Dashboard should not be visible after invalid login");

        log.info("Invalid login validation completed.");
    }

    /*@Test
    public void loginShouldBeBlockedAfterMultipleFailedAttempts() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        for (int attempt = 1; attempt <= MAX_FAILED_ATTEMPTS; attempt++) {
            loginPage.login(VALID_USERNAME, INVALID_PASSWORD);
        }

        loginPage.login(VALID_USERNAME, INVALID_PASSWORD);

        Assert.assertEquals(
                loginPage.getErrorMessage(),
                ERROR_TOO_MANY_ATTEMPTS,
                "The message 'Too many attempts' should be visible but it didn't"
        );

        DashboardPage dashboardPage = new DashboardPage(driver);
        Assert.assertFalse(
                dashboardPage.isDashboardDisplayed(),
                "The user accessed even he shouldn't"
        );
    }*/

    /*@Test//(dependsOnMethods = "loginShouldBeBlockedAfterMultipleFailedAttempts")
    public void loginShouldRemainBlockedEvenWithCorrectCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        for (int attempt = 1; attempt <= 10; attempt++) {
            loginPage.login(VALID_USERNAME, INVALID_PASSWORD);
        }

        loginPage.login(VALID_USERNAME, INVALID_PASSWORD);
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        Assert.assertEquals(
                loginPage.getErrorMessage(),
                ERROR_TOO_MANY_ATTEMPTS,
                "The message didn't display about too many attempts"
        );

        DashboardPage dashboardPage = new DashboardPage(driver);
        Assert.assertFalse(
                dashboardPage.isDashboardDisplayed(),
                "Dashboard should not be visible after too many invalid logins"
        );
    }*/
}
