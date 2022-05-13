package com.strategicgains.aclaid.builder;

import java.text.ParseException;
import java.util.Collection;

import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.policy.Condition;
import com.strategicgains.aclaid.policy.PolicyStatement;

public class PolicyStatementBuilder
{
	private PolicyStatement statement;
	private PolicyBuilder parent;

	public PolicyStatementBuilder(PolicyStatement statement, PolicyBuilder parent)
	{
		super();
		this.statement = statement;
		this.parent = parent;
	}

	public PolicyStatement userset(String userset)
	throws ParseException
	{
		return statement.setUserset(userset);
	}

	public PolicyStatement userset(UserSet userset)
	{
		return statement.setUserset(userset);
	}

	public PolicyStatement resource(String... resourceQrns)
	throws ParseException
	{
		return statement.setResource(resourceQrns);
	}

	public PolicyStatement resources(Collection<ResourceName> resourceQrns)
	{
		return statement.setResources(resourceQrns);
	}

	public PolicyStatement allow(String... relations)
	{
		return statement.allow(relations);
	}

	public PolicyStatement allow(Collection<String> relations)
	{
		return statement.allow(relations);
	}

	public PolicyStatement deny(String... relations)
	{
		return statement.deny(relations);
	}

	public PolicyStatement deny(Collection<String> relations)
	{
		return statement.deny(relations);
	}

	public PolicyStatement withCondition(Condition condition)
	{
		return statement.withCondition(condition);
	}

	public PolicyStatementBuilder statement()
	{
		return parent.statement();
	}
}
