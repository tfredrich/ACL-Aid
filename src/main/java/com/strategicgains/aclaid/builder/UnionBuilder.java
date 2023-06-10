package com.strategicgains.aclaid.builder;

import com.strategicgains.aclaid.domain.rewrite.ComputedUserSet;
import com.strategicgains.aclaid.domain.rewrite.This;
import com.strategicgains.aclaid.domain.rewrite.Union;

public class UnionBuilder
extends AbstractChildBuildable<RelationBuilder>
{
	private Union union;

	protected UnionBuilder(RelationBuilder parent, Union union)
	{
		super(parent);
		this.union = union;
	}

	public UnionBuilder _this()
	{
		union.child(new This());
		return this;
	}

	public UnionBuilder computedUserSet(String relation)
	{
		union.child(new ComputedUserSet(getParent().build(), relation));
		return this;
	}
}
