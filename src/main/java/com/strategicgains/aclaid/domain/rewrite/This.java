package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.expression.ThisExpression;
import com.strategicgains.aclaid.domain.rewrite.expression.UsersetExpression;

/**
 * From the Zanzibar document: Returns all users from stored relation tuples for the ⟨object#relation⟩ pair, including
 * indirect ACLs referenced by usersets from the tuples. This is the default behavior when no rewrite rule is specified.
 */
public class This
implements RewriteRuleLeaf
{
	private RelationDefinition relation;

	public This(RelationDefinition relation)
	{
		super();
		this.relation = relation;
	}

	@Override
	public UsersetExpression rewrite(ObjectId objectId)
	{
		return new ThisExpression(objectId, relation.getName());
	}
}
