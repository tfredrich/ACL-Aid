package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.TupleSet;

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
	public TupleSet rewrite(TupleSet input, String parentRelation, ResourceName objectId)
	{
		TupleSet read = input.read(relation, objectId);
		TupleSet rewrites = new LocalTupleSet();
		read.stream().forEach(t -> rewrites.addAll(computedUserSet.rewrite(read, parentRelation, t.getObjectId())));
		return rewrites;
	}
}
