package com.strategicgains.aclaid.domain.rewrite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.rewrite.expression.UnionPredicate;
import com.strategicgains.aclaid.domain.rewrite.expression.UsersetExpression;

/**
 * It takes two (or more) sets as input and returns a new set as output.
 * The returned set contains all elements that are members of all input sets.
 * Duplicate elements that are in both sets will only appear once in the union.
 * The union of A and B is equal to the union of B and A. So union is commutative.
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
implements RewriteRule
{
	private List<RewriteRule> children = new ArrayList<>();

	public Union()
	{
		super();
	}

	public Union(List<RewriteRule> children)
	{
		this();
		setChildren(children);
	}

	public Union addChild(RewriteRule child)
	{
		this.children.add(child);
		return this;
	}

	public Stream<RewriteRule> children()
	{
		return children.stream();
	}

	public void setChildren(List<RewriteRule> children)
	{
		if (children == null && !this.children.isEmpty())
		{
			this.children.clear();
			return;
		}

		this.children = new ArrayList<>(children);
	}

	@Override
	public UsersetExpression rewrite(ObjectId objectId)
	{
		return new UnionPredicate(children().map(child -> child.rewrite(objectId)).toList());
	}
}
