package com.strategicgains.aclaid.builder.rewrite;

import com.strategicgains.aclaid.builder.ResourceDefinitionBuilder;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.TupleToUserSet;

public class TupleToUserSetBuilder
implements RewriteRuleBuilder
{
	private SetOperationBuilder parent;
	private ComputedUserSetBuilder computedUserSetBuilder;

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

	public RewriteRuleBuilder end()
	{
		return parent.end();
	}

	public TupleToUserSetBuilder tupleSet(String relation)
	{
		// TODO Auto-generated method stub
		return this;
	}

	public ComputedUserSetBuilder computedUserSet()
	{
		this.computedUserSetBuilder = new ComputedUserSetBuilder(this);
		return computedUserSetBuilder;
	}
}
