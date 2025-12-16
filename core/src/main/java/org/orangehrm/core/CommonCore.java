package org.orangehrm.core;


import static org.testng.Assert.fail;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.testng.ITestResult;


public class CommonCore extends UserConstants{
	public List<String> errorLogs;

    boolean overrideLink;
    
    public String screenshotsDirectory;

	public static String downloadDirectory;
    public WebDriver driver;
    public WebDriverWait wait;
    public static TestLog testLog;
    public TestEnvironment envTest;
    
    
    @SuppressWarnings("static-access")
	public CommonCore(TestLog tl, String screenshotsDirectory, String downloadDirectory, WebDriver driver)
    {
    	super(driver);
        testLog = tl;
        this.screenshotsDirectory = screenshotsDirectory;
        this.downloadDirectory = downloadDirectory;
        errorLogs = new ArrayList<String>();
        overrideLink = false;
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
        envTest = null;
    }

	public void addErrorLog(String errorLogText)
    {
    	errorLogs.add(errorLogText);
    }
    
    /**
     * Called by tearDown() of TestSuite class to report minor errors at the end of the test
     */
    public void printErrorLogs()
    {
    	if(!errorLogs.isEmpty())
    	{
    		System.out.println("\r\n\r\n========= ERROR LOGS =========\r\n");    	
    		System.out.println("There are " + errorLogs.size() + " errors.\r\n");
    	}
    	
    	for(int i=0; i<errorLogs.size(); i++)
    	{
    		System.out.println("[" + (i+1) + "] " + errorLogs.get(i) + "\r\n");
    	}
    	
    	if(!errorLogs.isEmpty())
    		fail("Please see above for error logs.");
    }
    
    

    public void setDriver(WebDriver driver)
    {
        this.driver = driver;
    }

    public WebDriver getDriver()
    {
        return driver;
    }


    /*-------------TestNG Section-------------*/

    /**
     * Get the status name for test result
     * @param status - int
     * @return status name
     */
    public String getTestNGStatusName(int status)
    {
        String statusName = "";

        if (status == 1)
            statusName = "SUCCESS";
        else if (status == 2)
            statusName = "FAILURE";
        else if (status == 3)
            statusName = "SKIP";
        else if (status == 4)
            statusName = "SUCCESS PERCENTAGE FAILURE";
        else if (status == 16)
            statusName = "STARTED";

        return statusName;
    }

    public void tearDown(ITestResult result)
    {
        System.out.println("\r\n\r\n");
        System.out.println("Class: " + result.getMethod().getTestClass().getName());
        System.out.println("Test: " + result.getMethod().getMethodName());
        System.out.println("Result: " + getTestNGStatusName(result.getStatus()));
        
        if (result.getStatus() == 2)
            System.out.println(result.getThrowable().getMessage());
        
        driver.quit();
    }
    
    public String getBrowserVersion(){
    	Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = cap.getBrowserName().toLowerCase();
        return browserName;
    }
    
    /* Methods to start */
    @SuppressWarnings("deprecation")
	public static boolean verifyURL(String linkUrl)
	{
    	boolean invalidURL = true;
        try 
        {
           URL url = new URL(linkUrl);
           
           HttpURLConnection httpURLConnect=(HttpURLConnection)url.openConnection();
           
           httpURLConnect.setConnectTimeout(3000);
           
           httpURLConnect.connect();
           
          if(httpURLConnect.getResponseCode()==HttpURLConnection.HTTP_NOT_FOUND)  
           {
        	  invalidURL = false;
            }
        } catch (Exception e) {
        	invalidURL =  false;
        }
        return invalidURL;
    } 
      
    public void waitForLoadPage() {
    	waitForLoadPage(20);
    }
    
    public void waitForLoadPage(int iTime) {
		 driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(iTime));
	     new WebDriverWait(driver, Duration.ofSeconds(iTime)).until((ExpectedCondition<Boolean>) wd ->
	                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
	}
    
  
    public void doAWait(long seconds)
    {
        // Get start time in milliseconds
        long startTime = System.currentTimeMillis();
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

        while (elapsedTime < seconds)
        {
            elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        }
    }
    
    
    public void scrollDownpage()
    {
    	JavascriptExecutor js = (JavascriptExecutor)driver;
    	js.executeScript("window.scrollTo(0,Math.max(document.documentElement.scrollHeight," 
    			+ "document.body.scrollHeight,document.documentElement.clientHeight));");
    }
    
}
