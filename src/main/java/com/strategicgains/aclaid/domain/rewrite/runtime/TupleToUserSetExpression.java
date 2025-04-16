package com.strategicgains.aclaid.domain.rewrite.runtime;

import java.util.Set;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

/**
 * Computes a tupleset from the input object, fetches relation tuples matching the tupleset, and computes
 * a userset from every fetched relation tuple. This flexible primitive allows our clients to express complex
 * policies such as “look up the parent folder of the document and inherit its viewers”.
 */
public class TupleToUserSetExpression
implements UsersetExpression
{
	private ObjectId objectId;
	private String relation;
	private UsersetExpression userSet;

	public TupleToUserSetExpression(ObjectId objectId, String relation, UsersetExpression computedUserSet)
	{
		super();
		this.objectId = objectId;
		this.relation = relation;
		this.userSet = computedUserSet;
	}

	@Override
	public Set<UserSet> evaluate(TupleSet tuples)
	{
		TupleSet relations = tuples.readAll(relation, objectId);
        return userSet.evaluate(relations);
	}
}
