package com.strategicgains.aclaid;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.UserSet;

public class AccessControlList
{
	private Map<String, NamespaceConfiguration> namespaces = new HashMap<>();

	public NamespaceConfiguration namespace(String namespace)
	{
		return namespaces.computeIfAbsent(namespace, n -> new NamespaceConfiguration(this));
	}

	public boolean containsRelation(String relation)
	{
		return namespaces.values().stream().anyMatch(n -> n.containsRelation(relation));
	}

	public boolean check(String userset, String relation, String resource)
	throws ParseException
	{
		return check(UserSet.parse(userset), relation, Resource.parse(resource));
	}

	public boolean check(UserSet userset, String relation, Resource resource)
	{
		NamespaceConfiguration acl = namespaces.get(resource.getNamespace());

		if (acl == null) return false;

		return acl.check(this, userset, relation, resource);
	}
}
