package com.strategicgains.aclaid.builder.rewrite;

public abstract class Rewrites
{
	private Rewrites()
	{
		// prevents instantiation.
	}

	public static SetOperationBuilder union()
	{
		return new UnionBuilder();
	}
}
