package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.TupleSet;

public class This
implements RewriteRule
{
	public This()
	{
		super();
	}

	@Override
	public TupleSet rewrite(TupleSet relationTuples, String parentRelation, ResourceName objectId)
	{
		TupleSet ts = relationTuples.read(parentRelation, objectId);

		return (ts != null) ? ts : LocalTupleSet.EMPTY_SET;
	}
}
