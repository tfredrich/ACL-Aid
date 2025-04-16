package com.strategicgains.aclaid.domain.rewrite.runtime;

import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

/**
 * Userset rewrite rules are also translated to boolean expressions as part of check evaluation.
 */
public interface UsersetExpression
{
	/**
	 * Evaluates the expression against the given tuple set.
	 *
	 * @param tuples The tuple set to evaluate against.
	 * @return true if the expression evaluates to true for the given tuple set,
	 *         false otherwise.
	 */
	boolean evaluate(TupleSet tuples, UserSet userset);
}
