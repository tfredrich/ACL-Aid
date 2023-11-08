package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;

public abstract class AbstractRewriteRule
implements RewriteRule
{
	private RelationDefinition parent;

	private AbstractRewriteRule()
	{
		// Prevents empty instances.
		super();
	}

	protected AbstractRewriteRule(RelationDefinition parent)
	{
		this();
		setParent(parent);
	}

	protected RelationDefinition getParent()
	{
		return parent;
	}

	protected void setParent(RelationDefinition parent)
	{
		this.parent = parent;
	}
}
