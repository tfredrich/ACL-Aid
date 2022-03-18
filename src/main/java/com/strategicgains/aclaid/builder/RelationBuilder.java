package com.strategicgains.aclaid.builder;

import com.strategicgains.aclaid.domain.Relation;

public class RelationBuilder
extends AbstractChildBuildable
{
	private String name;
	private UnionBuilder union;
	private IntersectionBuilder intersection;
	private ExclusionBuilder exclusion;

	public RelationBuilder(String relation, NamespaceConfigurationBuilder aclBuilder)
	{
		super(aclBuilder);
		this.name = relation;
	}

	public String getName()
	{
		return name;
	}

	Relation build()
	{
		return new Relation(name);
	}

	public UnionBuilder union()
	{
		union = new UnionBuilder(this);
		return union;
	}

	public IntersectionBuilder intersection()
	{
		intersection = new IntersectionBuilder(this);
		return intersection;
	}

	public ExclusionBuilder exclusion()
	{
		exclusion = new ExclusionBuilder(this);
		return exclusion;
	}
}
