package org.orangehrm.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
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
        System.out.println("Current URL: " + driver.getCurrentUrl());
        wait.until(ExpectedConditions.visibilityOfElementLocated(username)).sendKeys(user);
    }

    public void enterPassword(String pass) {
        log.info("Entering password (hidden)");
        wait.until(ExpectedConditions.visibilityOfElementLocated(password)).sendKeys(pass);
    }

    public void clickLogin() {
        log.info("Clicking Login button");
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn)).click();
    }

    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
        } catch (Exception e) {
            log.error("Login error message NOT found.");
            return null;
        }
    }

    public boolean isErrorMessagePresent() {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
            boolean present = driver.findElements(errorMessage).size() > 0;
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            return present;
        } catch (Exception e) {
            return false;
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
}
