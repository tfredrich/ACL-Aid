package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.runtime.ThisExpression;
import com.strategicgains.aclaid.domain.rewrite.runtime.UsersetExpression;

public class This
implements RewriteRule
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
