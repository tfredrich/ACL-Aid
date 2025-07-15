package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.domain.rewrite.RewriteRule;
import com.strategicgains.aclaid.domain.rewrite.This;
import com.strategicgains.aclaid.domain.rewrite.expression.UsersetExpression;

public class RelationDefinition
{
	private ObjectDefinition objectDefinition;
	private String name;
	private RewriteRule rewriteRules;

	public RelationDefinition(ObjectDefinition objectDefinition, String name)
	{
		super();
		this.name = name;
		setObjectDefinition(objectDefinition);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ObjectDefinition getObjectDefinition()
	{
		return objectDefinition;
	}

	private void setObjectDefinition(ObjectDefinition objectDefinition)
	{
		this.objectDefinition = objectDefinition;
	}

	public RelationDefinition getSibling(String relation)
	{
		RelationDefinition sibling = objectDefinition.getRelation(relation);

		if (sibling == null)
		{
			throw new IllegalArgumentException(String.format("Relation '%s' not found in object '%s'.", relation, objectDefinition.getName()));
		}

		return sibling;
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

	public UsersetExpression rewrite(ObjectId objectId)
	{
		if (hasRewriteRules())
		{
			return rewriteRules.rewrite(objectId);
		}

		return new This(this).rewrite(objectId);
	}
}
