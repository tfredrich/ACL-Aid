package com.strategicgains.aclaid.domain.rewrite;

import java.util.Set;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

/**
 * Computes a tupleset from the input object, fetches relation tuples matching the tupleset, and computes
 * a userset from every fetched relation tuple. This flexible primitive allows our clients to express complex
 * policies such as “look up the parent folder of the document and inherit its viewers”.
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
	public Set<UserSet> rewrite(TupleSet tuples, ObjectId objectId)
	{
		Set<UserSet> usersets = tuples.read(relation, objectId);
		return tuples
			.read(relation, objectId)
			.stream()
			.flatMap(u -> computedUserSet.rewrite(tuples, u.getObjectId()).stream())
			.collect(Collectors.toSet());
	}
}
