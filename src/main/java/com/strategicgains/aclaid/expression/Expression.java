package com.strategicgains.aclaid.expression;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

public interface Expression
{
	Object evaluate(TupleSet tuples, UserSet user, String relation, ObjectId object);
}
