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
		return current;
	}

	public void registerRelation(String relation)
	{
		relations.add(relation);
	}

	public boolean containsRelation(String relation)
	{
		return relations.contains(relation);
	}

	public AccessControlList build()
	{
		AccessControlList acl = new AccessControlList();

		builders.values().stream().forEach(b -> acl.addAcl(b.build(acl)));

		return acl;
	}
}
