package com.strategicgains.aclaid.builder;

import static com.strategicgains.aclaid.builder.rewrite.Rewrites._this;
import static com.strategicgains.aclaid.builder.rewrite.Rewrites.computedUserSet;

import com.strategicgains.aclaid.builder.rewrite.RewriteRuleBuilder;
import com.strategicgains.aclaid.builder.rewrite.Rewrites;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.RewriteRule;

public class RelationBuilder
extends AbstractChildBuildable<ObjectDefinitionBuilder>
{
	private RelationDefinition relationDefinition;
	private RewriteRuleBuilder rewriteRuleBuilder;

	public RelationBuilder(String relation, ObjectDefinitionBuilder parent)
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

	public RelationBuilder rewrite(RewriteRuleBuilder rewrite)
	{
		this.rewriteRuleBuilder = rewrite;
		return this;
	}

	public RelationBuilder childOf(String relation)
	{
		RewriteRuleBuilder union = Rewrites.union(
			_this(),
			computedUserSet()
				.relation(relation)
		);
		rewrite(union);
		return this;
	}
}
