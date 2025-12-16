package org.orangehrm.core;

import java.lang.reflect.Method;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;


public class TestSuite
{	
    public CommonCore commonCore;
    public WebDriver driver;

    public String testDate;
    
    public Excel excel;
    public String serverType, envName, browser, browVersion;
    String projectRootFolderName, excelTestsToRunFileName, testClassName;
    
    /**
     * Constructor that setups specific characteristics for an Automation project
     * @param customizedProjectRootFolderName
     * @param customizedExcelTestsToRunFileName
     * @param customizedTestClassName, class name from specific Automation project
     * 
     */
    public TestSuite(String customizedProjectRootFolderName, String customizedExcelTestsToRunFileName, String customizedTestClassName) 
    		throws Exception
    {
    	projectRootFolderName = customizedProjectRootFolderName;
    	excelTestsToRunFileName = customizedExcelTestsToRunFileName;
    	testClassName = customizedTestClassName;
    	
    }

    /**
     * gets Information From Excel For GeneralUser
     * @param workingPath
     * @return details information of general user read from Excel
     * 
     */
    public UserDetails getInformationFromExcelForGeneralUser(String workingPath)
            throws Exception
    {
    	UserDetails details = null;
    	
    	if (excel != null)
    	{
	        excel.startRun();
	        details = excel.readGeneralUserSheet("General User");
    	}
    	else
    	{
    		System.out.println("Usage error: Excel object has not been initialized before using method getInformationFromExcelForGeneralUser");
    	}
    	
        return details;
    }
    
  
    public void setUpClass(String server, String environment, String browserName, String browserVersion) 
    		throws Exception
    {
    	serverType = server;
        envName = environment;
        browser = browserName;
        browVersion = browserVersion;  
        
        String workingDirectoryPath = System.getProperty("user.dir");       
        if (!workingDirectoryPath.endsWith(projectRootFolderName))
            workingDirectoryPath += "\\" + projectRootFolderName;      
        String excelFileFullPath = workingDirectoryPath + "\\"
                + excelTestsToRunFileName;
        excel = new Excel(excelFileFullPath,
        		projectRootFolderName,
        		testClassName);
        excel.startRun();
    }  
    
    /**
     * gets Information From Excel For GeneralUser
     * @param method, reference to know source of test for setup
     * @param automationEnv, required for automaton creation for specific project
     * example: super.setUp(method, automaton.env.lmco_url);
     */      
    public void setUp(Method method, String url) throws Exception
    {
        System.out.println("\r\n\r\n========================================");
        System.out.println(this.getClass().getName() + " \r\n"
                + method.getName() + "\r\n");
        System.out.println("========================================");
        start(serverType, browser, browVersion, url, excel.downloadDirectory);
        commonCore = new CommonCore(excel.testLog, excel.screenshotsDirectory, excel.downloadDirectory,driver);
        
    }      
	
    @AfterMethod
    public void tearDown(ITestResult result) throws Exception
    {
    	commonCore.printErrorLogs();
    	commonCore.tearDown(result);
        
    }

    @AfterClass
    public void end() throws Exception
    {
        excel.endRun();
    }
    

    /**
     * Called by test class to start Selenium
     * @param serverType Local machine, Browserstack or SauceLabs
     * @param browser Firefox, Chrome or Edge
     * @param browserVersion Defined when the automation runs on the cloud
     * @param testURL Client URL
     * @throws Exception
     */
    public void start(String serverType, String browser, String browserVersion, String testURL, String downloadDirectory)
    	throws Exception
    {
    	if(serverType.toLowerCase().contains("local")) 
    	{
    		startBrowser(browser, testURL, downloadDirectory);
    	}
    }
    
    /**
     * Set up and open the browser on local machine
     * @param browserName
     * @param testURL
     */
    public void startBrowser(String sBrowserName, String testURL, String downloadDirectory)
    {	
		switch (sBrowserName.toLowerCase()) 
		{
			case "chrome":
				ChromeOptions chromeOptions = new ChromeOptions();
				driver = new ChromeDriver(chromeOptions);
				break;
			case "edge":
				EdgeOptions edgeOptions = new EdgeOptions();
				driver = new EdgeDriver(edgeOptions);
				break;
			case "firefox":
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				driver = new FirefoxDriver(firefoxOptions);
				break;
			default:
				System.out.println("Browser not supported "+ sBrowserName);
		}
		driver.manage().window().maximize();
		
        driver.get(testURL);
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        // Maximize browser window
        driver.manage().window().maximize();

        if (browser.contains("firefox"))
            System.out.println("Firefox browser opened.\r\n");
        else if (browser.contains("chrome"))
            System.out.println("Chrome browser opened.\r\n");
        else if (browser.contains("edge"))
            System.out.println("Edge browser opened.\r\n");
        
    }  
}