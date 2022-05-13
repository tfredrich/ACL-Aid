package com.strategicgains.aclaid.policy;

import com.strategicgains.aclaid.domain.ResourceName;

public class TenancyCondition
implements Condition
{
	private ResourceName admin;
	
	public TenancyCondition(ResourceName admin)
	{
		super();
		this.admin = admin;
	}

	@Override
	public boolean test(PolicyContext context, PolicyStatement statement, String relation)
	{
		return true;
//		return admin.matches(context.getPrincipal());
	}
}
