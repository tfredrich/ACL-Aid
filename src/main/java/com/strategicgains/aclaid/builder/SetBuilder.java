package com.strategicgains.aclaid.builder;

import com.strategicgains.aclaid.builder.AclBuilder.RelationBuilder;

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
}
