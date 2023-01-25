package com.strategicgains.aclaid.builder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.strategicgains.aclaid.AccessControl;

public class AccessControlListBuilder
{
	private Map<String, NamespaceConfigurationBuilder> builders = new HashMap<>();
	private Set<String> relations = new HashSet<>();

	public NamespaceConfigurationBuilder namespace(String namespace)
	{
		NamespaceConfigurationBuilder current = new NamespaceConfigurationBuilder(this, namespace);
		builders.put(namespace, current);
		relations.clear();
		return current;
	}

	public boolean containsRelation(String relation)
	{
		if (relations.isEmpty())
		{
			builders.values().stream().forEach(b -> relations.addAll(b.getRelationNames()));
		}

		return relations.contains(relation);
	}

	public AccessControl build()
	{
		AccessControl acl = new AccessControl();

		builders.values().stream().forEach(b -> b.buildRelations(acl));
		builders.values().stream().forEach(b -> b.buildTuples(acl));

		return acl;
	}
}
