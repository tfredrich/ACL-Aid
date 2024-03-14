package com.strategicgains.aclaid.domain.rewrite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AggregateRewriteRule
implements RewriteRule
{
	private List<RewriteRule> children = new ArrayList<>();

	protected AggregateRewriteRule()
	{
		super();
	}

	protected AggregateRewriteRule(List<RewriteRule> children)
	{
		this();
		setChildren(children);
	}

	protected AggregateRewriteRule addChild(RewriteRule child)
	{
		this.children.add(child);
		return this;
	}

	protected Stream<RewriteRule> stream()
	{
		return children.stream();
	}

	private void setChildren(List<RewriteRule> children)
	{
		if (children == null && !this.children.isEmpty())
		{
			this.children.clear();
			return;
		}

		this.children = new ArrayList<>(children);
	}
}
