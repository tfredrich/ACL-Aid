package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class TupleToUsersetBuilder
{
	private UnionBuilder parent;

	public TupleToUsersetBuilder(UnionBuilder parent)
	{
		super();
		this.parent = parent;
	}

	public TupleToUsersetBuilder tupleSet(String relation)
	{
		// TODO Auto-generated method stub
		return this;
	}

	public TupleToUsersetBuilder computedUserset(UserSets tupleUsersetObject, String relation)
	{
		// TODO Auto-generated method stub
		return this;
	}

	public RelationBuilder relation(String relation)
	{
		return parent.relation(relation);
	}

	public NamespaceConfigurationBuilder namespace(String namespace)
	{
		return parent.namespace(namespace);
	}

	public NamespaceConfigurationBuilder tuple(String resource, String relation, String userset)
	throws ParseException, RelationNotRegisteredException
	{
		return parent.tuple(resource, relation, userset);
	}
}
