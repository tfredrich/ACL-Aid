package com.strategicgains.aclaid.domain.rewrite;

public abstract class UnaryExpression
implements Expression
{
	private Expression operand;
	
	protected UnaryExpression(Expression expression)
	{
		this.operand = expression;
	}
	
	protected Expression getOperand()
	{
		return operand;
	}
}
