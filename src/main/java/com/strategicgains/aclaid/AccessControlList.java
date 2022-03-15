package com.strategicgains.aclaid;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.strategicgains.aclaid.builder.Relation;
import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class AccessControlList
{
	private String namespace;
	private Map<String, Relation> relations = new HashMap<>();
	private Set<Tuple> tuples = new HashSet<>();

	public AccessControlList(String namespace)
	{
		super();
		this.namespace = namespace;
	}

	public String getNamespace()
	{
		return namespace;
	}

	public boolean containsRelation(String relation)
	{
		return relations.containsKey(relation);
	}

	public void addRelation(Relation relation)
	{
		relations.put(relation.getName(), relation);
	}

	public AccessControlList addTuple(Tuple tuple)
	throws RelationNotRegisteredException
	{
		if (!containsRelation(tuple.getRelation())) throw new RelationNotRegisteredException(tuple.getRelation());

		tuples.add(new Tuple(tuple));
		return this;
	}

	public AccessControlList addTuple(Resource resource, String relation, UserSet userset)
	throws RelationNotRegisteredException
	{
		return addTuple(new Tuple(resource, relation, userset));
	}

	public boolean check(String userset, String relation, String resource)
	throws ParseException
	{
		return check(UserSet.parse(userset), relation, Resource.parse(resource));
	}

	public boolean check(UserSet userset, String relation, Resource resource)
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
				if (check(userset, tuple.getUserset().getRelation(), tuple.getUserset())) return true;
			}
		}

		return false;
	}
}
