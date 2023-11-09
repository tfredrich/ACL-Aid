package com.strategicgains.aclaid.builder.rewrite;

import com.strategicgains.aclaid.domain.rewrite.ComputedUserSet;
import com.strategicgains.aclaid.domain.rewrite.This;
import com.strategicgains.aclaid.domain.rewrite.Union;

public class UnionBuilder
extends RewriteRuleBuilder
{
	private Union union;

	protected UnionBuilder()
	{
		super();
	}

	public UnionBuilder _this()
	{
		union.add(new This());
		return this;
	}

	public UnionBuilder computedUserSet(String relation)
	{
		union.add(new ComputedUserSet(getParent().getName(), relation);
		return this;
	}

//	public TupleToUserSetBuilder tupleToUserSet()
//	{
//		TupleToUserSet set = new TupleToUserSet(getParent().build());
//		child(set);
//		return new TupleToUserSetBuilder(this);
//	}
}
