package org.orangehrm.tests.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.orangehrm.tests.utils.Constants.*;

public class DriverFactory {

    public static WebDriver createInstance(String browserName) {

        browserName = browserName.toLowerCase();

        return switch (browserName) {
            case BROWSER_CHROME -> new ChromeDriver();
            case BROWSER_FIREFOX -> new FirefoxDriver();
            case BROWSER_EDGE -> new EdgeDriver();
            default -> throw new IllegalArgumentException(ERROR_SUPPORTED_BROWSER + browserName);
        };
    }
}
