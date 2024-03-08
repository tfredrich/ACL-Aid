package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.TupleSet;

public class This
extends AbstractRewriteRule
{
	public This(RelationDefinition parent)
	{
		super(parent);
	}

	@Override
	public TupleSet rewrite(TupleSet relationTuples, ResourceName objectId)
	{
		TupleSet ts = relationTuples.read(getParentRelationDefinition().getName(), objectId);

		return (ts != null) ? ts : LocalTupleSet.EMPTY_SET;
	}
}
