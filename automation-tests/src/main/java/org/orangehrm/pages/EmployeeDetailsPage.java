package org.orangehrm.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.orangehrm.core.logging.LoggerFactory;
import org.orangehrm.utils.WaitUtils;

import java.time.Duration;

public class EmployeeDetailsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger log = LoggerFactory.getLogger(EmployeeDetailsPage.class);

    private final By employeeNameHeader =
            By.xpath("//div[contains(@class,'orangehrm-edit-employee-name')]//h6");

    public EmployeeDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isEmployeeCreated() {
        log.info("Validating employee creation");

        WaitUtils.waitForLoaderToDisappear(driver, Duration.ofSeconds(30));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(employeeNameHeader)).isDisplayed();
    }

    public String getEmployeeName() {
        log.info("Validating employee name");
        WaitUtils.waitForLoaderToDisappear(driver, Duration.ofSeconds(30));
        return wait.until(driver -> {
            String text = driver.findElement(employeeNameHeader).getText();
            return (text != null && !text.isBlank()) ? text : null;
        }).trim();
    }
}
