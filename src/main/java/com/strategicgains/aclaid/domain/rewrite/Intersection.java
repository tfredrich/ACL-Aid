package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleStore;
import com.strategicgains.aclaid.domain.rewrite.expression.UsersetExpression;

/**
 * The returned set contains only elements that are members of both input sets.
 * If there are no common elements, the intersection is an empty set.
 * It is commutative - A.intersection(B) equals B.intersection(A).
 * It is associative - (A.intersection(B)).intersection(C) equals A.intersection(B.intersection(C)).
 * 
 * For example:
 *   Set A = {1, 2, 3, 4}
 *   Set B = {3, 4, 5, 6}
 *
 *   A.intersection(B) would return:
 *   {3, 4}
 */
public class Intersection
extends BinaryRewriteRule
{
	public Intersection()
	{
		super();
	}

	public Intersection(RewriteRule left, RewriteRule right)
	{
		super(left, right);
	}

	@Override
	public TupleStore evaluate(TupleStore left, TupleStore right)
	{
		return intersection(left, right);
	}

	private TupleStore intersection(TupleStore left, TupleStore right)
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
