package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.exception.InvalidTupleException;

public class ComputedUserSet
{
	private String parent;
	private String child;
	private ResourceName resource;

	public ComputedUserSet(String parent, String child)
	{
		super();
		setParent(parent);
		setChild(child);
	}

	public ComputedUserSet(ResourceName resource, String parent, String child)
	{
		this(parent, child);
		setResource(resource);
	}

	public ResourceName getResource()
	{
		return resource;
	}

	private void setResource(ResourceName resource)
	{
		this.resource = resource;
	}

	private String getChild()
	{
		return child;
	}

	private void setChild(String relation)
	{
		this.child = relation;
	}

	public String getParent()
	{
		return parent;
	}

	private void setParent(String relation)
	{
		this.parent = relation;
	}

	public Tuple compute(Tuple t)
	{
		try
		{
			if (t.getRelation().equals(getParent()))
			{
				return new Tuple(t.getUserset(), getChild(), t.getResource());
			}
		}
		catch (InvalidTupleException e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
