package com.strategicgains.aclaid.builder;

import com.strategicgains.aclaid.builder.rewrite.RewriteRuleBuilder;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.ComputedUserSet;
import com.strategicgains.aclaid.domain.rewrite.Rewritable;
import com.strategicgains.aclaid.domain.rewrite.RewriteExpression;

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

	public RelationBuilder child(Rewritable child)
	{
		relationDefinition.addRewriteRule(child);
		return this;
	}

	public RelationBuilder rewrite(RewriteRuleBuilder rewrite)
	{
		RewriteExpression rules = rewrite.build(relationDefinition);
		relationDefinition.setRewriteRules(rules);
		return this;
	}
}
