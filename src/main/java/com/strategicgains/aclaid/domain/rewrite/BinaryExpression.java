package com.strategicgains.aclaid.domain.rewrite;

import java.beans.Expression;

import com.strategicgains.aclaid.domain.RelationDefinition;

public abstract class BinaryExpression
extends AbstractRewriteRule
{
	private Expression leftOperand;
	private Expression rightOperand;
	
	protected BinaryExpression(RelationDefinition parent, Expression leftExpression, Expression rightExpression)
	{
		super(parent);
		this.leftOperand = leftExpression;
		this.rightOperand = rightExpression;
	}
	
	protected Expression getLeftOperand()
	{
		return leftOperand;
	}

	protected Expression getRightOperand()
	{
		return rightOperand;
	}
}
