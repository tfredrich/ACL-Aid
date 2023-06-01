package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;

public class ComputedUserSet
extends UserSet
{
	public ComputedUserSet(String relation)
	{
		super();
		setRelation(relation);
	}

	public Tuple compute(Tuple tuple)
	throws InvalidTupleException
	{
		return new Tuple(tuple.getUserset(), getRelation(), tuple.getResource());
	}
}
