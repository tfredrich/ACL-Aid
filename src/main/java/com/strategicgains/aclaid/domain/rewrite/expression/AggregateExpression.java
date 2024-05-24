package com.strategicgains.aclaid.domain.rewrite.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AggregateExpression
implements UsersetExpression
{
	private List<UsersetExpression> children = new ArrayList<>();

	protected AggregateExpression()
	{
		super();
	}

	protected AggregateExpression(List<UsersetExpression> children)
	{
		this();
		setChildren(children);
	}

	protected AggregateExpression addChild(UsersetExpression child)
	{
		this.children.add(child);
		return this;
	}

	protected Stream<UsersetExpression> children()
	{
		return children.stream();
	}

	private void setChildren(List<UsersetExpression> children)
	{
		if (children == null && !this.children.isEmpty())
		{
			this.children.clear();
			return;
		}

		this.children = new ArrayList<>(children);
	}
}
