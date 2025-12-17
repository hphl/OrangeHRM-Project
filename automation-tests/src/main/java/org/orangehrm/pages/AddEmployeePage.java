package org.orangehrm.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.orangehrm.core.logging.LoggerFactory;

import java.time.Duration;

public class AddEmployeePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger log = LoggerFactory.getLogger(AddEmployeePage.class);

    private final By firstName = By.name("firstName");
    private final By lastName = By.name("lastName");
    private final By employeeId = By.xpath(
            "//label[normalize-space()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input"
    );
    private final By saveBtn = By.xpath("//button[@type='submit']");
    private final By formLoader = By.cssSelector(".oxd-form-loader");

    public AddEmployeePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void createEmployee(String first, String last, String id) {
        log.info("Creating employee: {} {}", first, last);
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstName)).sendKeys(first);
        wait.until(ExpectedConditions.visibilityOfElementLocated(lastName)).sendKeys(last);
        WebElement idInput = wait.until(ExpectedConditions.elementToBeClickable(employeeId));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value=''; arguments[0].dispatchEvent(new Event('input'));", idInput);
        idInput.sendKeys(id);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
        wait.until(ExpectedConditions.urlContains("/pim/viewPersonalDetails"));
        log.info("Finished creating new employee: {} {}", first, last);
    }
}