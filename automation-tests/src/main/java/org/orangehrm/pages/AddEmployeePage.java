package org.orangehrm.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.orangehrm.core.logging.LoggerFactory;

import java.time.Duration;
import java.util.List;

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
    private final By requiredErrors = By.cssSelector(".oxd-input-field-error-message");

    public AddEmployeePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillEmployee(String first, String last, String id) {
        log.info("Filling employee data: {} {}", first, last);

        wait.until(ExpectedConditions.visibilityOfElementLocated(firstName)).sendKeys(first);
        wait.until(ExpectedConditions.visibilityOfElementLocated(lastName)).sendKeys(last);

        WebElement idInput = wait.until(ExpectedConditions.elementToBeClickable(employeeId));
        clearInput(idInput);
        idInput.sendKeys(id);
    }

    private void clearInput(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value=''; arguments[0].dispatchEvent(new Event('input'));", element);
    }

    public void clickSave() {
        log.info("Clicking Save button");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
    }

    public int getRequiredFieldErrorCount() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(requiredErrors));
        } catch (TimeoutException e) {
            log.info("No validation messages rendered");
            return 0;
        }

        List<WebElement> errors = driver.findElements(requiredErrors)
                .stream()
                .filter(e -> "Required".equalsIgnoreCase(e.getText().trim()))
                .toList();

        log.info("Required field errors found: {}", errors.size());
        return errors.size();
    }

    public boolean isRequiredNotFound() {

        boolean notFound = driver.findElements(By.cssSelector(".oxd-input-field-error-message")).isEmpty();
        log.info("Required field not found: {}", notFound);
        return notFound;
    }
}