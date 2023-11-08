package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

public class RewriteExpression 
implements RewriteFunction
{
	private RewriteFunction function;
	private RewriteExpression[] children;

	public TupleSet rewrite(TupleSet tuples, Tuple tupleKey)
	{
		// TODO Auto-generated method stub
		return LocalTupleSet.EMPTY;
	}

	public RewriteExpression add(Rewritable child)
	{
		// TODO implement add()
		return this;
	}

	@Override
	public UserSet rewrite(Tuple tupleKey) {
		// TODO Auto-generated method stub
		return null;
	}
}
