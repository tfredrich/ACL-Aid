package com.strategicgains.aclaid.domain.rewrite;

import java.util.Set;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

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
	// TODO: According to the Zanzibar paper, TupleSet is not the right return type. It should be UserSet, depending on the definition of 'userset' in this context.
	//TupleSet rewrite(TupleSet tuples, ObjectId objectId);

	// TODO: Even a Set<UserSet> is not the right return type. It should be a single UserSet if one believes the paper verbatim.
	Set<UserSet> rewrite(TupleSet tuples, ObjectId objectId);
}
