package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class TupleToUserSet
extends AbstractChild
{

	public TupleToUserSet(RelationDefinition parent)
	{
		super(parent);
	}

	@Override
	public TupleSet rewrite(TupleSet inputSet, Tuple tupleKey)
	{
		// TODO Auto-generated method stub
		return LocalTupleSet.EMPTY;
	}
}
