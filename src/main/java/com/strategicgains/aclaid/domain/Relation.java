package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.policy.Policy;

public class Relation
{
	private String name;
	private Union union;
	private Intersection intersection;
	private Exclusion exclusion;

	private Policy policy;

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

	public Union getUnion()
	{
		return union;
	}

	public void setUnion(Union union)
	{
		this.union = union;
	}

	public Intersection getIntersection()
	{
		return intersection;
	}

	public void setIntersection(Intersection intersection)
	{
		this.intersection = intersection;
	}

	public Exclusion getExclusion()
	{
		return exclusion;
	}

	public void setExclusion(Exclusion exclusion)
	{
		this.exclusion = exclusion;
	}

	public Policy getPolicy()
	{
		return policy;
	}

	public void setPolicy(Policy policy)
	{
		this.policy = policy;
	}
}
