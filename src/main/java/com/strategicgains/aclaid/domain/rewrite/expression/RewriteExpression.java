package com.strategicgains.aclaid.domain.rewrite.expression;

import java.util.Set;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

public interface RewriteExpression
{
	Set<UserSet> rewrite(TupleSet tuples, ObjectId inputObj);
}
