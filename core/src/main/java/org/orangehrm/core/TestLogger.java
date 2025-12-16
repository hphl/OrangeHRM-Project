package org.orangehrm.core;


import org.testng.Reporter;
import java.util.logging.Logger;

public class TestLogger {

private static Logger Log = Logger.getLogger(TestLogger.class.getName());//


// This is to print log for the beginning of the test case, as we usually run so many test cases as a test suite

public static void startTestCase(String sTestCaseName){

	Log.info("----------------------------------------------------------------------------------------");

	Log.info("-------------------------                 " + sTestCaseName + "       -------------------------");

	Log.info("----------------------------------------------------------------------------------------");

}

//This is to print log for the ending of the test case

public static void endTestCase(String sTestCaseName){

	Log.info("-------------------------            "+"----END----"+"             -------------------------");

	Log.info("-");

	Log.info("-");

	Log.info("-");

	}

public static void writeInfo(String message) {

		Log.info("\t" + message);
		System.out.println("\t\t" + message);

		}

public static void writeStep(String message) {

	System.out.println("\n**STEP: " + message);
	Reporter.log("**STEP: " + message);
	}

public static void writeFailure(String message) {
	Log.info("xxFAILURE: " + message);
	System.out.println("xxFAILURE: " + message);
	}


}
