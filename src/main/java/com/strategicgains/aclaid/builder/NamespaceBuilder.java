package com.strategicgains.aclaid.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NamespaceBuilder
{
	private String name;
	private Set<Relation> relations = new HashSet<>();

	public NamespaceBuilder(String name)
	{
		super();
		this.name = name;
	}

	public RelationBuilder relation(String relation)
	{
		return new RelationBuilder(relation, this);
	}

	public void addRelation(Relation relation)
	{
		relations.add(relation);
	}

	public class Relation
	{
		private String name;

		public Relation(String relation)
		{
			super();
			this.name = relation;
		}
	}

	public class RelationBuilder
	{
		private String name;
		private NamespaceBuilder parent;
		private UnionBuilder union;
		private IntersectionBuilder intersection;
		private ExclusionBuilder exclusion;

		public RelationBuilder(String relation, NamespaceBuilder namespaceBuilder)
		{
			super();
			this.name = relation;
			this.parent = namespaceBuilder;
		}

		public RelationBuilder relation(String relation)
		{
			Relation r = build();
			parent.addRelation(r);
			this.name = relation;
			return this;
		}

		private Relation build()
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
	}

	public class UnionBuilder
	{
		private RelationBuilder parent;
		private boolean thisUnion;
		private Set<String> computedUsersets = new HashSet<>();

		public UnionBuilder(RelationBuilder parent)
		{
			super();
			this.parent = parent;
		}

		public UnionBuilder _this()
		{
			thisUnion = true;
			return this;
		}

		public UnionBuilder computedUserset(String relation)
		{
			computedUsersets.add(relation);
			return this;
		}

		public RelationBuilder relation(String name)
		{
			return parent.relation(name);
		}
	}

	public class IntersectionBuilder
	{
		private RelationBuilder parent;

		public IntersectionBuilder(RelationBuilder parent)
		{
			super();
			this.parent = parent;
		}

		public RelationBuilder relation(String name)
		{
			return parent.relation(name);
		}		
	}

	public class ExclusionBuilder
	{
		private RelationBuilder parent;

		public ExclusionBuilder(RelationBuilder parent)
		{
			super();
			this.parent = parent;
		}

		public RelationBuilder relation(String name)
		{
			return parent.relation(name);
		}		
	}
}
