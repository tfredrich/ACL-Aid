package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.TupleSet;

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
	public Intersection(RelationDefinition parent)
	{
		super(parent);
	}

	public Intersection(RelationDefinition parent, RewriteRule left, RewriteRule right)
	{
		super(parent, left, right);
	}

	@Override
	public TupleSet evaluate(TupleSet left, TupleSet right)
	{
		return intersection(left, right);
	}

	private TupleSet intersection(TupleSet left, TupleSet right)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
