package com.strategicgains.aclaid;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.builder.Relation;
import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class NamespaceAccessControlList
{
	private String namespace;
	private AccessControlList parent;
	private Set<Relation> relations = new HashSet<>();
	private Set<Tuple> tuples = new HashSet<>();

	public NamespaceAccessControlList(AccessControlList parent, String namespace)
	{
		super();
		this.namespace = namespace;
		this.parent = parent;
	}

	public String getNamespace()
	{
		return namespace;
	}

	public void addRelation(Relation relation)
	{
		relations.add(relation);
	}

	public Collection<String> getRelationNames()
	{
		return relations.stream().map(r -> r.getName()).collect(Collectors.toList());
	}

	private boolean containsRelation(String relation)
	{
		return relations.stream().anyMatch(r -> r.getName().equals(relation))
			|| parent.containsRelation(relation);
	}

	public NamespaceAccessControlList addTuple(Tuple tuple)
	throws RelationNotRegisteredException
	{
		if (!containsRelation(tuple.getRelation())) throw new RelationNotRegisteredException(tuple.getRelation());

		tuples.add(new Tuple(tuple));
		return this;
	}

	public NamespaceAccessControlList addTuple(Resource resource, String relation, UserSet userset)
	throws RelationNotRegisteredException
	{
		return addTuple(new Tuple(resource, relation, userset));
	}

	public boolean check(AccessControlList acl, String userset, String relation, String resource)
	throws ParseException
	{
		return check(acl, UserSet.parse(userset), relation, Resource.parse(resource));
	}

	public boolean check(AccessControlList acl, UserSet userset, String relation, Resource resource)
	{
		for (Tuple tuple : tuples)
		{
			if (tuple.matches(userset, relation, resource))
			{
				return true;
			}

			if (tuple.getUserset().hasRelation() && tuple.applies(resource, relation))
			{
				//TODO: beware the recursion stack overflow!
				if (acl.check(userset, tuple.getUserset().getRelation(), tuple.getUserset())) return true;
			}
		}

		return false;
	}
}
