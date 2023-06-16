package com.strategicgains.aclaid;

import java.util.HashMap;
import java.util.Map;

import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class Namespace
{
	private String name;
	private Map<String, RelationDefinition> relations = new HashMap<>();

	public Namespace(String name)
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
		return (String.format("namespace: %s", getName()));
	}

	public RelationDefinition relation(String parent)
	{
		return relations.get(parent);
	}

	public TupleSet rewrite(TupleSet tuples, Tuple tupleKey)
	{
		TupleSet copy = tuples.copy();
		relations.values().forEach(relation -> copy.addAll(relation.rewrite(tuples, tupleKey)));
		return copy;
	}
}
