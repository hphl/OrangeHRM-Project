package org.orangehrm.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.orangehrm.core.logging.LoggerFactory;

import java.time.Duration;

public class PimPage {

    private final WebDriverWait wait;
    private static final Logger log = LoggerFactory.getLogger(PimPage.class);

    private final By pimMenu = By.xpath("//span[text()='PIM']");
    private final By addEmployeeBtn = By.xpath("//a[text()='Add Employee']");

    public PimPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openAddEmployee() {
        log.info("Navigating to PIM > Add Employee");
        wait.until(ExpectedConditions.elementToBeClickable(pimMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(addEmployeeBtn)).click();
    }
}
