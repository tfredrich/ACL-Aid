package com.strategicgains.aclaid.domain.rewrite;

import java.util.ArrayList;
import java.util.List;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class Union
extends AbstractChild
{
	public Union(RelationDefinition parent)
	{
		super(parent);
	}

	private List<Child> children = new ArrayList<>();

	public Union child(Child child)
	{
		children.add(child);
		return this;
	}

	@Override
	public TupleSet rewrite(TupleSet inputSet, Tuple tupleKey)
	{
		TupleSet rewrites = new LocalTupleSet();
		children
			.stream()
			.map(r -> r.rewrite(inputSet, tupleKey))
			.forEach(rewrites::addAll);
		return rewrites;
	}
}
