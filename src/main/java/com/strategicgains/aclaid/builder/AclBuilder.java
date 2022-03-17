package com.strategicgains.aclaid.builder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.strategicgains.aclaid.AccessControlList;

public class AclBuilder
{
	private Map<String, NamespaceAclBuilder> builders = new HashMap<>();
	private Set<String> relations = new HashSet<>();

	public NamespaceAclBuilder namespace(String namespace)
	{
		NamespaceAclBuilder current = new NamespaceAclBuilder(this, namespace);
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

	public AccessControlList build()
	{
		AccessControlList acl = new AccessControlList();

		builders.values().stream().forEach(b -> acl.addNamespaceAcl(b.build(acl)));

		return acl;
	}
}
