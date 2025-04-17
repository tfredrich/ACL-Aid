package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.predicate.ThisPredicate;
import com.strategicgains.aclaid.domain.rewrite.predicate.UsersetPredicate;

/**
 * From the Zanzibar document: 
 * Returns all users from stored relation tuples for the ⟨object#relation⟩
 * pair, including indirect ACLs referenced by usersets from the tuples.
 * This is the default behavior when no rewrite rule is specified.
 */
public class This implements RewriteRule {
	private RelationDefinition relation;

	public This(RelationDefinition relation) {
		super();
		this.relation = relation;
	}

	@Override
	public UsersetPredicate rewrite(ObjectId objectId) {
		return new ThisPredicate(objectId, relation.getName());
	}
}
