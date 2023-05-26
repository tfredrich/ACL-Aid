package com.strategicgains.aclaid.domain;

import java.util.ArrayList;
import java.util.List;

public class Relation
{
	private String name;
	private List<RewriteRule> rewriteRules;

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

	public void addRewriteRule(RewriteRule rule)
	{
		if (rewriteRules == null)
		{
			this.rewriteRules = new ArrayList<>();
		}

		this.rewriteRules.add(rule);
	}

	public boolean hasRewriteRules()
	{
		return (rewriteRules != null);
	}

	public TupleSet rewrite(UserSet userset, String relation, ResourceName resource)
	{
		if (!hasRewriteRules()) return null;
		
		TupleSet tuples = new LocalTupleSet();
		rewriteRules.stream().forEach(r -> {
			Tuple t = r.rewrite(userset, relation, resource);
			if (t != null)
			{
				tuples.add(t);
			}
		});
		return tuples;
	}
}
