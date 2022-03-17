package com.strategicgains.aclaid;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import com.strategicgains.aclaid.builder.Relation;
import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class NamespaceConfiguration
{
	private AccessControlList rootAcl;
	private Set<Relation> relations = new HashSet<>();
	private Set<Tuple> tuples = new HashSet<>();

	public NamespaceConfiguration(AccessControlList parent)
	{
		super();
		this.rootAcl = parent;
	}

	public void addRelation(Relation relation)
	{
		relations.add(relation);
	}

	public boolean containsRelation(String relation)
	{
		return relations.stream().anyMatch(r -> r.getName().equals(relation));
	}

	public NamespaceConfiguration addTuple(String resource, String relation, String userset)
	throws ParseException, RelationNotRegisteredException
	{
		return addTuple(new Tuple(resource, relation, userset));
	}

	public NamespaceConfiguration addTuple(Resource resource, String relation, UserSet userset)
	throws RelationNotRegisteredException
	{
		return addTuple(new Tuple(resource, relation, userset));
	}

	public NamespaceConfiguration addTuple(Tuple tuple)
	throws RelationNotRegisteredException
	{
		if (!rootAcl.containsRelation(tuple.getRelation())) throw new RelationNotRegisteredException(tuple.getRelation());

		tuples.add(new Tuple(tuple));
		return this;
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