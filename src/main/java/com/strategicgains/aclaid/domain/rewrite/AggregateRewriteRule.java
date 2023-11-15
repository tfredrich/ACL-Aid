package com.strategicgains.aclaid.domain.rewrite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.strategicgains.aclaid.domain.RelationDefinition;

public abstract class AggregateRewriteRule
extends AbstractRewriteRule
{
	private List<RewriteRule> operands = new ArrayList<>();

	protected AggregateRewriteRule(RelationDefinition parent)
	{
		super(parent);
	}

	protected AggregateRewriteRule(RelationDefinition parent, List<RewriteRule> operands)
	{
		this(parent);
		setOperands(operands);
	}

	protected AggregateRewriteRule child(RewriteRule operand)
	{
		this.operands.add(operand);
		return this;
	}

	protected Stream<RewriteRule> operands()
	{
		return operands.stream();
	}

	private void setOperands(List<RewriteRule> operands)
	{
		if (operands == null && !this.operands.isEmpty())
		{
			this.operands.clear();
			return;
		}

		this.operands = new ArrayList<>(operands);
	}
}
