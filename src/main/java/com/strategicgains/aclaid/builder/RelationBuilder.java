package com.strategicgains.aclaid.builder;

import com.strategicgains.aclaid.builder.rewrite.RewriteRuleBuilder;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.RewriteRule;

public class RelationBuilder
extends AbstractChildBuildable<ResourceDefinitionBuilder>
{
	private RelationDefinition relationDefinition;
	private RewriteRuleBuilder rewriteRuleBuilder;

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
		if (rewriteRuleBuilder != null)
		{
			RewriteRule rewrites = rewriteRuleBuilder.build(relationDefinition);
			relationDefinition.setRewriteRules(rewrites);
		}

		return relationDefinition;
	}

//	public RelationBuilder childOf(String relation)
//	{
//		return child(new ComputedUserSet(relationDefinition, relation));
//	}
//
//	public RelationBuilder child(RewriteRule child)
//	{
//		relationDefinition.addRewriteRule(child);
//		return this;
//	}

	public RelationBuilder rewrite(RewriteRuleBuilder rewrite)
	{
		this.rewriteRuleBuilder = rewrite;
		return this;
	}
}
