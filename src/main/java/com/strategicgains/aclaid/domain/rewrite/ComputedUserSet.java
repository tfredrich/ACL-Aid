package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class ComputedUserSet
extends AbstractRewriteRule
{
	private String relation;
	private ResourceName resource;

	public ComputedUserSet(RelationDefinition parent)
	{
		super(parent);
	}

	public ComputedUserSet(RelationDefinition parent, String relation)
	{
		this(parent);
		setRelation(relation);
	}

	public ComputedUserSet(RelationDefinition parent, ResourceName resource)
	{
		this(parent);
		setResource(resource);
	}

	public ComputedUserSet(RelationDefinition parent, ResourceName resource, String relation)
	{
		this(parent);
		setResource(resource);
		setRelation(relation);
	}

	public void setResource(ResourceName resource)
	{
		this.resource = resource;
	}

	public void setRelation(String relation)
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
