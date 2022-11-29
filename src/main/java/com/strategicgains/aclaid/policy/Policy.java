/*
    Copyright 2018, Strategic Gains, Inc.
*/
package com.strategicgains.aclaid.policy;

import java.util.ArrayList;
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
{
	private List<Statement> statements = new ArrayList<>();

	public Policy()
	{
		super();
	}

	/**
	 * Add a statement to this policy.
	 * 
	 * @param statement
	 * @return
	 */
	public Policy add(Statement statement)
	{
		statements.add(statement);
		return this;
	}

	/**
	 * Merge another policy into this policy.
	 * 
	 * @param that
	 */
	public void merge(Policy that)
	{
		if (that == null) return;

		statements.addAll(that.statements);
	}

	/**
	 * Evaluate the policy with the given context for the permission.
	 * 
	 * @param context
	 * @param relation
	 * @return
	 */
	public boolean evaluate(PolicyContext context, String relation)
	{
		return stream().anyMatch(s -> s.evaluate(context, relation));
	}

	/**
	 * Factory method to create a new empty statement within the Policy.
	 * 
	 * @return a new empty Policy Statement.
	 */
	public Statement statement()
	{
		Statement stmt = new Statement();
		add(stmt);
		return stmt; 
	}

	private Stream<Statement> stream()
	{
		return statements.stream();
	}
}
