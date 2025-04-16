package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.rewrite.runtime.TupleToUserSetExpression;
import com.strategicgains.aclaid.domain.rewrite.runtime.UsersetExpression;

/**
 * From the Zanzibar document:
 * Computes a tupleset from the input object,
 * fetches relation tuples matching the tupleset, and computes a userset from
 * every fetched relation tuple. This flexible primitive allows our clients to
 * express complex policies such as “look up the parent folder of the document
 * and inherit its viewers”.
 */
public class TupleToUserSet
implements RewriteRule
{
	private String relation;
	private ComputedUserSet computedUserSet;

	public TupleToUserSet(String relation, ComputedUserSet computedUserSet)
	{
		super();
		this.relation = relation;
		this.computedUserSet = computedUserSet;
	}

	@Override
	public UsersetExpression rewrite(ObjectId objectId)
	{
		return new TupleToUserSetExpression(objectId, relation, computedUserSet.rewrite(objectId));
	}
}
