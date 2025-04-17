package com.strategicgains.aclaid.domain.rewrite.predicate;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

/**
 * From the Zanzibar document: Computes a tupleset from the input object,
 * fetches relation tuples matching the tupleset, and computes a userset from
 * every fetched relation tuple. This flexible primitive allows our clients to
 * express complex policies such as “look up the parent folder of the document
 * and inherit its viewers”.
 */
public class TupleToUserSetPredicate
implements UsersetPredicate
{
	private ObjectId objectId;
	private String relation;
	private UsersetPredicate userSet;

	public TupleToUserSetPredicate(ObjectId objectId, String relation, UsersetPredicate computedUserSet)
	{
		super();
		this.objectId = objectId;
		this.relation = relation;
		this.userSet = computedUserSet;
	}

	@Override
	public boolean test(TupleSet tuples, UserSet userset)
    {
//		TupleSet relations = tuples.readAll(relation, objectId);
//        return userSet.evaluate(relations, userSet, relation, objectId);
		return false;
	}
}
