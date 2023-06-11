package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class This
implements Child
{
	@Override
	public TupleSet rewrite(TupleSet set, Tuple tuple)
	{
		TupleSet tuples = set.read(tuple.getRelation(), tuple.getResource());

		if (tuples != null) return tuples;
		return LocalTupleSet.EMPTY;
	}
}
