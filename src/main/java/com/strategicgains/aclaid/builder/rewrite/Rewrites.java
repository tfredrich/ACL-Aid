package com.strategicgains.aclaid.builder.rewrite;

public abstract class Rewrites
{
	private Rewrites()
	{
		// prevents instantiation.
	}

	public static UnionBuilder union(RewriteRuleBuilder... ruleBuilders)
	{
		return new UnionBuilder(ruleBuilders);
	}

	public static ComputedUserSetBuilder computedUserSet(String relation)
	{
		return new ComputedUserSetBuilder().relation(relation);
	}

	public static TupleToUserSetBuilder tupleToUserSet(String relation, ComputedUserSetBuilder computedUserSetBuilder)
	{
		return new TupleToUserSetBuilder(null, relation, computedUserSetBuilder);
	}

	public static ThisBuilder _this()
	{
		return new ThisBuilder();
	}
}
