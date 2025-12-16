package org.orangehrm.core;
import static org.orangehrm.core.TestURLs.*;

public class TestEnvironment {
	public String environmentName;
    
    public TestEnvironment(String environmentName)
    {
    	this.environmentName = environmentName;
    }
    
    public String getUrl()
    {
    	String sUATUrl = "";
    	
    	if (environmentName.equals("QA")) 
    	{
    		
    	}else if (environmentName.equals("UAT")){
    		sUATUrl = UAT_TEST_URL;
    	}
    	return sUATUrl;
    }
    public void setDBVariables()
    {
    	    
    }
}
