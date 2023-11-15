package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.TupleSet;

public class This
extends AbstractRewriteRule
{
	public This(RelationDefinition relation)
	{
		super(relation);
	}

	@Override
	public TupleSet rewrite(TupleSet input, ResourceName resource)
	{
		return input.read(getParent().getName(), resource);
	}
}
