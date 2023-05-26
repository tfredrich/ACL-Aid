package com.strategicgains.aclaid;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.strategicgains.aclaid.domain.Relation;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

public class Namespace
{
	private String name;
	private Map<String, Relation> relations = new HashMap<>();
	private TupleSet tuples;

	public Namespace(String name)
	{
		super();
		this.name = name;
	}

	public void addRelation(Relation relation)
	{
		relations.put(relation.getName(), relation);
	}

	public boolean containsRelation(String relation)
	{
		return relations.containsKey(relation);
	}

	public boolean check(String userset, String relation, String resource)
	throws ParseException
	{
		return check(UserSet.parse(userset), relation, new ResourceName(resource));
	}

	public boolean check(UserSet userset, String relation, ResourceName resource)
	{
		TupleSet rewrites = rewrite(userset, relation, resource);

		if (rewrites != null && (rewrites.readOne(relation, relation, relation) != null)) return true;

		if (hasTuples())
		{
			return (tuples.readOne(userset, relation, resource) != null);
		}

		return false;
	}

	private TupleSet rewrite(UserSet userset, String relation, ResourceName resource)
	{
		return relations.values().stream().flatMap(r -> r.rewrite(userset, relation, resource));
	}

	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return (String.format("namespace: %s", getName()));
	}

	private boolean hasTuples()
	{
		return (tuples != null);
	}

	public Relation relation(String parent)
	{
		return relations.get(parent);
	}
}
