package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.TupleSet;

public class TupleToUserSet
extends AbstractRewriteRule
{
	public TupleToUserSet(RelationDefinition relation)
	{
		super(relation);
	}

	@Override
	public TupleSet rewrite(TupleSet input, ResourceName key)
	{
		return LocalTupleSet.EMPTY;
	}
}
