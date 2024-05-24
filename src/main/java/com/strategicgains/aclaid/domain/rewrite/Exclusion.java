package com.strategicgains.aclaid.domain.rewrite;

import java.util.Set;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;
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
	public TupleSet evaluate(TupleSet left, TupleSet right)
	{
		return exclusion(left, right);
	}

	private TupleSet exclusion(TupleSet left, TupleSet right)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UsersetExpression rewrite(ObjectId inputObj)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
