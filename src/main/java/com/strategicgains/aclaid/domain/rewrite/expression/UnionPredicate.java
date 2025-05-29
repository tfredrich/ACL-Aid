package com.strategicgains.aclaid.domain.rewrite.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.strategicgains.aclaid.domain.TupleStore;
import com.strategicgains.aclaid.domain.UserSet;

/**
 * It takes two sets as input and returns a new set as output.
 * The returned set contains all elements that are members of either the first or second input set.
 * Duplicate elements that are in both sets will only appear once in the union.
 * The union of A and B is equal to the union of B and A. So union is commutative.
 * UnionPredicate is associative - (A.union(B)).union(C) equals A.union(B.union(C))
 * The union of a set with an empty set returns the original set.
 * 
 * For example:
 *   Set A = {1, 2, 3, 4}
 *   Set B = {3, 4, 5, 6}
 *   
 * A.union(B) would return:
 * {1, 2, 3, 4, 5, 6}
 */
public class UnionPredicate
implements UsersetExpression
{
	private List<UsersetExpression> children = new ArrayList<>();

	public UnionPredicate()
	{
		super();
	}

	public UnionPredicate(List<UsersetExpression> children)
	{
		this();
		setChildren(children);
	}

	public UnionPredicate addChild(UsersetExpression child)
	{
		this.children.add(child);
		return this;
	}

	public Stream<UsersetExpression> children()
	{
		return children.stream();
	}

	public void setChildren(List<UsersetExpression> children)
	{
		if (children == null && !this.children.isEmpty())
		{
			this.children.clear();
			return;
		}

		this.children = new ArrayList<>(children);
	}

	@Override
	public boolean evaluate(TupleStore tuples, UserSet userSet) {
		return children().anyMatch(child -> child.evaluate(tuples, userSet));
	}
}
