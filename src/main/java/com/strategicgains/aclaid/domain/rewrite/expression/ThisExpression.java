package com.strategicgains.aclaid.domain.rewrite.expression;

import java.util.Set;

import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.domain.rewrite.RewriteRule;

/**
 * Returns all users from stored relation tuples for the ⟨object#relation⟩ pair, including indirect ACLs referenced by usersets
 * from the tuples. This is the default behavior when no rewrite rule is specified.
 * 
 * @author Todd Fredrich
 * @see RewriteRule
 */
public class ThisExpression
implements UsersetExpression
{
	private UserSet userset;

	public ThisExpression(UserSet userset)
	{
		super();
		this.userset = userset;
	}

	@Override
	public Set<UserSet> evaluate(TupleSet tuples)
	{
		return tuples.expandUserSets(userset.getRelation(), userset.getObjectId());
	}
}
