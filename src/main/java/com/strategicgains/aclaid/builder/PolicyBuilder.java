package com.strategicgains.aclaid.builder;

import com.strategicgains.aclaid.policy.Policy;

public class PolicyBuilder
{
	private RelationBuilder parent;
	private Policy policy;

	public PolicyBuilder(RelationBuilder relationBuilder)
	{
		super();
		this.parent = relationBuilder;
	}

	public RelationBuilder relation(String name)
	{
		return parent.relation(name);
	}

	/**
	 * Factory method to create a new empty statement within the Policy.
	 * 
	 * @return a new empty Policy Statement.
	 */
	public PolicyStatementBuilder statement()
	{
		return new PolicyStatementBuilder(policy.statement(), this);
	}

	public Policy build()
	{
		return policy;
	}
}
