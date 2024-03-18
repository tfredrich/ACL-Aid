package com.strategicgains.aclaid.domain;

import java.util.Set;

import com.strategicgains.aclaid.domain.rewrite.RewriteRule;
import com.strategicgains.aclaid.domain.rewrite.This;

public class RelationDefinition
{
	private String name;
	private RewriteRule rewriteRules;

	public RelationDefinition(String name)
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

	public void setRewriteRules(RewriteRule expression)
	{
		this.rewriteRules = expression;
	}

	public boolean hasRewriteRules()
	{
		return (rewriteRules != null);
	}

	public boolean check(TupleSet tuples, UserSet userset, ObjectId objectId)
	{
		Set<UserSet> rewrites = null;

		if (hasRewriteRules()) rewrites = rewriteRules.rewrite(tuples, objectId);
		else rewrites = new This(this).rewrite(tuples, objectId);

		return rewrites.contains(userset);
	}
}
