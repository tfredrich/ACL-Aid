package com.strategicgains.aclaid.policy;

import com.strategicgains.aclaid.builder.RelationBuilder;

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
