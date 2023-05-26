package com.strategicgains.aclaid.builder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.strategicgains.aclaid.AccessControl;

public class AccessControlBuilder
{
	private Map<String, NamespaceBuilder> buildersByNamespace = new HashMap<>();
	private Set<String> relations = new HashSet<>();

	public NamespaceBuilder namespace(String namespace)
	{
		NamespaceBuilder current = new NamespaceBuilder(this, namespace);
		buildersByNamespace.put(namespace, current);
		relations.clear();
		return current;
	}

	public boolean containsRelation(String relation)
	{
		if (relations.isEmpty())
		{
			buildersByNamespace.values().stream().forEach(b -> relations.addAll(b.getRelationNames()));
		}

		return relations.contains(relation);
	}

	public AccessControl build()
	{
		AccessControl acl = new AccessControl();

		buildersByNamespace.values().stream().forEach(b -> b.buildRelations(acl));
		buildersByNamespace.values().stream().forEach(b -> b.buildTuples(acl));

		return acl;
	}
}
