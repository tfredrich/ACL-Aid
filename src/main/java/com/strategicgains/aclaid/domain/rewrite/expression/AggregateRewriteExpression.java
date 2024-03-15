package com.strategicgains.aclaid.domain.rewrite.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AggregateRewriteExpression
implements RewriteExpression
{
	private List<RewriteExpression> children = new ArrayList<>();

	protected AggregateRewriteExpression()
	{
		super();
	}

	protected AggregateRewriteExpression(List<RewriteExpression> children)
	{
		this();
		setChildren(children);
	}

	protected AggregateRewriteExpression addChild(RewriteExpression child)
	{
		this.children.add(child);
		return this;
	}

	protected Stream<RewriteExpression> children()
	{
		return children.stream();
	}

	private void setChildren(List<RewriteExpression> children)
	{
		if (children == null && !this.children.isEmpty())
		{
			this.children.clear();
			return;
		}

		this.children = new ArrayList<>(children);
	}
}
