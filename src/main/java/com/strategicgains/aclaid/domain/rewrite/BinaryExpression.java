package com.strategicgains.aclaid.domain.rewrite;

public abstract class BinaryExpression
implements Expression
{
	private Expression leftOperand;
	private Expression rightOperand;
	
	protected BinaryExpression(Expression leftExpression, Expression rightExpression)
	{
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
