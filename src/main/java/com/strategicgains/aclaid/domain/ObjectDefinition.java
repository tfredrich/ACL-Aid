package com.strategicgains.aclaid.domain;

import java.util.HashMap;
import java.util.Map;

public class ObjectDefinition
{
	private String name;
	private Map<String, RelationDefinition> relationsByName = new HashMap<>();

	public ObjectDefinition(String name)
	{
		super();
		this.name = name;
	}

	public void addRelation(RelationDefinition relation)
	{
		relationsByName.put(relation.getName(), relation);
	}

	public boolean containsRelation(String relation)
	{
		return relationsByName.containsKey(relation);
	}

	public RelationDefinition getRelation(String relation)
	{
		return relationsByName.get(relation);
	}

	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return (String.format("Object: %s", getName()));
	}

	public boolean check(TupleStore tuples, UserSet userset, String relation, ObjectId objectId)
	{
		RelationDefinition r = relationsByName.get(relation);

		if (r != null)
		{
			return r.check(tuples, userset, objectId);
		}

		return false;
	}
}
