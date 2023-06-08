package com.strategicgains.aclaid;

import java.util.HashMap;
import java.util.Map;

import com.strategicgains.aclaid.domain.Relation;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

public class Namespace
{
	private String name;
	private Map<String, Relation> relations = new HashMap<>();

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

	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return (String.format("namespace: %s", getName()));
	}

	public Relation relation(String parent)
	{
		return relations.get(parent);
	}

	public TupleSet rewrite(TupleSet tuples, UserSet userset)
	{
		TupleSet copy = tuples.copy();
		relations.forEach((n, r) -> copy.addAll(r.rewrite(tuples, userset)));
		return copy;
	}
}
