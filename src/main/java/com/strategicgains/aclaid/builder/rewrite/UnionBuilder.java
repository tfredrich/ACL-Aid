package com.strategicgains.aclaid.builder.rewrite;

import java.util.ArrayList;
import java.util.List;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.RewriteRule;
import com.strategicgains.aclaid.domain.rewrite.Union;

public class UnionBuilder
implements SetOperationBuilder
{
	private List<RewriteRuleBuilder> children = new ArrayList<>();

	protected UnionBuilder()
	{
		super();
	}

	@Override
	public RewriteRule build(RelationDefinition relation)
	{
		List<RewriteRule> rules = children.stream().map(c -> c.build(relation)).toList();
		return new Union(relation, rules);
	}

	@Override
	public ThisBuilder _this()
	{
		ThisBuilder builder = new ThisBuilder(this);
		children.add(builder);
		return builder;
	}

	@Override
	public ComputedUserSetBuilder computedUserSet()
	{
		ComputedUserSetBuilder builder = new ComputedUserSetBuilder(this);
		children.add(builder);
		return builder;
	}

	@Override
	public TupleToUserSetBuilder tupleToUserSet()
	{
		TupleToUserSetBuilder builder = new TupleToUserSetBuilder(this);
		children.add(builder);
		return builder;
	}

	@Override
	public SetOperationBuilder end()
	{
		return this;
	}
}
