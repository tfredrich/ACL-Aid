package com.strategicgains.aclaid.builder;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import com.strategicgains.aclaid.domain.Relation;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class UsersetRewriteBuilder
{
	private RelationBuilder parentBuilder;
	private Set<String> parentRelations;
	private UnionBuilder union;
	private IntersectionBuilder intersection;
	private ExclusionBuilder exclusion;

	public UsersetRewriteBuilder(RelationBuilder parent)
	{
		super();
		this.parentBuilder = parent;
	}

	public UsersetRewriteBuilder childOf(String parentRelation)
	{
		if (this.parentRelations == null)
		{
			this.parentRelations = new HashSet<>();
		}

		this.parentRelations.add(parentRelation);
		return this;
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
		return parentBuilder.namespace(namespace);
	}

	public RelationBuilder relation(String name)
	{
		return parentBuilder.relation(name);
	}

	public NamespaceConfigurationBuilder tuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException
	{
		return parentBuilder.tuple(userset, relation, resource);
	}

	public void apply(Relation r)
	{
		// tuple (userset#parent, relation, resource);
//		r.addRewriteRule(new ChildOfRewriteRule());
	}
}
