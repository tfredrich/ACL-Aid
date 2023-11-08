package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class Union
extends AggregateExpression
{
	public Union(Expression... expressions)
	{
		super(expressions);
	}

	@Override
	public Union add(Expression expression)
	{
		super.add(expression);
		return this;
	}

	@Override
	public TupleSet rewrite(TupleSet input, Tuple key)
	{
		TupleSet rewrites = new LocalTupleSet();
		operands()
			.map(r -> r.rewrite(input, key))
			.forEach(rewrites::addAll);
		return rewrites;	}
}
