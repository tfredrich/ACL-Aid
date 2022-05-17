package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class UsersetRewriteBuilder
{
	private RelationBuilder parent;
	private UnionBuilder union;
	private IntersectionBuilder intersection;
	private ExclusionBuilder exclusion;

	public UsersetRewriteBuilder(RelationBuilder parent)
	{
		super();
		this.parent = parent;
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

	public NamespaceConfigurationBuilder namespace(String namespace)
	{
		return parent.namespace(namespace);
	}

	public RelationBuilder relation(String name)
	{
		return parent.relation(name);
	}

	public NamespaceConfigurationBuilder tuple(String resource, String relation, String userset)
	throws ParseException, RelationNotRegisteredException
	{
		return parent.tuple(resource, relation, userset);
	}
}
