package org.orangehrm.tests;

import io.qameta.allure.Attachment;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.orangehrm.core.logging.LoggerFactory;
import org.orangehrm.core.utils.ScreenshotUtil;
import org.orangehrm.tests.utils.DriverFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;


public class BaseTest {

    protected WebDriver driver;
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected String browserName;

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setup(Method method, String browser) {
        this.browserName = browser;
        log.info(
                "=== STARTING TEST: {}.{} (browser={}, thread={}) ===",
                method.getDeclaringClass().getSimpleName(),
                method.getName(),
                browserName,
                Thread.currentThread().getId()
        );

        System.setProperty("webdriver.http.factory", "jdk-http-client");

        driver = DriverFactory.createInstance(browserName);
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void teardown(ITestResult result, Method method) {
        String status = switch (result.getStatus()) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP -> "SKIPPED";
            default -> "UNKNOWN";
        };

        log.info(
                "=== FINISHED TEST: {}.{} -> {} (browser={}, thread={}) ===",
                method.getDeclaringClass().getSimpleName(),
                method.getName(),
                status,
                browserName,
                Thread.currentThread().getId()
        );

        if (result.getStatus() == ITestResult.FAILURE) {
            String path = ScreenshotUtil.takeScreenshot(driver, "screenshots/", method.getName(), browserName);
            log.info("Screenshot saved at: {}", path);
            attachScreenshotToAllure("Screenshot on Failure");
            log.info("Attached screenshot to Allure.");
        }

        if (driver != null) {
            driver.quit();
        }
    }

    @Attachment(value = "{0}", type = "image/png")
    public byte[] attachScreenshotToAllure(String name) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
