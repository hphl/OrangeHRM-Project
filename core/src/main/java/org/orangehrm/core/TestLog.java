package org.orangehrm.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.io.MultiOutputStream;


public class TestLog
{

    MultiOutputStream multiOut = null, multiErr = null;
    String outFileName, errFileName;
    List<String> logFiles = new ArrayList<String>();
    List<String> screenShots = new ArrayList<String>();

    String logsDirectory;
    String testClassName;

    public TestLog(String logsDirectory)
    {
        this.logsDirectory = logsDirectory;
        this.testClassName = "";
    }

    public TestLog(String logsDirectory, String testClassName)
    {
        this.logsDirectory = logsDirectory;
        this.testClassName = testClassName;
    }

    public List<String> getFileNames()
    {
        // Verify each .txt file
        for (int i = 0; i < logFiles.size(); i++)
        {
            try
            {
                FileReader input = new FileReader(logFiles.get(i));
                BufferedReader buffer = new BufferedReader(input);
                String line = buffer.readLine();

                // If .txt file is empty, delete it, remove its name from
                // fileNames list and update fileNames list
                if (line == null)
                {
                    (new File(logFiles.get(i))).deleteOnExit();
                    logFiles.remove(i);
                }

                buffer.close();
            }
            catch (Exception e)
            {
            }
        }

        // List of files : logs and screenshots
        List<String> files = new ArrayList<String>();

        for (int i = 0; i < logFiles.size(); i++)
            files.add(logFiles.get(i));

        for (int i = 0; i < screenShots.size(); i++)
            files.add(screenShots.get(i));

        return files;

    }

    public void addScreenShot(String fileName)
    {
        screenShots.add(fileName);
    }

    public void consolePrintsToTxt()
    {
        try
        {
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-dd hh-mm-ss-SSS a", Locale.US);
        	String sDate = format.format(new Date()); // "now"

        	
            outFileName = logsDirectory + "\\LOG "
                    + sDate + " "
                    + testClassName + ".txt";
            // errFileName = pathWithBackslash + "ERROR " +
            // TestDate.getDateAndTimeForLog() + ".txt";
            logFiles.add(outFileName);
            // logFiles.add(errFileName);

            // Set output file stream
            FileOutputStream fout = new FileOutputStream(outFileName);
            // FileOutputStream ferr = new FileOutputStream(errFileName);

            // Multiple output stream : console and .txt file
            multiOut = new MultiOutputStream(System.out, fout);
            multiErr = new MultiOutputStream(System.err, fout);

            // Output stream
            PrintStream stdout = new PrintStream(multiOut);
            PrintStream stderr = new PrintStream(multiErr);

            // Set system prints to multiple output stream
            System.setOut(stdout);
            System.setErr(stderr);
        }
        catch (IOException e)
        {
            // Could not create/open the file
        }
    }

    public String getOutputString()
    {
        return multiOut.toString() + multiErr.toString();
    }

    public void close() throws Exception
    {
        multiOut.close();
        multiErr.close();
    }

}
