package com.strategicgains.aclaid.domain.rewrite.predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AggregatePredicate
implements UsersetPredicate
{
	private List<UsersetPredicate> children = new ArrayList<>();

	protected AggregatePredicate()
	{
		super();
	}

	protected AggregatePredicate(List<UsersetPredicate> children)
	{
		this();
		setChildren(children);
	}

	protected AggregatePredicate addChild(UsersetPredicate child)
	{
		this.children.add(child);
		return this;
	}

	protected Stream<UsersetPredicate> children()
	{
		return children.stream();
	}

	private void setChildren(List<UsersetPredicate> children)
	{
		if (children == null && !this.children.isEmpty())
		{
			this.children.clear();
			return;
		}

		this.children = new ArrayList<>(children);
	}
}
