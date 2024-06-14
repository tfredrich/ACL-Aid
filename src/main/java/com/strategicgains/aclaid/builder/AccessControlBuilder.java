package com.strategicgains.aclaid.builder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.strategicgains.aclaid.AccessControl;

public class AccessControlBuilder
{
	private Map<String, ObjectDefinitionBuilder> objectBuilders = new HashMap<>();
	private Set<String> relations = new HashSet<>();

	public ObjectDefinitionBuilder object(String objectName)
	{
		ObjectDefinitionBuilder current = new ObjectDefinitionBuilder(this, objectName);
		objectBuilders.put(objectName, current);
		relations.clear();
		return current;
	}

	public boolean containsRelation(String relation)
	{
		if (relations.isEmpty())
		{
			objectBuilders.values().stream().forEach(b -> relations.addAll(b.getRelationNames()));
		}

		return relations.contains(relation);
	}

	public AccessControl build()
	{
		AccessControl acl = new AccessControl();

		objectBuilders.values().stream().forEach(b -> b.buildRelations(acl));
		objectBuilders.values().stream().forEach(b -> b.buildTuples(acl));

		return acl;
	}
}
