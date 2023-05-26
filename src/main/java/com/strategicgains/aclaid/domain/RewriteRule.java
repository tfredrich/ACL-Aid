package com.strategicgains.aclaid.domain;

public interface RewriteRule
{
	Tuple rewrite(UserSet userset, String relation, ResourceName resource);
}
