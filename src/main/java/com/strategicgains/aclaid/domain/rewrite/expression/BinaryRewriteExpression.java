package com.strategicgains.aclaid.domain.rewrite.expression;

import com.strategicgains.aclaid.domain.TupleSet;

public abstract class BinaryRewriteExpression
implements RewriteExpression
{
	private RewriteExpression lExpression;
	private RewriteExpression rExpression;

	protected BinaryRewriteExpression()
	{
		super();
	}

	protected BinaryRewriteExpression(RewriteExpression left, RewriteExpression right)
	{
		setLExpression(left);
		setRExpression(right);
	}

	protected RewriteExpression getLExpression()
	{
		return lExpression;
	}

	protected BinaryRewriteExpression setLExpression(RewriteExpression operand)
	{
		this.lExpression = operand;
		return this;
	}

	protected RewriteExpression getRRule()
	{
		return rExpression;
	}

	protected BinaryRewriteExpression setRExpression(RewriteExpression operand)
	{
		this.rExpression = operand;
		return this;
	}

	protected abstract TupleSet evaluate(TupleSet left, TupleSet right);
}
