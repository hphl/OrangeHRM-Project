package org.orangehrm.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class WaitUtils {
    public static final By LOADER = By.cssSelector(".oxd-loading-spinner");

    public static void waitForLoaderToDisappear(WebDriver driver, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        wait.until(d -> driver.findElements(LOADER).isEmpty());
    }
}
