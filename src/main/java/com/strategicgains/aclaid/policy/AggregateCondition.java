/*
    Copyright 2016-2018, Strategic Gains, Inc.
*/
package com.strategicgains.aclaid.policy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * An assertion that contains multiple conditions where any of the conditions are true, returns true.
 * 
 * @author toddf
 * @since Mar 8, 2016
 */
public class AggregateCondition
implements Condition
{
	private List<Condition> conditions = new ArrayList<>();

	public Stream<Condition> stream()
	{
		return conditions.stream();
	}

	/**
	 * Add a condition to this aggregate. Will not add the same condition (by reference) more-than once.
	 * 
	 * @param condition
	 * @return this instance for method chaining.
	 */
	public AggregateCondition add(Condition condition)
	{
		if (!conditions.contains(condition))
		{
			conditions.add(condition);
		}

		return this;
	}

	/**
	 * Tests the aggregated conditions, returning true on the first condition that returns true. Otherwise, returns false.
	 * 
	 * @param context
	 * @param statement
	 * @param relation
	 */
	@Override
	public boolean test(PolicyContext context, PolicyStatement statement, String relation)
	{
		return conditions.stream().anyMatch(c -> c.test(context, statement, relation));
	}
}
