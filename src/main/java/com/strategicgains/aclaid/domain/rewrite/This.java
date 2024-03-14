package com.strategicgains.aclaid.domain.rewrite;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.domain.LocalTupleSet;
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
	public TupleSet rewrite(TupleSet relationTuples, String parentRelation, ObjectId objectId)
	{
		TupleSet ts = relationTuples.read(parentRelation, objectId);

		return (ts != null) ? ts : LocalTupleSet.EMPTY_SET;
	}

	public Set<UserSet> evaluate(TupleSet relationTuples, String parentRelation, ObjectId objectId)
	{
		TupleSet ts = relationTuples.read(parentRelation, objectId);

		return (ts != null) ? ts.userSets().collect(Collectors.toSet()) : Collections.emptySet();
	}
}
