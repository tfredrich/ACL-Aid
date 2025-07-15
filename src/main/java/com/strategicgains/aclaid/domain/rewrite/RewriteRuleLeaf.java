package com.strategicgains.aclaid.domain.rewrite;

/**
 * A RewriteRuleLeaf is a terminal node in the rewrite rule tree.
 * It does not have any children and represents a final computation or value.
 * 
 * @see RewriteRule
 */
public interface RewriteRuleLeaf
extends RewriteRule
{
	/**
	 * Checks if this node is a leaf node.
	 *
	 * @return true if this node is a leaf node, false otherwise.
	 */
	@Override
	default boolean isLeaf()
	{
		return true;
	}
}
