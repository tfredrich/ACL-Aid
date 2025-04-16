package com.strategicgains.aclaid.domain.rewrite.runtime;

import java.util.Set;

import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

public interface UsersetExpression
{
	Set<UserSet> evaluate(TupleSet tuples);
}
