package com.strategicgains.aclaid.builder.rewrite;

public interface SetOperationBuilder
extends RewriteRuleBuilder
{
	ThisBuilder _this();
	ComputedUserSetBuilder computedUserSet();
	TupleToUserSetBuilder tupleToUserSet();
}