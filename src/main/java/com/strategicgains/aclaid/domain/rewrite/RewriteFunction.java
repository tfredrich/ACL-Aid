package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;

@FunctionalInterface
public interface RewriteFunction
{
	UserSet rewrite(Tuple tupleKey);
}
