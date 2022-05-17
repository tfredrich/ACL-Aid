package com.strategicgains.aclaid.builder;

import com.strategicgains.aclaid.domain.Relation;

public class RelationBuilder
extends AbstractChildBuildable
{
	private String name;
	private UsersetRewriteBuilder rewriteBuilder;
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

	Relation build()
	{
		return new Relation(name);
	}

	public PolicyBuilder policy()
	{
		this.policy = new PolicyBuilder(this);
		return policy;
	}

	public UsersetRewriteBuilder usersetRewrite()
	{
		this.rewriteBuilder = new UsersetRewriteBuilder(this);
		return rewriteBuilder;
	}
}
