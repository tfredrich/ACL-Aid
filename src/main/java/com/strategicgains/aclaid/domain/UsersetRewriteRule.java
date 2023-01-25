package com.strategicgains.aclaid.domain;

import java.util.Set;

public interface UsersetRewriteRule
{
	UserSetExpression rewrite(Resource resource);
}
