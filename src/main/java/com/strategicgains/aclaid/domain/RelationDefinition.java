package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.domain.rewrite.RewriteRule;

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

	public void addRewriteRule(RewriteRule child)
	{
		if (rewriteRules == null)
		{
			rewriteRules = new RewriteExpression();
		}
	
		rewriteRules.add(child);
	}

	public boolean hasRewriteRules()
	{
		return (rewriteRules != null);
	}

	public TupleSet rewrite(TupleSet tuples, Tuple tuple)
	{
		if (!hasRewriteRules()) return LocalTupleSet.EMPTY;

		return rewriteRules.rewrite(tuples, tuple);
	}

	public void setRewriteRules(RewriteRule rules)
	{
		this.rewriteRules = rules;
	}
}
