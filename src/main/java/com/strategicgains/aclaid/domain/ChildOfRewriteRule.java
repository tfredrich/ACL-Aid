package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.exception.InvalidTupleException;

public class ChildOfRewriteRule
implements RewriteRule
{
	private String childOf;

	public ChildOfRewriteRule(String relation)
	{
		super();
		this.childOf = relation;
	}

	@Override
	public Tuple rewrite(UserSet userset, String relation, ResourceName resource)
	{
		try
		{
			return new Tuple(userset, childOf, resource);
		}
		catch (InvalidTupleException e)
		{
			return null;
		}
	}
}
