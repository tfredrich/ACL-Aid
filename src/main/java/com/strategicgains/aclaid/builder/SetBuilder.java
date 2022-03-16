package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.builder.NamespaceAclBuilder.RelationBuilder;
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

	public NamespaceAclBuilder tuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException
	{
		return parent.tuple(userset, relation, resource);
	}
}
