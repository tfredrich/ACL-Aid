package com.strategicgains.aclaid.domain.rewrite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.strategicgains.aclaid.domain.RelationDefinition;

public abstract class AggregateRewriteRule
extends AbstractRewriteRule
{
	private List<RewriteRule> children = new ArrayList<>();

	protected AggregateRewriteRule(RelationDefinition parent)
	{
		super(parent);
	}

	protected AggregateRewriteRule(RelationDefinition parent, List<RewriteRule> children)
	{
		this(parent);
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
