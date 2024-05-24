package com.strategicgains.aclaid.domain.rewrite.expression;

import java.util.Set;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.domain.InMemoryTupleSet;
import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

/**
 * Computes a tupleset from the input object, fetches relation tuples matching the tupleset, and computes
 * a userset from every fetched relation tuple. This flexible primitive allows our clients to express complex
 * policies such as “look up the parent folder of the document and inherit its viewers”.
 */
public class TupleToUserSet
implements UsersetExpression
{
	private String relation;
	private UserSet userSet;

	public TupleToUserSet(String relation, UserSet computedUserSet)
	{
		super();
		this.relation = relation;
		this.userSet = computedUserSet;
	}

	@Override
	public Set<UserSet> evaluate(TupleSet tuples)
	{
//		return tuples.readAll(relation, userSet.getObjectId());
//				.stream()
//				.flatMap(u -> computedUserSet.evaluate(tuples).stream())
//				.collect(Collectors.toSet());
		return null;
	}
}
