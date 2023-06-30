package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public interface Rewritable
{
	TupleSet rewrite(TupleSet inputSet, Tuple tupleKey);
}
