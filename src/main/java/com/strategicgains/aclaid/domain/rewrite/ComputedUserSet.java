package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

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

	public RewriteRule withRelation(String relation)
	{
		setRelation(relation);
		return this;
	}

	@Override
	public TupleSet rewrite(TupleSet input, ResourceName resource)
	{
		UserSet rewrite = new UserSet(resource, relation);
		Tuple t = new Tuple(rewrite, getParent().getName(), resource);
		return new LocalTupleSet().add(t);
	}
}
