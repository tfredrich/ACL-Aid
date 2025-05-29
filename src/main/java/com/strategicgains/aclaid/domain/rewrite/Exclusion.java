package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleStore;
import com.strategicgains.aclaid.domain.rewrite.expression.UsersetExpression;

/**
 * The returned set contains only elements that are members of the first set but not the second set.
 * If the first set is a subset of the second set, the exclusion is an empty set.
 * It is not commutative - A.exclusion(B) does not necessarily equal B.exclusion(A).
 * The exclusion of A from B is equivalent to the intersection of B and the complement of A.
 * 
 * For example:
 *   Set A = {1, 2, 3, 4}
 *   Set B = {3, 4, 5, 6}
 *
 *   A.exclusion(B) would return:
 *   {1, 2}
 *
 *   B.exclusion(A) would return:
 *   {5, 6}
 */
public class Exclusion
extends BinaryRewriteRule
{
	public Exclusion()
	{
		super();
	}

	public Exclusion(RewriteRule left, RewriteRule right)
	{
		super(left, right);
	}

	@Override
	public TupleStore evaluate(TupleStore left, TupleStore right)
	{
		return exclusion(left, right);
	}

	private TupleStore exclusion(TupleStore left, TupleStore right)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UsersetExpression rewrite(ObjectId objectId)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
