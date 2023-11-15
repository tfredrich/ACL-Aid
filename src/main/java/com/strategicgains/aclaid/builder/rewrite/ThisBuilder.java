package com.strategicgains.aclaid.builder.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.RewriteRule;
import com.strategicgains.aclaid.domain.rewrite.This;

public class ThisBuilder
implements RewriteRuleBuilder
{
	private SetOperationBuilder parent;

	protected ThisBuilder(SetOperationBuilder parent)
	{
		super();
		this.parent = parent;
	}

	@Override
	public RewriteRule build(RelationDefinition relation)
	{
		return new This(relation);
	}

	public ComputedUserSetBuilder computedUserSet()
	{
		return parent.computedUserSet();
	}

	public TupleToUserSetBuilder tupleToUserSet()
	{
		return parent.tupleToUserSet();
	}
}
