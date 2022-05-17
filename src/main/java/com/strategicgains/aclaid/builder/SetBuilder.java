package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public abstract class SetBuilder
{
	private RelationBuilder parent;

	protected SetBuilder(RelationBuilder parent)
	{
		super();
		this.parent = parent;
	}

	public RelationBuilder relation(String name)
	{
		return parent.relation(name);
	}

	public NamespaceConfigurationBuilder tuple(String resource, String relation, String userset)
	throws ParseException, RelationNotRegisteredException
	{
		return parent.tuple(resource, relation, userset);
	}

	public NamespaceConfigurationBuilder namespace(String namespace)
	{
		return parent.namespace(namespace);
	}

}
