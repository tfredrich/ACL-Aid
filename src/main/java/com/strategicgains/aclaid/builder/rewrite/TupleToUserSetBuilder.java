package com.strategicgains.aclaid.builder.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.TupleToUserSet;

public class TupleToUserSetBuilder
implements RewriteRuleBuilder
{
	private SetOperationBuilder parent;
	private String relation;
	private ComputedUserSetBuilder computedUserSetBuilder;

	public TupleToUserSetBuilder(SetOperationBuilder parent, String relation, ComputedUserSetBuilder computedUserSetBuilder)
	{
		super();
		this.parent = parent;
		this.relation = relation;
		this.computedUserSetBuilder = computedUserSetBuilder;
	}

	@Override
	public TupleToUserSet build(RelationDefinition parent)
	{
		return new TupleToUserSet(parent, relation, computedUserSetBuilder.build(parent));
	}

	public RewriteRuleBuilder end()
	{
		return parent.end();
	}

	public ComputedUserSetBuilder computedUserSet()
	{
		this.computedUserSetBuilder = new ComputedUserSetBuilder(parent);
		return computedUserSetBuilder;
	}
}
