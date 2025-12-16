package org.orangehrm.core;


import static org.orangehrm.core.UserConstants.DOWNLOAD_FILES_DIRECTORY;
import static org.orangehrm.core.UserConstants.LOGS_DIRECTORY;
import static org.orangehrm.core.UserConstants.SCREENSHOTS_DIRECTORY;
import static org.orangehrm.core.UserConstants.TESTNG_REPORT_DIRECTORY;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.IOException;


public class Excel
{
    public TestLog testLog;

    public String logsDirectory, screenshotsDirectory, downloadDirectory;

    String testClassName;
    
    String server;
    List<String> environments;
    List<TestBrowser> browsers;
    List<String> tests;
    List<String> emailAddresses;

    FileInputStream inputWorkbook;
    Workbook w;
    
    
    public Excel(String fileName) 
    {
    	
    	try {
    	    inputWorkbook = new FileInputStream(fileName);
			w = WorkbookFactory.create(inputWorkbook);
		} catch (Exception e) {
			e.printStackTrace();
		} 
    }
 
    public Excel(String fileName, String resultsFolderName, String testClassName) throws IOException
    {
        this.logsDirectory = LOGS_DIRECTORY + resultsFolderName;
        this.screenshotsDirectory = SCREENSHOTS_DIRECTORY + resultsFolderName;
        this.downloadDirectory = DOWNLOAD_FILES_DIRECTORY + resultsFolderName;
                
        this.testClassName = testClassName;
        inputWorkbook =  new FileInputStream(fileName);
        
        w = WorkbookFactory.create(inputWorkbook);
                
        emailAddresses = new ArrayList<String>();
    }
    
    /**
     * It runs by Excel sheet.
     * Start reading Server, Environment, Browser and Tests sheets and run tests programmatically with TestNG
     * @throws Exception
     */
    // Read .xls file, execute tests that have no "X" and send results by email
    public void run() throws Exception
    {
    	readServerSheet();
        readEnvironmentSheet();
        readBrowsersSheet();
        readTestsSheet();
        executeTests();
    }

    /**
     * Called from test class.
     * Start writing logs and reading Email sheet.
     * @throws Exception
     */
    public void startRun() throws Exception
    {
        createDirOnNotExist(logsDirectory);
        createDirOnNotExist(screenshotsDirectory);
        createDirOnNotExist(downloadDirectory);
        
        testLog = new TestLog(logsDirectory, testClassName);
        testLog.consolePrintsToTxt();

        readEmailAddressSheet();
    }

    /**
     * Send email with results if email address is set in Email sheet and close logs streams
     * @throws Exception
     */
    public void endRun() throws Exception
    {
        if (!emailAddresses.isEmpty())
            sendResultsByEmail();

        testLog.close();
    }

    // Create directory if the directory defined in Excel file doesn't exist
    public void createDirOnNotExist(String path)
    {
    	
        if (!new File(path).exists())
        {
            System.out.println("Directory doesn't exist. Current path : " + path);
            System.out.println("Creating directory : " + path);
            boolean result = new File(path).mkdirs();

            if (result)
                System.out.println("Directory created\r\n");
            else
                System.out.println("Directory creation failed\r\n");
        }
    }
    
