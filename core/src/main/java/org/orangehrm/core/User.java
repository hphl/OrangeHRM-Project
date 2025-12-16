package org.orangehrm.core;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class User {
	//protected static WebDriver driver;
	
	public CommonCore commonCore;
    
	public String username, password;
	public String lastName, preferredName, firstName, fullName, businessUnit, jobTitle;
	public String email;
	public String role;
	
    
    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
	
    // IMPORTANT! Always call initializeUser from <Client>User class for new user before doing actions
    
    public void setEnvironment(WebDriver driver,
    		CommonCore commonCore)
    {
        this.commonCore = commonCore;
        
    }
	
	//--------------------------------------------
	
	 public void waitForLoadPage() {
	    	waitForLoadPage(30);
	 }
	    
	 public void waitForLoadPage(int iTime) {
		 commonCore.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(iTime));
	     new WebDriverWait(commonCore.driver, Duration.ofSeconds(iTime)).until((ExpectedCondition<Boolean>) wd ->
	                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
	}
	
	 public void doAWait(long seconds)
	    {
	        // Get start time in milliseconds
	        long startTime = System.currentTimeMillis();
	        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

	        while (elapsedTime < seconds)
	        {
	            // elapsed time in seconds = (current time in milliseconds - start
	            // time in milliseconds) / 1000
	            elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
	        }
	    } 
	 
	public WebDriver initializeDriver(String sBrowserName) 
	{
		WebDriver lDriver = null;
		
		switch (sBrowserName.toLowerCase()) 
		{
			case "chrome":
				ChromeOptions chromeOptions = new ChromeOptions();
				lDriver = new ChromeDriver(chromeOptions);
				break;
			case "edge":
				EdgeOptions edgeOptions = new EdgeOptions();
				lDriver = new EdgeDriver(edgeOptions);
				break;
			case "firefox":
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				lDriver = new FirefoxDriver(firefoxOptions);
				break;
			default:
				System.out.println("Browser not supported "+ sBrowserName);
		}
		lDriver.manage().window().maximize();
		return lDriver;
	}
	
	public void waitForElementPresent(WebElement locator, int timeout)  
	{
		WebDriverWait wait = new WebDriverWait(commonCore.driver, Duration.ofSeconds(timeout));
		int count = 0;
		int maxRetries  =3;
		boolean itemPresent = false;
		while (!itemPresent)
		{
			try 
			{
				itemPresent = true;
				wait.until(ExpectedConditions.visibilityOf(locator));
				
			}catch (Exception e) 
			{
				itemPresent = false;
				if (count == maxRetries) {
					itemPresent = true;
					System.out.println("Locator might not valid anymore: " + locator);
				}
				count ++;
				this.doAWait(4);
			}
		}
	}
	
	public WebElement waitForElementClickable(WebElement locator, int timeout) 
	{
		waitForElementPresent(locator, 10);
		WebDriverWait wait = new WebDriverWait(commonCore.driver, Duration.ofSeconds(timeout));
		WebElement wElement = wait.until(ExpectedConditions.elementToBeClickable(locator));
		return wElement;
	}
	
	public void click(WebElement locator, String fieldName) throws InterruptedException 
	{
		
		WebElement bElement = waitForElementClickable(locator, 10);
		try {
		    TestLogger.writeInfo("Click on \"" + fieldName + "\" element");
			bElement.click();
		}catch (Exception e) {
			TestLogger.writeInfo("WARNING: The click "+ fieldName + "is taking long time");
		}
	}
	
	public void doubleClick(WebElement locator, String fieldName)  
	{
		
		WebElement bElement = waitForElementClickable(locator, 10);
		Actions bAction = new Actions(commonCore.driver);
		try {
			TestLogger.writeInfo("Double click on \"" + fieldName + "\" element");
			bAction.doubleClick(bElement).perform();
			
		}catch (Exception e) {
			TestLogger.writeInfo("WARNING: The double click "+ fieldName + "is taking long time");
		}
	}
	
	public void rightClick(WebElement locator, String fieldName)  
	{
		
		WebElement bElement = waitForElementClickable(locator, 10);
		Actions bAction = new Actions(commonCore.driver);
		try {
			TestLogger.writeInfo("Right click on \"" + fieldName + "\" element");
			bAction.contextClick(bElement).perform();
			
		}catch (Exception e) {
			TestLogger.writeInfo("WARNING: The right click "+ fieldName + "is taking long time");
		}
	}
	
	public String getTextFromElementFound( WebElement locator, String sFieldName) 
	{
		String text = "";
		waitForElementPresent(locator, 10);
		
		try {
			text = locator.getText();
		}catch (Exception e)
		{
			if(text.isEmpty()){
				try {
					text = locator.getAttribute("value");
				}catch(Exception e1) {
					TestLogger.writeInfo("WARNING: The "+ sFieldName + "is taking long time to get text");
				}
			}
		}
		
		return text;
	}
	
	public void check(WebElement locator, String fieldName) 
	{
		WebElement bCheckbox = waitForElementClickable(locator, 10);
		try 
		{
			if (!bCheckbox.isSelected()) 
			{
				bCheckbox.click();
			}else 
			{
				TestLogger.writeInfo("WARNING: The checkbox "+ fieldName + "is already checked");
			}
		}catch(Exception e) 
		{
			TestLogger.writeInfo("WARNING: The checkbox "+ fieldName + "is taking long time to check");
		}
	}
	
	 /**
     * Wait for a page element to be visible
     * @param locator Page element locator (XPath)
     * @param timeout Maximum wait time
     */
    public void waitForVisible(WebElement locator, int timeout)
    {
    	try{
    		WebDriverWait wait = new WebDriverWait(commonCore.driver, Duration.ofSeconds(timeout));
    		wait.until(ExpectedConditions.visibilityOf(locator));
    	}catch (Exception e)
    	{
    		TestLogger.writeFailure("Web Element is not present");
    	}
    }
    
	
}
