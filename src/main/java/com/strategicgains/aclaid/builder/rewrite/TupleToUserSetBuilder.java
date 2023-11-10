package com.strategicgains.aclaid.builder.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.RewriteRule;
import com.strategicgains.aclaid.domain.rewrite.TupleToUserSet;

public class TupleToUserSetBuilder
implements RewriteRuleBuilder
{
	private SetOperationBuilder parent;

	public TupleToUserSetBuilder(SetOperationBuilder parent)
	{
		super();
		this.parent = parent;
	}

	@Override
	public TupleToUserSet build(RelationDefinition relation)
	{
		return new TupleToUserSet(relation);
	}
}
