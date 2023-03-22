package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.NamespaceConfiguration;

public class UsersetRewriteRuleImpl
implements UsersetRewriteRule
{
	private TupleSet rules;

	public UsersetRewriteRuleImpl(TupleSet rules)
	{
		super();
		this.rules = rules;
	}

	@Override
	public boolean matches(NamespaceConfiguration namespace, UserSet userset, String relation, ResourceName resource)
	{
		return (rules.readOne(userset, relation, resource) != null);
	}
}
