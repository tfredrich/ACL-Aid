package com.strategicgains.aclaid.domain.rewrite;

import java.util.List;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

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
	public Union(RelationDefinition parent)
	{
		super(parent);
	}

	public Union(RelationDefinition parent, List<RewriteRule> expressions)
	{
		super(parent, expressions);
	}

	@Override
	public Union child(RewriteRule rewriteRule)
	{
		super.child(rewriteRule);
		return this;
	}

	@Override
	public TupleSet rewrite(TupleSet input, Tuple key)
	{
		TupleSet rewrites = new LocalTupleSet();
		operands()
			.map(r -> r.rewrite(input, key))
			.forEach(rewrites::addAll);
		return rewrites;
	}
}
