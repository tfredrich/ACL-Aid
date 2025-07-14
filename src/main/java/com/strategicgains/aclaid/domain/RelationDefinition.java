package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.domain.rewrite.RewriteRule;
import com.strategicgains.aclaid.domain.rewrite.This;
import com.strategicgains.aclaid.domain.rewrite.expression.UsersetExpression;

public class RelationDefinition
{
	private ObjectDefinition parent;
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

	public ObjectDefinition getParent()
	{
		return parent;
	}

	public void setParent(ObjectDefinition parent)
	{
		this.parent = parent;
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

	public boolean check(TupleStore tuples, UserSet userset, ObjectId objectId)
	{
		UsersetExpression rewrites = rewrite(objectId);
		return rewrites.evaluate(tuples, userset);
	}

	private UsersetExpression rewrite(ObjectId objectId)
	{
		if (hasRewriteRules())
		{
			return rewriteRules.rewrite(objectId);
		}

		return new This(this).rewrite(objectId);
	}

	public UsersetExpression rewrite(ObjectId objectId, String relation)
	{
		if (parent != null && parent.containsRelation(relation))
		{
			return parent.getRelation(relation).rewrite(objectId);
		}
		else
		{
			throw new IllegalArgumentException("Relation not found: " + relation); 
		}
	}
}
