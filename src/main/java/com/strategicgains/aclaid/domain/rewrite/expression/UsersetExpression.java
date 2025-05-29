package com.strategicgains.aclaid.domain.rewrite.expression;

import com.strategicgains.aclaid.domain.TupleStore;
import com.strategicgains.aclaid.domain.UserSet;

/**
 * Userset rewrite rules are also translated to boolean expressions as part of check evaluation.
 */
public interface UsersetExpression
{
	/**
	 * Evaluates the expression against the given tuple set for the given user.
	 *
	 * @param tuples The tuple set to evaluate against.
	 * @param userset The user to evaluate against the tuples.
	 * @return true if the expression evaluates to true for the given tuple set,
	 *         false otherwise.
	 */
	boolean evaluate(TupleStore tuples, UserSet userset);
}
