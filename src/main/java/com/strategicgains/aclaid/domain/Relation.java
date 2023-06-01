package com.strategicgains.aclaid.domain;

public class Relation
{
	private String name;
	private UserSet rewrites;

	public Relation(String name)
	{
		super();
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String toString()
	{
		return String.format("Relation: %s", name);
	}

	public void setRewrite(UserSet userset)
	{
		this.rewrites = userset;
	}

	public boolean hasRewrites()
	{
		return (rewrites != null);
	}
}
