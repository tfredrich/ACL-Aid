package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;

public abstract class AbstractRewritable
implements Rewritable
{
	private RelationDefinition parent;

	private AbstractRewritable()
	{
		// Prevents empty instances.
		super();
	}

	protected AbstractRewritable(RelationDefinition parent)
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
