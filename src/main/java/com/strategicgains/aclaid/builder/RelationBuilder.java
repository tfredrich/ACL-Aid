package com.strategicgains.aclaid.builder;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.Child;
import com.strategicgains.aclaid.domain.rewrite.ComputedUsersetImpl;

public class RelationBuilder
extends AbstractChildBuildable<NamespaceBuilder>
{
	private RelationDefinition relationDefinition;

	public RelationBuilder(String relation, NamespaceBuilder parent)
	{
		super(parent);
		this.relationDefinition = new RelationDefinition(relation);
	}

	public String getName()
	{
		return relationDefinition.getName();
	}

	public String toString()
	{
		return (String.format("Relation: %s", relationDefinition.getName()));
	}

	RelationDefinition build()
	{
		return relationDefinition;
	}

	public RelationBuilder childOf(String relation)
	{
		return child(new ComputedUsersetImpl(relationDefinition, relation));
	}

	public RelationBuilder ownedBy(String string, String string2)
	{
		// TODO Auto-generated method stub
		return this;
	}

	public RelationBuilder child(Child child)
	{
		relationDefinition.addRewriteRule(child);
		return this;
	}
}
