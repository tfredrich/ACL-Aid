package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.rewrite.expression.UsersetExpression;

/**
 * In Zanzibar, Userset rewrite rules are defined per relation in a namespace.
 * 
 * Each rule specifies a function that takes an object ID as input and outputs a userset expression tree.
 * 
 * Userset rewrite rules are translated to boolean expressions as part of check evaluation.
 *
 * For example, the following rule specifies that the userset for a relation is the intersection
 * of the "owner" and "editor" relations unioned with the "viewer" relation:
 * 
 * union(
 *     intersection(
 *         relation(inputObj, "owner"),
 *         relation(inputObj, "editor")
 *     ),
 *     relation(inputObj, "viewer")
 * )
 *
 * Outputs:
 *        +--union--+
 *       /           \
 *      /             \
 *     /               \
 * intersection      viewer
 *   /       \
 * owner    editor
 *
 * 
 * union {
 *    child { _this {} }
 *    child { computed_userset { relation: "owner" } }
 * }
 * 
 * Outputs:
 *      union
 *     /     \
 * _this     computed_userset
 *                    \
 *                relation: owner
 * 
 * union {
 *   child { _this {} }
 *   child { computed_userset { relation: "editor" } }
 *   child { tuple_to_userset {
 *     tupleset { relation: "parent" }
 *     computed_userset {
 *       object: $TUPLE_USERSET_OBJECT  # parent folder
 *       relation: "viewer"
 *     }
 *  }}
 * }
 * 
 * Outputs:
 *      +---------------+----------union---------------+
 *     /               /                                \
 * _this     computed_userset           +-----------tuple_to_userset---------+
 *                    /                /                                      \
 *           relation: editor     tupleset                             computed_userset
 *                                    /                                /               \
 *                             relation: parent     object: $TUPLE_USERSET_OBJECT     relation: viewer
 */
public interface RewriteRule
{
	/**
	 * Checks if this node is a leaf node.
	 *
	 * @return true if this node is a leaf node, false otherwise.
	 */
	default boolean isLeaf()
	{
		return false;
	}

	UsersetExpression rewrite(ObjectId objectId);
}
