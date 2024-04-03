package com.strategicgains.aclaid.domain.rewrite;

import java.util.Set;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.RelationDefinition;
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
	private RelationDefinition relation;

	public This(RelationDefinition relation)
	{
		super();
		this.relation = relation;
	}

	@Override
	public Set<UserSet> rewrite(TupleSet tuples, ObjectId objectId)
	{
		return tuples.expand(relation.getName(), objectId);
	}
}
