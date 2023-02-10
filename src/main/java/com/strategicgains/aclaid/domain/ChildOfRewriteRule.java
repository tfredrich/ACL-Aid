package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.NamespaceConfiguration;

public class ChildOfRewriteRule
implements UsersetRewriteRule
{
	private String parent;

	public ChildOfRewriteRule(String relation)
	{
		super();
		this.parent = relation;
	}

	@Override
	public boolean matches(NamespaceConfiguration namespace, UserSet userset, String relation, ResourceName resource)
	{
		return namespace.relation(parent).checkUsersetRewrites(userset, relation, resource);
	}
}
