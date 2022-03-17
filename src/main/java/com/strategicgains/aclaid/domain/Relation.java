package com.strategicgains.aclaid.domain;

public class Relation
{
	private String name;
	private Union union;
	private Intersection intersection;
	private Exclusion exclusion;

	public Relation(String name)
	{
		super();
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
}
