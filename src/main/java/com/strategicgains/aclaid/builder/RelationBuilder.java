package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.domain.Relation;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class RelationBuilder
{
	private String name;
	private NamespaceConfigurationBuilder parent;
	private UnionBuilder union;
	private IntersectionBuilder intersection;
	private ExclusionBuilder exclusion;

	public RelationBuilder(String relation, NamespaceConfigurationBuilder aclBuilder)
	{
		super();
		this.name = relation;
		this.parent = aclBuilder;
	}

	public String getName()
	{
		return name;
	}

	public RelationBuilder relation(String relation)
	{
		parent.relation(relation);
		return this;
	}

	public NamespaceConfigurationBuilder tuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException
	{
		return parent.tuple(userset, relation, resource);
	}

	public NamespaceConfigurationBuilder namespace(String namespace)
	{
		return parent.namespace(namespace);
	}

	Relation build()
	{
		return new Relation(name);
	}

	public UnionBuilder union()
	{
		union = new UnionBuilder(this);
		return union;
	}

	public IntersectionBuilder intersection()
	{
		intersection = new IntersectionBuilder(this);
		return intersection;
	}

	public ExclusionBuilder exclusion()
	{
		exclusion = new ExclusionBuilder(this);
		return exclusion;
	}
}
