package com.strategicgains.aclaid.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.AccessControlList;
import com.strategicgains.aclaid.NamespaceAccessControlList;
import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class NamespaceBuilder
{
	private AccessControlListBuilder parent;
	private String namespace;
	private Set<RelationBuilder> builders = new HashSet<>();
	private List<Tuple> tuples = new ArrayList<>();
	private Tuple workingTuple;

	public NamespaceBuilder(AccessControlListBuilder parent, String namespace)
	{
		super();
		this.namespace = namespace;
		this.parent = parent;
	}

	public NamespaceBuilder forResource(String resource)
	throws ParseException, InvalidTupleException
	{
		workingTuple = new Tuple();
		tuples.add(workingTuple);
		return withResource(resource);
	}

	public NamespaceBuilder forUserset(String userset)
	throws ParseException, InvalidTupleException
	{
		workingTuple = new Tuple();
		tuples.add(workingTuple);
		return withUserset(userset);
	}

	public NamespaceBuilder withResource(String resource)
	throws ParseException, InvalidTupleException
	{
		if (workingTuple.hasResource())
		{
			workingTuple = cloneCurrent();
		}

		workingTuple.setResource(Resource.parse(resource));
		return this;
	}

	public NamespaceBuilder withRelation(String relation)
	throws InvalidTupleException
	{
		if (workingTuple.hasRelation())
		{
			workingTuple = cloneCurrent();
		}

		workingTuple.setRelation(relation);
		return this;
	}

	public NamespaceBuilder withUserset(String userset)
	throws ParseException, InvalidTupleException
	{
		if (workingTuple.hasUserset())
		{
			workingTuple = cloneCurrent();
		}

		workingTuple.setUserset(UserSet.parse(userset));
		return this;
	}

	private Tuple cloneCurrent()
	throws InvalidTupleException
	{
		if (!workingTuple.isValid()) throw new InvalidTupleException(workingTuple.toString());

		Tuple t = new Tuple(workingTuple);
		tuples.add(t);
		return t;
	}

	public NamespaceAccessControlList buildRelations(AccessControlList parent)
	{
		NamespaceAccessControlList acl = parent.namespace(namespace);
		builders.stream().forEach(r -> acl.addRelation(r.build()));
		return acl;
	}

	public NamespaceAccessControlList buildTuples(AccessControlList parent)
	{
		NamespaceAccessControlList acl = parent.namespace(namespace);
		tuples.stream().forEach(t -> {
			try
			{
				acl.tuple(t);
			}
			catch (RelationNotRegisteredException e)
			{
				e.printStackTrace();
			}
		});
		return acl;
	}

	public NamespaceBuilder namespace(String namespace)
	{
		return parent.namespace(namespace);
	}

	public Collection<String> getRelationNames()
	{
		return builders.stream().map(rb -> rb.getName()).collect(Collectors.toList());
	}

	public RelationBuilder relation(String relation)
	{
		RelationBuilder rb = new RelationBuilder(relation, this);
		builders.add(rb);
		return rb;
	}

	public NamespaceBuilder tuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException
	{
		return tuple(Resource.parse(resource), relation, UserSet.parse(userset));
	}

	public NamespaceBuilder tuple(Resource resource, String relation, UserSet userset)
	throws RelationNotRegisteredException
	{
		if (!containsRelation(relation)) throw new RelationNotRegisteredException(relation);

		tuples.add(new Tuple(resource, relation, userset));
		return this;
	}

	private boolean containsRelation(String relation)
	{
		return parent.containsRelation(relation);
	}

	public NamespaceBuilder tuple(String tuple)
	throws ParseException
	{
		tuples.add(Tuple.parse(tuple));
		return this;
	}

	public class RelationBuilder
	{
		private String name;
		private NamespaceBuilder parent;
		private UnionBuilder union;
		private IntersectionBuilder intersection;
		private ExclusionBuilder exclusion;

		public RelationBuilder(String relation, NamespaceBuilder aclBuilder)
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

		public NamespaceBuilder tuple(String userset, String relation, String resource)
		throws ParseException, RelationNotRegisteredException
		{
			return parent.tuple(userset, relation, resource);
		}

		public NamespaceBuilder namespace(String namespace)
		{
			return parent.namespace(namespace);
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
