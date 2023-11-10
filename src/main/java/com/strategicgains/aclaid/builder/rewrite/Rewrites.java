package com.strategicgains.aclaid.builder.rewrite;

public abstract class Rewrites
{
	private Rewrites()
	{
		// prevents instantiation.
	}

	public static UnionBuilder union()
	{
		return new UnionBuilder();
	}
}
