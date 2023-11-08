package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public interface Expression
{
	TupleSet rewrite(TupleSet input, Tuple key);
}
