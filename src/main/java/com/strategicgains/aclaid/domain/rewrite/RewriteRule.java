package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.TupleSet;

public interface RewriteRule
{
	TupleSet rewrite(TupleSet input, ResourceName objectId);
}
