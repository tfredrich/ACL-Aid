/*
    Copyright 2016-2018, Strategic Gains, Inc.
*/
package com.strategicgains.aclaid.policy;

/**
 * An assertion that contains multiple conditions where any of the conditions are true, returns true.
 * 
 * @author toddf
 * @since Mar 8, 2016
 */
public class AnyOfCondition
extends AbstractAggregateCondition
{
	/**
	 * Tests the aggregated conditions, returning true on the first condition that returns true. Otherwise, returns false.
	 * 
	 * @param context
	 * @param statement
	 * @param relation
	 */
	@Override
	public boolean test(PolicyContext context, Statement statement, String relation)
	{
		return stream().anyMatch(c -> c.test(context, statement, relation));
	}
}
