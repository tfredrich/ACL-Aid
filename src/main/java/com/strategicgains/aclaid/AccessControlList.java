package com.strategicgains.aclaid;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.strategicgains.aclaid.builder.Relation;
import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.UserSet;

public class AccessControlList
{
	private Map<String, NamespaceAccessControlList> acls = new HashMap<>();
	private Set<String> relations = new HashSet<>();

	public boolean containsNamespace(String namespace)
	{
		return acls.containsKey(namespace);
	}

	public boolean containsRelation(String relation)
	{
		return relations.contains(relation);
	}

	public void addRelation(Relation relation)
	{
		relations.add(relation.getName());
	}

	public void addAcl(NamespaceAccessControlList acl)
	{
		acls.put(acl.getNamespace(), acl);
	}

	public boolean check(String userset, String relation, String resource)
	throws ParseException
	{
		return check(UserSet.parse(userset), relation, Resource.parse(resource));
	}

	public boolean check(UserSet userset, String relation, Resource resource)
	{
		NamespaceAccessControlList acl = acls.get(resource.getNamespace());

		if (acl == null) return false;

		return acl.check(this, userset, relation, resource);
	}
}
