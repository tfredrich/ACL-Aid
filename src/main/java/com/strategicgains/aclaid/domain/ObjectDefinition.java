package com.strategicgains.aclaid.domain;

import java.util.HashMap;
import java.util.Map;

public class ObjectDefinition
{
	private String name;
	private Map<String, RelationDefinition> relations = new HashMap<>();

	public ObjectDefinition(String name)
	{
		super();
		this.name = name;
	}

	public void addRelation(RelationDefinition relation)
	{
		relations.put(relation.getName(), relation);
	}

	public boolean containsRelation(String relation)
	{
		return relations.containsKey(relation);
	}

	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return (String.format("resource: %s", getName()));
	}

	public RelationDefinition relation(String relation)
	{
		return relations.get(relation);
	}

	public boolean check(TupleSet tuples, UserSet userset, String relation, ObjectId objectId)
	{
		RelationDefinition r = relations.get(relation);

		if (r != null)
		{
			return r.check(tuples, userset, objectId);
		}

		return false;
	}
}
