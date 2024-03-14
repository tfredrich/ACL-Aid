package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleSet;

public abstract class BinaryRewriteRule
implements RewriteRule
{
	private RewriteRule lRule;
	private RewriteRule rRule;

	protected BinaryRewriteRule()
	{
		super();
	}

	protected BinaryRewriteRule(RewriteRule left, RewriteRule right)
	{
		setLRule(left);
		setRRule(right);
	}

	protected RewriteRule getLRule()
	{
		return lRule;
	}

	protected BinaryRewriteRule setLRule(RewriteRule operand)
	{
		this.lRule = operand;
		return this;
	}

	protected RewriteRule getRRule()
	{
		return rRule;
	}

	protected BinaryRewriteRule setRRule(RewriteRule operand)
	{
		this.rRule = operand;
		return this;
	}

	@Override
	public TupleSet rewrite(TupleSet input, String parentRelation, ObjectId key)
	{
		TupleSet left = lRule.rewrite(input, parentRelation, key);
		TupleSet right = rRule.rewrite(input, parentRelation, key);
		return evaluate(left, right);
	}

	protected abstract TupleSet evaluate(TupleSet left, TupleSet right);
}
