package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

/**
 * Returns all users from stored relation tuples for the ⟨object#relation⟩ pair, including indirect ACLs referenced by usersets
 * from the tuples. This is the default behavior when no rewrite rule is specified.
 * 
 * @author Todd Fredrich
 * @see RewriteRule
 */
public class This
implements RewriteRule
{
	public This()
	{
		super();
	}

	@Override
	public boolean check(TupleSet relationTuples, UserSet user, String relation, ObjectId objectId)
	{
		return relationTuples.readOne(user, relation, objectId) != null;
	}
}
