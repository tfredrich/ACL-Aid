package com.strategicgains.aclaid.builder.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.RewriteRule;

public interface RewriteRuleBuilder
{
	RewriteRule build(RelationDefinition relationDefinition);
}
