package com.strategicgains.aclaid.builder.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.ComputedUserSet;

public class ComputedUserSetBuilder
implements RewriteRuleBuilder
{
	private SetOperationBuilder parent;
	private String relation;
	private String resourceName;

	public ComputedUserSetBuilder(SetOperationBuilder parent)
	{
		super();
		this.parent = parent;
	}

	public ComputedUserSetBuilder relation(String relation)
	{
		this.relation = relation;
		return this;
	}

	public ComputedUserSetBuilder resource(String resourceName)
	{
		this.resourceName = resourceName;
		return this;
	}

	@Override
	public ComputedUserSet build(RelationDefinition parent)
	{
		return new ComputedUserSet(resourceName, relation);
	}

	public ThisBuilder _this()
	{
		return parent._this();
	}

	public TupleToUserSetBuilder tupleToUserSet(String relation, ComputedUserSetBuilder computedUserSetBuilder)
	{
		return parent.tupleToUserSet(relation, computedUserSetBuilder);
	}

	public RewriteRuleBuilder end()
	{
		return parent.end();
	}
}
