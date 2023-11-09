package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class This
implements RewriteRule
{
	public This()
	{
		super();
	}

	@Override
	public TupleSet rewrite(TupleSet input, Tuple key)
	{
		return input.read(key.getRelation(), key.getResource());
	}
}
