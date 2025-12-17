package org.orangehrm.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.orangehrm.core.logging.LoggerFactory;

import java.time.Duration;

import static org.orangehrm.utils.Constants.URL_ORANGE_HRM;


public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By username = By.name("username");
    private final By password = By.name("password");
    private final By loginBtn = By.cssSelector("button[type='submit']");
    private final By errorMessage = By.cssSelector(".oxd-alert-content-text");
    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);
    private final By requiredFieldErrors = By.cssSelector(".oxd-input-field-error-message");


    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        log.info("Opening Login Page: {}", URL_ORANGE_HRM);
        driver.get(URL_ORANGE_HRM);
        wait.until(ExpectedConditions.visibilityOfElementLocated(username));
    }

    public void enterUsername(String user) {
        log.info("Entering username: {}", user);
        wait.until(driver -> {
            try {
                var element = driver.findElement(username);
                element.clear();
                element.sendKeys(user);
                return true;
            } catch (Exception e) {
                log.warn("Retrying username input due to DOM refresh");
                return false;
            }
        });
    }

    public void enterPassword(String pass) {
        log.info("Entering password (hidden)");
        wait.until(driver -> {
            try {
                var element = driver.findElement(password);
                element.clear();
                element.sendKeys(pass);
                return true;
            } catch (Exception e) {
                log.warn("Retrying password input due to DOM refresh");
                return false;
            }
        });
    }

    public void clickLogin() {
        log.info("Clicking Login button");
        for (int i = 0; i < 3; i++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(loginBtn)).click();
                return;
            } catch (Exception e) {
                log.warn("Stale element on login button, retrying {}", i + 1);
            }
        }
        throw new RuntimeException("Failed to click login button due to stale element");
    }

    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
        } catch (Exception e) {
            log.error("Login error message NOT found.");
            return null;
        }
    }

    public boolean isLoginButtonVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginBtn)).isDisplayed();
    }

    public void login(String user, String pass) {
        enterUsername(user);
        enterPassword(pass);
        clickLogin();
    }

    public int getRequiredFieldErrorCount() {
        return driver.findElements(requiredFieldErrors).size();
    }

    public boolean hasRequiredFieldErrors(int expectedErrors) {
        return getRequiredFieldErrorCount() == expectedErrors;
    }
}
