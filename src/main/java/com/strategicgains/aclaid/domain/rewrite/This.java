package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class This
implements Expression
{
	public This()
	{
		super();
	}

	@Override
	public TupleSet rewrite(TupleSet input, Tuple key)
	{
		return LocalTupleSet.EMPTY;
	}
}
