package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class This
implements Child
{
	@Override
	public TupleSet rewrite(TupleSet inputSet, Tuple tupleKey)
	{
		TupleSet tuples = inputSet.read(tupleKey.getRelation(), tupleKey.getResource());

		if (tuples != null) return tuples;
		return LocalTupleSet.EMPTY;
	}
}
