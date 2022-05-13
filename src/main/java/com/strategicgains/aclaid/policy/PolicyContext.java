package com.strategicgains.aclaid.policy;

import java.util.HashMap;
import java.util.Map;

import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.ResourceName;

public class PolicyContext
{
	private ResourceName principal;
	private Resource resource;
	private Map<String, Object> meta;

	public PolicyContext(ResourceName principal, Resource resource)
	{
		super();
		this.principal = principal;
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

	public boolean hasPrincipal()
	{
		return (principal != null);
	}

	public ResourceName getPrincipal()
	{
		return principal;
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
