package com.strategicgains.aclaid;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.UserSet;

public class AccessControlList
{
	private Map<String, NamespaceConfiguration> namespaces = new HashMap<>();

	/**
	 * Get an existing NamespaceConfiguration by name or create a new, empty one.
	 * Changes to the instance make changes to this AccessControlList.
	 * 
	 * @param namespace
	 * @return an existing or new, empty NamespaceConfiguration instance.
	 */
	public NamespaceConfiguration namespace(String namespace)
	{
		return namespaces.computeIfAbsent(namespace, n -> new NamespaceConfiguration(this));
	}

	/**
	 * Check to see if the given relation name is defined in any of the namespaces.
	 * 
	 * @param relation
	 * @return
	 */
	public boolean containsRelation(String relation)
	{
		return namespaces.values().stream().anyMatch(n -> n.containsRelation(relation));
	}

	/**
	 * Make an authorization check.
	 * 
	 * @param userset
	 * @param relation
	 * @param resource
	 * @return
	 * @throws ParseException
	 */
	public boolean check(String userset, String relation, String resource)
	throws ParseException
	{
		return check(UserSet.parse(userset), relation, Resource.parse(resource));
	}

	/**
	 * Make an authorization check.
	 * 
	 * @param userset
	 * @param relation
	 * @param resource
	 * @return
	 */
	public boolean check(UserSet userset, String relation, Resource resource)
	{
		NamespaceConfiguration acl = namespaces.get(resource.getNamespace());

		if (acl == null) return false;

		return acl.check(this, userset, relation, resource);
	}
}
