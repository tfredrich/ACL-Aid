package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.TupleSet;

public class TupleToUserSet
extends AbstractRewriteRule
{
	private String relation;
	private ComputedUserSet computedUserSet;

	public TupleToUserSet(RelationDefinition parent, String relation, ComputedUserSet computedUserSet)
	{
		super(parent);
		this.relation = relation;
		this.computedUserSet = computedUserSet;
	}

	@Override
	public TupleSet rewrite(TupleSet input, ResourceName objectId)
	{
		TupleSet read = input.read(relation, objectId);
		TupleSet rewrites = new LocalTupleSet();
		read.stream().forEach(t -> rewrites.addAll(computedUserSet.rewrite(read, t.getObjectId())));
		return rewrites;
	}
}
