package com.strategicgains.aclaid;

public class User
implements Role
{
	private String id;

	public User(String id)
	{
		super();
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	@Override
	public String getRoleId()
	{
		return "guest";
	}
}