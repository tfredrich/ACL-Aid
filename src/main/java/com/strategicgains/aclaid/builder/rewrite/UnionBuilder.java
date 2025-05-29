package com.strategicgains.aclaid.builder.rewrite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

		if (ruleBuilders == null || ruleBuilders.length == 0)
		{
			throw new IllegalArgumentException("At least one child rule builder is required.");
		}

		Stream.of(ruleBuilders)
			.forEach(rb -> {
				children.add(rb);
			});
	}

	@Override
	public RewriteRule build(RelationDefinition relation)
	{
		List<RewriteRule> rules = children.stream().map(c -> c.build(relation)).toList();
		return new Union(rules);
	}
}
