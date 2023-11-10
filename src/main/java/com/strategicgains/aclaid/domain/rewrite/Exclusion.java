package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.TupleSet;

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
	public Exclusion(RelationDefinition parent)
	{
		super(parent);
	}

	public Exclusion(RelationDefinition parent, RewriteRule left, RewriteRule right)
	{
		super(parent, left, right);
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
}
