package com.strategicgains.aclaid.domain.rewrite;

import javax.management.relation.Relation;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class ComputedUserSet
implements Expression
{
	private Relation relation;
	private ResourceName resource;

	public ComputedUserSet()
	{
		super();
	}

	public ComputedUserSet(Relation relation)
	{
		this();
		setRelation(relation);
	}

	public ComputedUserSet(ResourceName resource)
	{
		this();
		setResource(resource);
	}

	public ComputedUserSet(ResourceName resource, Relation relation)
	{
		this();
		setResource(resource);
		setRelation(relation);
	}

	public void setResource(ResourceName resource)
	{
		this.resource = resource;
	}

	public void setRelation(Relation relation)
	{
		this.relation = relation;
	}

	@Override
	public TupleSet rewrite(TupleSet inputSet, Tuple tupleKey)
	{
		TupleSet rewrites = new LocalTupleSet();
		TupleSet subset = inputSet.read(relation, tupleKey.getResource());

		if (subset != null)
		{
			subset.stream().map(this::rewrite).forEach(rewrites::add);
		}

		return rewrites;
	}

	private Tuple rewrite(Tuple t)
	{
		return new Tuple(t.getUserset(), getParent().getName(), t.getResource());
	}

}
