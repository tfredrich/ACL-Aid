package com.strategicgains.aclaid.builder;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.Child;
import com.strategicgains.aclaid.domain.rewrite.ComputedUserSet;
import com.strategicgains.aclaid.domain.rewrite.Union;

public class RelationBuilder
extends AbstractChildBuildable<ResourceDefinitionBuilder>
{
	private RelationDefinition relationDefinition;

	public RelationBuilder(String relation, ResourceDefinitionBuilder parent)
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
		return child(new ComputedUserSet(relationDefinition, relation));
	}

	public RelationBuilder child(Child child)
	{
		relationDefinition.addRewriteRule(child);
		return this;
	}

	public UnionBuilder union() {
		Union union = new Union(relationDefinition);
		relationDefinition.addRewriteRule(union);
		return new UnionBuilder(this, union);
	}
}
