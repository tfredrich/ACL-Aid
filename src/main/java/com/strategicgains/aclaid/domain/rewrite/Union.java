package com.strategicgains.aclaid.domain.rewrite;

import java.util.List;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

/**
 * It takes two sets as input and returns a new set as output.
 * The returned set contains all elements that are members of either the first or second input set.
 * Duplicate elements that are in both sets will only appear once in the union.
 * The union of A and B is equal to the union of B and A. So union is commutative.
 * Union is associative - (A.union(B)).union(C) equals A.union(B.union(C))
 * The union of a set with an empty set returns the original set.
 * 
 * For example:
 *   Set A = {1, 2, 3, 4}
 *   Set B = {3, 4, 5, 6}
 *   
 * A.union(B) would return:
 * {1, 2, 3, 4, 5, 6}
 */
public class Union
extends AggregateRewriteRule
{
	public Union()
	{
		super();
	}

	public Union(List<RewriteRule> children)
	{
		super(children);
	}

	@Override
	public Union addChild(RewriteRule rewriteRule)
	{
		super.addChild(rewriteRule);
		return this;
	}

	@Override
	public boolean check(TupleSet relationTuples, UserSet user, String relation, ObjectId objectId)
	{
		return children().anyMatch(r -> r.check(relationTuples, user, relation, objectId));
	}
}
