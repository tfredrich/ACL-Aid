package com.strategicgains.aclaid.builder;

import static com.strategicgains.aclaid.builder.rewrite.Rewrites._this;
import static com.strategicgains.aclaid.builder.rewrite.Rewrites.computedUserSet;

import com.strategicgains.aclaid.builder.rewrite.RewriteRuleBuilder;
import com.strategicgains.aclaid.builder.rewrite.Rewrites;
import com.strategicgains.aclaid.domain.ObjectDefinition;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.RewriteRule;

public class RelationBuilder
extends AbstractChildBuildable<ObjectDefinitionBuilder>
{
	private String name;
	private RewriteRuleBuilder rewriteRuleBuilder;

	public RelationBuilder(String relation, ObjectDefinitionBuilder objectBuilder)
	{
		super(objectBuilder);
		this.name = relation;
	}

	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return (String.format("Relation: %s", getName()));
	}

	RelationDefinition build(ObjectDefinition objectDefinition)
	{
		RelationDefinition relationDefinition = new RelationDefinition(objectDefinition, name);
	
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
			computedUserSet(relation)
		);
		rewrite(union);
		return this;
	}
}
