package com.strategicgains.aclaid.builder.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.TupleToUserSet;

public class TupleToUserSetBuilder
implements RewriteRuleBuilder
{
	private String relation;
	private ComputedUserSetBuilder computedUserSetBuilder;

	public TupleToUserSetBuilder(SetOperationBuilder parent, String relation, ComputedUserSetBuilder computedUserSetBuilder)
	{
		super();
		this.relation = relation;
		this.computedUserSetBuilder = computedUserSetBuilder;
	}

	@Override
	public TupleToUserSet build(RelationDefinition parent)
	{
		return new TupleToUserSet(relation, computedUserSetBuilder.build(parent));
	}

	public ComputedUserSetBuilder computedUserSet()
	{
		return new ComputedUserSetBuilder();
	}
}
