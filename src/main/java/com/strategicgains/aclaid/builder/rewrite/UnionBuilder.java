package com.strategicgains.aclaid.builder.rewrite;

public class UnionBuilder
extends RewriteRuleBuilder
{
	private Object[] children;

	protected UnionBuilder()
	{
		super();
	}

	public UnionBuilder _this()
	{
//		child(new This());
		return this;
	}

	public UnionBuilder computedUserSet(String relation)
	{
//		child(new ComputedUserSet(getParent().build(), relation));
		return this;
	}

//	public TupleToUserSetBuilder tupleToUserSet()
//	{
//		TupleToUserSet set = new TupleToUserSet(getParent().build());
//		child(set);
//		return new TupleToUserSetBuilder(this);
//	}
}
