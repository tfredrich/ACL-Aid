package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.NamespaceConfiguration;

public class RulesSetRewriteRule
implements UsersetRewriteRule
{
	private RuleSet rules;

	public RulesSetRewriteRule(RuleSet rules)
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
