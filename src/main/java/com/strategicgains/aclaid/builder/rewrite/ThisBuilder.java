package com.strategicgains.aclaid.builder.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.RewriteRule;
import com.strategicgains.aclaid.domain.rewrite.This;

public class ThisBuilder
implements RewriteRuleBuilder
{
	protected ThisBuilder()
	{
		super();
	}

	@Override
	public RewriteRule build(RelationDefinition relation)
	{
		return new This(relation);
	}
}
