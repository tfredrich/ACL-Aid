package com.strategicgains.aclaid.policy;

import java.util.HashMap;
import java.util.Map;

import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.UserSet;

public class PolicyContext
{
	private UserSet user;
	private Resource resource;
	private Map<String, Object> meta;

	public PolicyContext(UserSet user, Resource resource)
	{
		super();
		this.user = user;
		this.resource = resource;
	}

	public PolicyContext add(String key, Object value)
	{
		if (!hasMeta())
		{
			meta = new HashMap<>();
		}

		meta.put(key, value);
		return this;
	}

	public boolean hasMeta()
	{
		return (meta != null);
	}

	public Object get(String key)
	{
		return (hasMeta() ? meta.get(key) : null);
	}

	public boolean hasUser()
	{
		return (user != null);
	}

	public UserSet getUser()
	{
		return user;
	}

	public Resource getResource()
	{
		return resource;
	}

	public ResourceName getResourceName()
	{
		return resource.getResourceName();
	}
}
