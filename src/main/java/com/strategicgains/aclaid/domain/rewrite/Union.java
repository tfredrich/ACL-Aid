package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class Union
extends AggregateRewriteRule
{
	public Union(RelationDefinition parent)
	{
		super(parent);
	}

	public Union(RelationDefinition parent, RewriteRule... expressions)
	{
		super(parent, expressions);
	}

	@Override
	public Union add(RewriteRule rewriteRule)
	{
		super.add(rewriteRule);
		return this;
	}

	@Override
	public TupleSet rewrite(TupleSet input, Tuple key)
	{
		TupleSet rewrites = new LocalTupleSet();
		operands()
			.map(r -> r.rewrite(input, key))
			.forEach(rewrites::addAll);
		return rewrites;
	}
}
