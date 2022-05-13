package com.strategicgains.aclaid.policy;

import java.util.UUID;

import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.ResourceName;

public class TransientResource
implements Resource
{
	private String namespace;
	private String type;
	private UUID id;

	public TransientResource(String namespace, String type, UUID id)
	{
		super();
		this.namespace = namespace;
		this.type = type;
		this.id = id;
	}

	@Override
	public ResourceName getResourceName()
	{
		return new ResourceName(namespace, type, id.toString());
	}

	@Override
	public String getResourceType()
	{
		return type;
	}

	@Override
	public String getNamespace()
	{
		return namespace;
	}
}
