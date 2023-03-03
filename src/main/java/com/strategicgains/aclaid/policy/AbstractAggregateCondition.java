/*
    Copyright 2016-2018, Strategic Gains, Inc.
*/
package com.strategicgains.aclaid.policy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * An assertion that contains multiple conditions.
 * 
 * @author tfredrich
 * @since Mar 3, 2023
 */
public abstract class AbstractAggregateCondition
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
	public AbstractAggregateCondition add(Condition condition)
	{
		if (!conditions.contains(condition))
		{
			conditions.add(condition);
		}

		return this;
	}
}
