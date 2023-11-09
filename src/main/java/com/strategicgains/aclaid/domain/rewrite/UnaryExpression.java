package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;

public abstract class UnaryExpression
extends AbstractRewriteRule
{
	private RewriteRule operand;
	
	protected UnaryExpression(RelationDefinition parent, RewriteRule expression)
	{
		super(parent);
		this.operand = expression;
	}
	
	protected RewriteRule getOperand()
	{
		return operand;
	}
}
