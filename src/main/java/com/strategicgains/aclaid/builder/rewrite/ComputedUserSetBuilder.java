package com.strategicgains.aclaid.builder.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.ComputedUserSet;
import com.strategicgains.aclaid.domain.rewrite.RewriteRule;

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

	@Override
	public RewriteRule build(RelationDefinition parent)
	{
		return new ComputedUserSet(parent, relation);
	}

	public ThisBuilder _this()
	{
		return parent._this();
	}

	public TupleToUserSetBuilder tupleToUserSet()
	{
		return parent.tupleToUserSet();
	}

	public ComputedUserSetBuilder resource(String resourceName)
	{
		this.resourceName = resourceName;
		return this;
	}
}
