package com.strategicgains.aclaid.builder;

import com.strategicgains.aclaid.domain.Relation;
import com.strategicgains.aclaid.domain.rewrite.ComputedUserSet;
import com.strategicgains.aclaid.domain.rewrite.RewriteRules;

public class RelationBuilder
extends AbstractChildBuildable<NamespaceBuilder>
{
	private String name;
	private RewriteRules rewrites;

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
		r.setRewriteRules(rewrites);
		return r;
	}

	public RelationBuilder childOf(String relation)
	{
		if (rewrites == null)
		{
			rewrites = new RewriteRules();
		}

		rewrites.add(new ComputedUserSet(relation));
		return this;
	}
}
