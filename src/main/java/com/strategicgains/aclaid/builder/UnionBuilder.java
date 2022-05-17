package com.strategicgains.aclaid.builder;

import java.util.HashSet;
import java.util.Set;

public class UnionBuilder
extends SetBuilder
{
	private boolean thisUnion;
	private Set<String> computedUsersets = new HashSet<>();
	private Set<TupleToUsersetBuilder> tupleToUsersets = new HashSet<>();

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
		//TODO validate that the relation exists in the namespace.

		computedUsersets.add(relation);
		return this;
	}

	public TupleToUsersetBuilder tupleToUserSet()
	{
		TupleToUsersetBuilder b = new TupleToUsersetBuilder(this);
		tupleToUsersets.add(b);
		return b;
	}
}
