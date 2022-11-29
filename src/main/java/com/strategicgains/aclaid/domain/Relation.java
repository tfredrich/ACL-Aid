package com.strategicgains.aclaid.domain;

import java.util.List;

public class Relation
{
	private String name;
	private List<UsersetRewriteRule> usersetRewriteRules;

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
}
