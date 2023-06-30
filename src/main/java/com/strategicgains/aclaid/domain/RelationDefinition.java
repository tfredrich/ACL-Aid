package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.domain.rewrite.Rewritable;
import com.strategicgains.aclaid.domain.rewrite.RewriteRules;

public class RelationDefinition
{
	private String name;
	private RewriteRules rewriteRules;

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

	public void addRewriteRule(Rewritable child)
	{
		if (rewriteRules == null)
		{
			rewriteRules = new RewriteRules();
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
}
