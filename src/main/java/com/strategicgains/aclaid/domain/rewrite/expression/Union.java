package com.strategicgains.aclaid.domain.rewrite.expression;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
extends AggregateExpression
{
	public Union()
	{
		super();
	}

	public Union(List<UsersetExpression> children)
	{
		super(children);
	}

	@Override
	public Union addChild(UsersetExpression rewriteRule)
	{
		super.addChild(rewriteRule);
		return this;
	}

	@Override
	public Set<UserSet> evaluate(TupleSet relationTuples)
	{
		return children().flatMap(child -> child.evaluate(relationTuples).stream()).collect(Collectors.toSet());
	}
}
