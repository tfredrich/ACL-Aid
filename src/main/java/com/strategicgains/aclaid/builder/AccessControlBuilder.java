package com.strategicgains.aclaid.builder;

import java.util.HashMap;
import java.util.Map;

import com.strategicgains.aclaid.AccessControl;

public class AccessControlBuilder
{
	private Map<String, ObjectDefinitionBuilder> objectBuilders = new HashMap<>();

	public ObjectDefinitionBuilder object(String objectName)
	{
		ObjectDefinitionBuilder current = new ObjectDefinitionBuilder(this, objectName);
		objectBuilders.put(objectName, current);
		return current;
	}

	public boolean containsRelation(String relation, String objectName)
	{
		ObjectDefinitionBuilder builder = objectBuilders.get(objectName);

		if (builder == null)
		{
			return false;
		}

		return builder.containsRelation(relation);
	}

	public AccessControl build()
	{
		AccessControl acl = new AccessControl();
		objectBuilders.values().stream().forEach(b -> b.build(acl));
		return acl;
	}
}
