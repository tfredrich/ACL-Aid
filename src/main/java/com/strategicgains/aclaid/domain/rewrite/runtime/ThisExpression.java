package com.strategicgains.aclaid.domain.rewrite.runtime;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.domain.rewrite.RewriteRule;

/**
 * From the Zanzibar document:
 * Returns all users from stored relation tuples for the ⟨object#relation⟩
 * pair, including indirect ACLs referenced by usersets from the tuples.
 * This is the default behavior when no rewrite rule is specified.
 * 
 * @author Todd Fredrich
 * @see RewriteRule
 */
public class ThisExpression
implements UsersetExpression
{
	// The object/relation pair to retrieve from the tuple set.
	private ObjectId objectId;
	private String relation;

	public ThisExpression(ObjectId objectId, String relation)
	{
		super();
		this.objectId = objectId;
		this.relation = relation;
	}

	@Override
	public boolean evaluate(TupleSet tuples, UserSet userset)
   	{
		return tuples.check(userset, relation, objectId);
	}
}
