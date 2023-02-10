package com.strategicgains.aclaid.builder;

import com.strategicgains.aclaid.NamespaceConfiguration;
import com.strategicgains.aclaid.domain.Relation;
import com.strategicgains.aclaid.policy.PolicyBuilder;

public class RelationBuilder
extends AbstractChildBuildable
{
	private String name;
	private UsersetRewriteBuilder rewrites;
	private PolicyBuilder policy;

	public RelationBuilder(String relation, NamespaceConfigurationBuilder parent)
	{
		super(parent);
		this.name = relation;
	}

	public String getName()
	{
		return name;
	}

	public PolicyBuilder policy()
	{
		this.policy = new PolicyBuilder(this);
		return policy;
	}

	public UsersetRewriteBuilder usersetRewrite()
	{
		this.rewrites = new UsersetRewriteBuilder(this);
		return this.rewrites;
	}

	public String toString()
	{
		return (String.format("Relation: %s", name));
	}

	Relation build(NamespaceConfiguration namespace)
	{
		Relation r = new Relation(namespace, name);
		if (hasRewrites()) rewrites.apply(r);
		if (hasPolicy()) policy.apply(r);
		return r;
	}

	private boolean hasRewrites()
	{
		return (rewrites != null);
	}

	private boolean hasPolicy()
	{
		return (policy != null);
	}
}
