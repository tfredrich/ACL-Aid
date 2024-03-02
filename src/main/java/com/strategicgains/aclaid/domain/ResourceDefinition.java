package com.strategicgains.aclaid.domain;

import java.util.HashMap;
import java.util.Map;

public class ResourceDefinition
{
	private String name;
	private Map<String, RelationDefinition> relations = new HashMap<>();

	public ResourceDefinition(String name)
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

	public TupleSet rewrite(TupleSet tuples, ResourceName objectId)
	{
		TupleSet copy = tuples.copy();
		relations.values().forEach(relation -> copy.addAll(relation.rewrite(tuples, objectId)));
		return copy;
	}
}
