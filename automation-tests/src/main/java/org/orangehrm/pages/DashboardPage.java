package org.orangehrm.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.orangehrm.core.logging.LoggerFactory;

import java.time.Duration;

public class DashboardPage {

    //private final WebDriver driver; // Remove code if is not used in the future
    private final WebDriverWait wait;
    private final By dashboardHeader = By.xpath("//h6[text()='Dashboard']");
    private static final Logger log = LoggerFactory.getLogger(DashboardPage.class);

    public DashboardPage(WebDriver driver) {
        //this.driver = driver; // Remove code if is not used in the future
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isDashboardDisplayed() {
        log.info("Checking if Dashboard is displayed");
        try {
            return wait
                    .until(ExpectedConditions.visibilityOfElementLocated(dashboardHeader))
                    .isDisplayed();
        } catch(Exception exception) {
            log.warn("Dashboard is NOT displayed");
            return false;
        }
    }
}
