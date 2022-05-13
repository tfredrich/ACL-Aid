package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public abstract class AbstractChildBuildable
implements Buildable
{
	private NamespaceConfigurationBuilder parent;

	protected AbstractChildBuildable(NamespaceConfigurationBuilder parent)
	{
		super();
		this.parent = parent;
	}

	@Override
	public NamespaceConfigurationBuilder namespace(String name)
	{
		return parent.namespace(name);
	}

	@Override
	public RelationBuilder relation(String name)
	{
		return parent.relation(name);
	}

	@Override
	public NamespaceConfigurationBuilder tuple(String resource, String relation, String userset)
	throws ParseException, RelationNotRegisteredException
	{
		return parent.tuple(resource, relation, userset);
	}

	@Override
	public NamespaceConfigurationBuilder tuple(ResourceName resource, String relation, UserSet userset)
	throws RelationNotRegisteredException
	{
		return parent.tuple(resource, relation, userset);
	}

	@Override
	public NamespaceConfigurationBuilder tuple(String tuple)
	throws ParseException
	{
		return parent.tuple(tuple);
	}

	@Override
	public TupleBuilder tuples()
	{
		return parent.tuples();
	}
}
