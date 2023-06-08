package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.domain.rewrite.RewriteRules;

public class Relation
{
	private String name;
	private RewriteRules rewriteRules;

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

	public void setRewriteRules(RewriteRules rewriteRules)
	{
		this.rewriteRules = rewriteRules;
	}

	public boolean hasRewriteRules()
	{
		return (rewriteRules != null);
	}

	public TupleSet rewrite(TupleSet tuples, UserSet userset)
	{
		if (!hasRewriteRules()) return new LocalTupleSet();

		return rewriteRules.rewrite(tuples, userset, name);
	}
}
