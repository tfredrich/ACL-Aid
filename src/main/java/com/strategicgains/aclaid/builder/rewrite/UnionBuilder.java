package com.strategicgains.aclaid.builder.rewrite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.RewriteRule;
import com.strategicgains.aclaid.domain.rewrite.Union;

public class UnionBuilder
implements SetOperationBuilder
{
	private List<RewriteRuleBuilder> children = new ArrayList<>();

	protected UnionBuilder(RewriteRuleBuilder... ruleBuilders)
	{
		super();
		children = Arrays.(ruleBuilders);

		for (RewriteRuleBuilder ruleBuilder : ruleBuilders)
		{
			children.add(ruleBuilder);
		}
	}

	@Override
	public RewriteRule build(RelationDefinition relation)
	{
		List<RewriteRule> rules = children.stream().map(c -> c.build(relation)).toList();
		return new Union(rules);
	}
}
