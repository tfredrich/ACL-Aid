package com.strategicgains.aclaid.domain.rewrite.expression;

import java.util.Set;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

public class ThisExpression
implements RewriteExpression
{
	private RelationDefinition relation;

	public ThisExpression(RelationDefinition relation)
	{
		super();
		this.relation = relation;
	}

	@Override
	public Set<UserSet> rewrite(TupleSet tuples, ObjectId objectId)
	{
		return tuples.read(relation.getName(), objectId).userSets().collect(Collectors.toSet());
	}
}
