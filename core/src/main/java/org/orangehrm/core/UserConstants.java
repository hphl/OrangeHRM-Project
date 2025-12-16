package org.orangehrm.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class UserConstants {
	protected WebDriver driver;
		// Prevents to instantiate another Constants object
	protected UserConstants(WebDriver driver){
		this.driver = driver;
	    PageFactory.initElements(driver, this);
	}
	
	
	private static final String ROOT_DIRECTORY = "C:\\QA_Automation\\";
	
	public static final String LOGS_DIRECTORY = ROOT_DIRECTORY + "tests_results\\logs\\";
	public static final String SCREENSHOTS_DIRECTORY = ROOT_DIRECTORY + "tests_results\\screenshots\\";
	public static final String DOWNLOAD_FILES_DIRECTORY = ROOT_DIRECTORY + "download\\";
	public static final String TESTNG_REPORT_DIRECTORY = ROOT_DIRECTORY + "testng_output";
	public static final String EXCEL_TESTS_TO_RUN_FILE_NAME = "tests_to_run.xls";
	
	public static final String PROJECT_ROOT_FOLDER_NAME = "eCommerce";
	
	
	
		
}
