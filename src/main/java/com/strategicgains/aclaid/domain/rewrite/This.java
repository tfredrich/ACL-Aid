package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class This
extends AbstractRewriteRule
{
	public This(RelationDefinition relation)
	{
		super(relation);
	}

	@Override
	public TupleSet rewrite(TupleSet input, Tuple key)
	{
		return input.read(key.getRelation(), key.getResource());
	}
}
