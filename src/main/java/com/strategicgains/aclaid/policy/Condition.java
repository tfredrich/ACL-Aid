/*
    Copyright 2016-2018, Strategic Gains, Inc.
*/
package com.strategicgains.aclaid.policy;

/**
 * @author toddf
 * @since Feb 24, 2016
 */
public interface Condition
{
	boolean test(PolicyContext context, PolicyStatement statement, String relation);
}
