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

	public static ComputedUserSetBuilder computedUserSet()
	{
		return new ComputedUserSetBuilder(null);
	}

	public static TupleToUserSetBuilder tupleToUserSet()
	{
		return new TupleToUserSetBuilder();
	}

	public static ThisBuilder _this()
	{
		return new ThisBuilder(null);
	}
}
