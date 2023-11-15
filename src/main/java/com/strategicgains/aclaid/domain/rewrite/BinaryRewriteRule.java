package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.TupleSet;

public abstract class BinaryRewriteRule
extends AbstractRewriteRule
{
	private RewriteRule lRule;
	private RewriteRule rRule;

	protected BinaryRewriteRule(RelationDefinition parent)
	{
		super(parent);
	}

	protected BinaryRewriteRule(RelationDefinition parent, RewriteRule left, RewriteRule right)
	{
		this(parent);
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
	public TupleSet rewrite(TupleSet input, ResourceName key)
	{
		TupleSet left = lRule.rewrite(input, key);
		TupleSet right = rRule.rewrite(input, key);
		return evaluate(left, right);
	}

	protected abstract TupleSet evaluate(TupleSet left, TupleSet right);
}
