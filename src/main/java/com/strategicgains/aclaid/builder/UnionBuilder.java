package com.strategicgains.aclaid.builder;

import java.util.HashSet;
import java.util.Set;

import com.strategicgains.aclaid.builder.NamespaceBuilder.RelationBuilder;

public class UnionBuilder
extends SetBuilder
{
	private boolean thisUnion;
	private Set<String> computedUsersets = new HashSet<>();

	public UnionBuilder(RelationBuilder parent)
	{
		super(parent);
	}

	public UnionBuilder _this()
	{
		thisUnion = true;
		return this;
	}

	public UnionBuilder computedUserset(String relation)
	{
		computedUsersets.add(relation);
		return this;
	}
}
