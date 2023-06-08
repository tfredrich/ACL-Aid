package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;

public interface IComputedUserSet
{
	UserSet compute(Tuple t);
}
