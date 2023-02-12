package com.strategicgains.aclaid;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.strategicgains.aclaid.domain.Relation;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

public class NamespaceConfiguration
{
	private String name;
	private AccessControl accessControl;
	private Map<String, Relation> relations = new HashMap<>();
	private TupleSet tuples;

	public NamespaceConfiguration(AccessControl parent, String name)
	{
		super();
		this.accessControl = parent;
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
		if (checkUsersetRewrites(userset, relation, resource)) return true;

		if (hasTuples())
		{
			return tuples.readOne(userset, relation, resource) != null;
		}

		return false;
	}

	private boolean checkUsersetRewrites(UserSet userset, String relation, ResourceName resource)
	{
		return relations.values().stream().anyMatch(r -> r.checkUsersetRewrites(userset, relation, resource));
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
