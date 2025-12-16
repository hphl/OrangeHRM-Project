package org.orangehrm.core;

public class UserDetails 
{
	String username;
	String password;
	
	public void setUser(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	public String getUserName()
	{
		return username;
	}
	

	public String getUserPassword()
	{
		return password;
	}
	

}