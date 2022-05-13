package com.strategicgains.aclaid.builder;

import com.strategicgains.aclaid.domain.Relation;

public class RelationBuilder
extends AbstractChildBuildable
{
	private String name;
	private UnionBuilder union;
	private IntersectionBuilder intersection;
	private ExclusionBuilder exclusion;
	private PolicyBuilder policy;

	public RelationBuilder(String relation, NamespaceConfigurationBuilder parent)
	{
		super(parent);
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

	public PolicyBuilder policy()
	{
		this.policy = new PolicyBuilder(this);
		return policy;
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
