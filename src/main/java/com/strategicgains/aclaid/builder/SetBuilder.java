package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public abstract class SetBuilder
{
	private UsersetRewriteBuilder parent;

	protected SetBuilder(UsersetRewriteBuilder parent)
	{
		super();
		this.parent = parent;
	}

	public RelationBuilder relation(String name)
	{
		return parent.relation(name);
	}

	public NamespaceConfigurationBuilder tuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException
	{
		return parent.tuple(userset, relation, resource);
	}

	public NamespaceConfigurationBuilder namespace(String namespace)
	{
		return parent.namespace(namespace);
	}

}
