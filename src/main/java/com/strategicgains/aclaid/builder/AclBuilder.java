package com.strategicgains.aclaid.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.strategicgains.aclaid.AccessControlList;
import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class AclBuilder
{
	private String namespace;
	private Set<RelationBuilder> relationBuilders = new HashSet<>();
	private Set<String> registeredRelations = new HashSet<>();
	private List<Tuple> tuples = new ArrayList<>();
	private Tuple current;

	public AclBuilder(String namespace)
	{
		super();
		this.namespace = namespace;
	}

	public AclBuilder forResource(String qrn)
	throws ParseException, InvalidTupleException
	{
		current = new Tuple();
		tuples.add(current);
		return withResource(qrn);
	}

	public AclBuilder forUserset(String qrn)
	throws ParseException, InvalidTupleException
	{
		current = new Tuple();
		tuples.add(current);
		return withUserset(qrn);
	}

	public AclBuilder withResource(String qrn)
	throws ParseException, InvalidTupleException
	{
		if (current.hasResource())
		{
			current = cloneCurrent();
		}

		current.setResource(Resource.parse(qrn));
		return this;
	}

	public AclBuilder withRelation(String relation)
	throws InvalidTupleException
	{
		if (current.hasRelation())
		{
			current = cloneCurrent();
		}

		current.setRelation(relation);
		return this;
	}

	public AclBuilder withUserset(String qrn)
	throws ParseException, InvalidTupleException
	{
		if (current.hasUserset())
		{
			current = cloneCurrent();
		}

		current.setUserset(UserSet.parse(qrn));
		return this;
	}

	private Tuple cloneCurrent()
	throws InvalidTupleException
	{
		if (!current.isValid()) throw new InvalidTupleException(current.toString());

		Tuple g = new Tuple(current);
		tuples.add(g);
		return g;
	}

	public AccessControlList build()
	{
		AccessControlList m = new AccessControlList(namespace);
		relationBuilders.stream().forEach(r -> m.addRelation(r.build()));
		tuples.stream().forEach(t -> {
			try
			{
				m.addTuple(t);
			} catch (RelationNotRegisteredException e)
			{
				e.printStackTrace();
			}
		});
		return m;
	}

	public RelationBuilder relation(String relation)
	{
		RelationBuilder rb = new RelationBuilder(relation, this);
		relationBuilders.add(rb);
		registeredRelations.add(relation);
		return rb;
	}

	public AclBuilder tuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException
	{
		return tuple(Resource.parse(resource), relation, UserSet.parse(userset));
	}

	public AclBuilder tuple(Resource resource, String relation, UserSet userset)
	throws RelationNotRegisteredException
	{
		if (!registeredRelations.contains(relation)) throw new RelationNotRegisteredException(relation);

		tuples.add(new Tuple(resource, relation, userset));
		return this;
	}

	public AclBuilder tupe(String tuple)
	throws ParseException
	{
		tuples.add(Tuple.parse(tuple));
		return this;
	}

	public class RelationBuilder
	{
		private String name;
		private AclBuilder parent;
		private UnionBuilder union;
		private IntersectionBuilder intersection;
		private ExclusionBuilder exclusion;

		public RelationBuilder(String relation, AclBuilder aclBuilder)
		{
			super();
			this.name = relation;
			this.parent = aclBuilder;
		}

		public RelationBuilder relation(String relation)
		{
			parent.relation(relation);
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

		public ExclusionBuilder exclusion()
		{
			exclusion = new ExclusionBuilder(this);
			return exclusion;
		}
	}
}
