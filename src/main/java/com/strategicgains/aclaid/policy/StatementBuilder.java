package com.strategicgains.aclaid.policy;

import java.text.ParseException;
import java.util.Collection;

import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.UserSet;

public class StatementBuilder
{
	private Statement statement;
	private PolicyBuilder parent;

	public StatementBuilder(Statement statement, PolicyBuilder parent)
	{
		super();
		this.statement = statement;
		this.parent = parent;
	}

	public Statement userset(String userset)
	throws ParseException
	{
		return statement.setUserset(userset);
	}

	public Statement userset(UserSet userset)
	{
		return statement.setUserset(userset);
	}

	public Statement resource(String... resourceQrns)
	throws ParseException
	{
		return statement.setResource(resourceQrns);
	}

	public Statement resources(Collection<ResourceName> resourceQrns)
	{
		return statement.setResources(resourceQrns);
	}

	public Statement allow(String... relations)
	{
		return statement.allow(relations);
	}

	public Statement allow(Collection<String> relations)
	{
		return statement.allow(relations);
	}

	public Statement deny(String... relations)
	{
		return statement.deny(relations);
	}

	public Statement deny(Collection<String> relations)
	{
		return statement.deny(relations);
	}

	public Statement withCondition(Condition condition)
	{
		return statement.withCondition(condition);
	}

	public StatementBuilder statement()
	{
		return parent.statement();
	}
}
