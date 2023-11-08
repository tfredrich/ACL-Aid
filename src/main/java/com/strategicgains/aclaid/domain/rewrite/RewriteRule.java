package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public interface RewriteRule
{
	TupleSet rewrite(TupleSet inputSet, Tuple tupleKey);
}