    public void readEmailAddressSheet() throws Exception
    {
    	int lastColumnNum = 0;
        try
        {
           // w = WorkbookFactory.create(inputWorkbook);
            // Get sheet with the title "Set directory path"
            Sheet sheet = w.getSheet("Email");
            
            Row firstRow = sheet.getRow(0);
            if (firstRow != null) {
                // getLastCellNum() returns the count (1-based)
                lastColumnNum = firstRow.getLastCellNum();
            } else {
                System.out.println("First row is empty or null.");
            }
            
            for (int i = 1; i < sheet.getLastRowNum(); i++)
            {
                for (int j = 0; j < lastColumnNum; j++)
                {
                    // First cell of each row
                    Cell cell = sheet.getRow(i).getCell(j);
                    String cellContent = cell.getStringCellValue();

                    if (!cellContent.isEmpty())
                    {
                        emailAddresses.add(cellContent);
                        break;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void readServerSheet() throws Exception
    {
        try
        {
           // w = WorkbookFactory.create(inputWorkbook);
            Sheet sheet = w.getSheet("Server");

            System.out.println("\r\nReading \"Server\" sheet... \r\n");
            System.out.println("Num of Rows "+sheet.getLastRowNum());
            for (int i = 1; i <= sheet.getLastRowNum() ; i++)
            {
                // First cell of each row
                Cell firstCell = sheet.getRow(i).getCell(0);
                String firstCellContent = firstCell.getStringCellValue();
                
                // Second cell of each row : Type of server
                Cell serverCell = sheet.getRow(i).getCell(1);
                String serverType = serverCell.getStringCellValue();

                System.out.println("[ " + firstCellContent + " ] " + serverType
                        + "\r\n");

                if (firstCellContent.isEmpty())
                {
                    server = serverType;
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void readEnvironmentSheet() throws Exception
    {
        environments = new ArrayList<String>();

        try
        {
           // w = WorkbookFactory.create(inputWorkbook);
            
            Sheet sheet = w.getSheet("Environment");

            System.out.println("\r\nReading \"Environment\" sheet... \r\n");

            System.out.println ();
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++)
            {
                // First cell of each row
                Cell firstCell = sheet.getRow(i).getCell(0);
                String firstCellContent = firstCell.getStringCellValue();

                // Second cell of each row : Type of environment
                Cell envCell = sheet.getRow(i).getCell(1);
                String env = envCell.getStringCellValue();

                System.out.println("[ " + firstCellContent + " ] " + env
                        + "\r\n");

                if (firstCellContent.isEmpty())
                {
                	 System.out.println ();
                    environments.add(env);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void readBrowsersSheet() throws Exception
    {
        browsers = new ArrayList<TestBrowser>();

        try
        {  
            Sheet sheet = w.getSheet("Browsers");

            System.out.println("\r\nReading \"Browsers\" sheet... \r\n");

            for (int i = 1; i <= sheet.getLastRowNum(); i++)
            {
                // First cell of each row
                Cell firstCell = sheet.getRow(i).getCell(0);
                String firstCellContent = firstCell.getStringCellValue();

                // Second cell of each row : Browser name
                Cell browserTypeCell = sheet.getRow(i).getCell(1);
                String browserType = browserTypeCell.getStringCellValue();
                
                // Second cell of each row : Browser version
                Cell browserVersionCell = sheet.getRow(i).getCell(2);
                String browserVersion = browserVersionCell.getStringCellValue();

                System.out.println("[ " + firstCellContent + " ] "
                        + browserType + " " + browserVersion + "\r\n");

                if (firstCellContent.isEmpty())
                {
                    browsers.add(new TestBrowser(browserType, browserVersion));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void readTestsSheet() throws Exception
    {
        // Initialize "tests" list to add items
        tests = new ArrayList<String>();

        try
        {
            Sheet sheet = w.getSheet("Tests");
            for (int i = 0; i <= sheet.getLastRowNum(); i++)
            {
                // First cell of each row
                Cell cell = sheet.getRow(i).getCell(0);
                String cellContent = cell.getStringCellValue();

                // Add test class name to "tests" list
                if (cellContent.startsWith("com"))
                {
                	System.out.println("Test to add:  "+cellContent);
                    tests.add(cellContent);
                }
             // If there is not an X next to a test, add test name to "tests"
                // list
                else if (cellContent.isEmpty()/*
                                               * !cellContent.equalsIgnoreCase("x"
                                               * )
                                               */)
                {
                    tests.add(sheet.getRow(i).getCell(1).getStringCellValue());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public UserDetails readGeneralUserSheet(String sheetName)
            throws Exception
    {
    	UserDetails details = new UserDetails();

    	
    	int lastColumnNum = 0;
        try
        {
            Sheet sheet = w.getSheet("Email");
            
            Row firstRow = sheet.getRow(0);
            if (firstRow != null) {
                // getLastCellNum() returns the count (1-based)
                lastColumnNum = firstRow.getLastCellNum();
            } else {
                System.out.println("First row is empty or null.");
            }
            



            // Username at first cell of second row
            Cell userCell = sheet.getRow(1).getCell(0);
            String username = userCell.getStringCellValue();

            // Password at second cell of second row
            String password = "";

            if (lastColumnNum > 1)
            {
                Cell passwordCell = sheet.getRow(1).getCell(1);
                password = passwordCell.getStringCellValue();
            }

            // Set Contact Us details
            details.setUser(username, password);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return details;
    }

    public void executeTests()
    {
        for (int e = 0; e < environments.size(); e++)
        {
            String envName = environments.get(e);

            // Go through browsers to test
            for (int b = 0; b < browsers.size(); b++)
            {
                TestBrowser browser = browsers.get(b);
                runByTestNG(envName, browser, tests);
            }
        }
    }
    
    private void runByTestNG(String envName, TestBrowser browser,
            List<String> tests)
    {
    	
        List<XmlClass> myClasses = new ArrayList<XmlClass>();
        List<XmlInclude> includedMethods = new ArrayList<XmlInclude>();
        XmlClass testClass = null;

        // Go through "tests" list
        for (int i = 0; i < tests.size(); i++)
        {
            if (tests.get(i).startsWith("com"))
            {
                if (testClass != null)
                {
                    if (!includedMethods.isEmpty())
                    {
                        testClass.setIncludedMethods(new ArrayList<XmlInclude>(
                                includedMethods));
                        myClasses.add(testClass);
                        includedMethods.clear();
                    }

                    testClass = null;
                }

                if ((i + 1) < tests.size())
                {
                    if (tests.get(i + 1).startsWith("Test"))
                    {   
                        testClass = new XmlClass(tests.get(i));
                    }
                }
            }
            // Else run test from current "className"
            else if (tests.get(i).startsWith("Test"))
            {
                XmlInclude methodIncluded = new XmlInclude(tests.get(i));
                includedMethods.add(methodIncluded);
            }
        }

        if (testClass != null)
        {
            if (!includedMethods.isEmpty())
            {
                testClass.setIncludedMethods(new ArrayList<XmlInclude>(
                        includedMethods));
                myClasses.add(testClass);
                includedMethods.clear();
            }

            testClass = null;
        }

        Map<String, String> testngParams = new HashMap<String, String>();
        testngParams.put("server", server);
        testngParams.put("environment", envName);
        testngParams.put("browserName", browser.name);
        testngParams.put("browserVersion", browser.version);
        
        setUpTestNGAndRun(testngParams, myClasses);        
    }
    
    
    
    private void setUpTestNGAndRun(Map<String, String> testngParams,
            List<XmlClass> myClasses)
    {
    
        // Create an instance of XML Suite and assign a name for it.
        XmlSuite mySuite = new XmlSuite();
        mySuite.setName("ExcelSuite");

        // Create an instance of XmlTest and assign a name for it.
        XmlTest myTest = new XmlTest(mySuite);
        myTest.setName("ExcelTestCases");
 //UPDATE this      // myTest.setPreserveOrder("true");
        myTest.setParameters(testngParams);

        // Assign that to the XmlTest Object created earlier.
        myTest.setXmlClasses(myClasses);

        // Create a list of XmlTests and add the Xmltest you created earlier to
        // it.
        List<XmlTest> myTests = new ArrayList<XmlTest>();
        myTests.add(myTest);

        // Add the list of tests to your Suite.
        mySuite.setTests(myTests);

        // Add the suite to the list of suites.
        List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
        mySuites.add(mySuite);

        // Create an instance on TestNG
        TestNG myTestNG = new TestNG();
        myTestNG.setPreserveOrder(true);
        
        System.out.println("Output: "+TESTNG_REPORT_DIRECTORY);
        myTestNG.setOutputDirectory(TESTNG_REPORT_DIRECTORY);

        // Set the list of Suites to the testNG object you created earlier.
        myTestNG.setXmlSuites(mySuites);

        // Invoke run() - this will run your class.
        myTestNG.run();
    }

    public void sendResultsByEmail() throws Exception
    {
        // Set email with message and attachment(s)
        TestEmail mail = new TestEmail(emailAddresses, testLog.getOutputString(),
                testLog.getFileNames());
        // Send email
        mail.sendEmail();
    }
}