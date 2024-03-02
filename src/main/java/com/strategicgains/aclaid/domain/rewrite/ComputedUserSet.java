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
	private String resourceToken;

	public ComputedUserSet(RelationDefinition parent)
	{
		super(parent);
	}

	public ComputedUserSet(RelationDefinition parent, String relation)
	{
		this(parent);
		setRelation(relation);
	}

	public ComputedUserSet(RelationDefinition parent, String resource, String relation)
	{
		this(parent);
		setResource(resource);
		setRelation(relation);
	}

	protected String getResource()
	{
		return resourceToken;
	}

	public boolean hasResource()
	{
		return (resourceToken != null);
	}

	protected void setResource(String resource)
	{
		this.resourceToken = resource;
	}

	public boolean hasRelation()
	{
		return (relation != null);
	}

	protected void setRelation(String relation)
	{
		this.relation = relation;
	}

	public RewriteRule withRelation(String relation)
	{
		setRelation(relation);
		return this;
	}

	@Override
	public TupleSet rewrite(TupleSet input, ResourceName objectId)
	{
		UserSet rewrite = new UserSet(objectId, relation);
		
		if (hasResource() && getResource().startsWith("$"))
		{
			switch(getResource())
			{
				case Tuple.USERSET_OBJECT:
					System.out.println(Tuple.USERSET_OBJECT + " of " + rewrite + " / " + objectId);
					input.stream().findFirst().ifPresent(t -> rewrite.setResource(t.getUsersetResource()));
					break;
				case Tuple.USERSET_RELATION:
					System.out.println(Tuple.USERSET_RELATION + " of " + rewrite);
					break;
				case Tuple.RELATION:
					System.out.println(Tuple.RELATION + " of " + rewrite);
					break;
				default:
					System.out.println(getResource() + " of " + rewrite);
			}
		}

		return new LocalTupleSet()
			.add(new Tuple(rewrite, getParent().getName(), objectId));
	}
}
