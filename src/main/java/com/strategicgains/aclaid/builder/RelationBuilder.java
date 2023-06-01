package com.strategicgains.aclaid.builder;

import com.strategicgains.aclaid.domain.Relation;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.domain.rewrite.ComputedUserSet;

public class RelationBuilder
extends AbstractChildBuildable<NamespaceBuilder>
{
	private String name;
	private UserSet rewrites;

	public RelationBuilder(String relation, NamespaceBuilder parent)
	{
		super(parent);
		this.name = relation;
	}

	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return (String.format("Relation: %s", name));
	}

	Relation build()
	{
		Relation r = new Relation(name);
		r.setRewrite(rewrites);
		return r;
	}

	public RelationBuilder childOf(String relation)
	{
		rewrites = new ComputedUserSet(relation);
		return this;
	}
}
