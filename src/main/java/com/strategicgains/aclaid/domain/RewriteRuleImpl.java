package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.Namespace;

public class RewriteRuleImpl
implements RewriteRule
{
	private TupleSet rules;

	public RewriteRuleImpl(TupleSet rules)
	{
		super();
		this.rules = rules;
	}

	@Override
	public boolean rewrite(Namespace namespace, UserSet userset, String relation, ResourceName resource)
	{
		return rules.readOne(userset, relation, resource) != null;
	}
}
