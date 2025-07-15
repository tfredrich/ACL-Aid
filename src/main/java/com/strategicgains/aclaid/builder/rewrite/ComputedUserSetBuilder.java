package com.strategicgains.aclaid.builder.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.ComputedUserSet;

public class ComputedUserSetBuilder
implements RewriteRuleBuilder
{
	private String relation;
	private String resourceName;

	public ComputedUserSetBuilder()
	{
		super();
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
		RelationDefinition relationDefinition = parent;
	
		if (!parent.getName().equals(relation))
		{
			relationDefinition = parent.getSibling(relation);
		}

		return new ComputedUserSet(relationDefinition, resourceName);
	}
}
