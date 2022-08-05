/*
    Copyright 2018, Strategic Gains, Inc.
*/
package com.strategicgains.aclaid.policy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;


/**
 * A policy is a collection of PolicyStatements (predicates really), such that if any of the statements,
 * at evaluation time, match the condition, the entire policy evaluates to true. Otherwise, the policy evaluates
 * to false.
 * 
 * @author toddf
 * @since Nov 2, 2018
 */
public class Policy
implements Iterable<PolicyStatement>
{
	private List<PolicyStatement> statements = new ArrayList<>();

	public Policy()
	{
		super();
	}

	@Override
	public Iterator<PolicyStatement> iterator()
	{
		return statements.iterator();
	}

	public Stream<PolicyStatement> stream()
	{
		return statements.stream();
	}

	public Policy add(PolicyStatement statement)
	{
		statements.add(statement);
		return this;
	}

	public void merge(Policy that)
	{
		if (that == null) return;

		statements.addAll(that.statements);
	}

	/**
	 * Evaluate the policy with the given context for the permission.
	 * 
	 * @param context
	 * @param relations
	 * @return
	 */
	public boolean evaluate(PolicyContext context, String relations)
	{
		return stream().anyMatch(s -> s.evaluate(context, relations));
	}

	/**
	 * Factory method to create a new empty statement within the Policy.
	 * 
	 * @return a new empty Policy Statement.
	 */
	public PolicyStatement statement()
	{
		PolicyStatement stmt = new PolicyStatement();
		add(stmt);
		return stmt; 
	}
}
