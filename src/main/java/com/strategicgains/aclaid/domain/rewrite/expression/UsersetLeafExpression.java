package com.strategicgains.aclaid.domain.rewrite.expression;

/**
 * A marker interface for leaf nodes in an expression tree. Leaf nodes are the terminal nodes that do not have any
 * children. They represent nodes in the expression that read tuples from the data source.
 */
public interface UsersetLeafExpression
extends UsersetExpression
{
	/**
	 * Checks if this node is a leaf node.
	 *
	 * @return true if this node is a leaf node, false otherwise.
	 */
	default boolean isLeaf()
	{
		return true;
	}
}
