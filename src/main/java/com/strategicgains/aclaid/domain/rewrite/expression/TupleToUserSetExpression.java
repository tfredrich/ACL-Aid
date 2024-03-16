package com.strategicgains.aclaid.domain.rewrite.expression;

import java.util.Set;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

/**
 * Computes a tupleset from the input object, fetches relation tuples matching the tupleset, and computes
 * a userset from every fetched relation tuple. This flexible primitive allows our clients to express complex
 * policies such as “look up the parent folder of the document and inherit its viewers”.
 */
public class TupleToUserSetExpression
implements RewriteExpression
{
	private String relation;
	private RewriteExpression childExpression;

	public TupleToUserSetExpression(String relation, RewriteExpression childExpression)
	{
		super();
		this.relation = relation;
		this.childExpression = childExpression;
	}

	@Override
	public Set<UserSet> rewrite(TupleSet tuples, ObjectId objectId)
	{
		return tuples
			.read(relation, objectId)
			.stream()
			.flatMap(t -> childExpression.rewrite(new LocalTupleSet().add(t), t.getObjectId()).stream())
			.collect(Collectors.toSet());
	}
}
