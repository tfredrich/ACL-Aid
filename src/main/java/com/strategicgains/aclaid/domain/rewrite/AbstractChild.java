package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;

public abstract class AbstractChild
implements Child
{
	private RelationDefinition parent;

	private AbstractChild()
	{
		// Prevents empty instances.
		super();
	}

	protected AbstractChild(RelationDefinition parent)
	{
		this();
		this.parent = parent;
	}

	protected RelationDefinition getParent()
	{
		return parent;
	}
}
