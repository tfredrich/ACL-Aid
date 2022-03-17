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

public class NamespaceAclBuilder
{
	private AclBuilder parent;
	private String namespace;
	private Set<RelationBuilder> builders = new HashSet<>();
	private List<Tuple> tuples = new ArrayList<>();
	private Tuple tuple;

	public NamespaceAclBuilder(AclBuilder parent, String namespace)
	{
		super();
		this.namespace = namespace;
		this.parent = parent;
	}

	public NamespaceAclBuilder forResource(String resource)
	throws ParseException, InvalidTupleException
	{
		tuple = new Tuple();
		tuples.add(tuple);
		return withResource(resource);
	}

	public NamespaceAclBuilder forUserset(String userset)
	throws ParseException, InvalidTupleException
	{
		tuple = new Tuple();
		tuples.add(tuple);
		return withUserset(userset);
	}

	public NamespaceAclBuilder withResource(String resource)
	throws ParseException, InvalidTupleException
	{
		if (tuple.hasResource())
		{
			tuple = cloneCurrent();
		}

		tuple.setResource(Resource.parse(resource));
		return this;
	}

	public NamespaceAclBuilder withRelation(String relation)
	throws InvalidTupleException
	{
		if (tuple.hasRelation())
		{
			tuple = cloneCurrent();
		}

		tuple.setRelation(relation);
		return this;
	}

	public NamespaceAclBuilder withUserset(String userset)
	throws ParseException, InvalidTupleException
	{
		if (tuple.hasUserset())
		{
			tuple = cloneCurrent();
		}

		tuple.setUserset(UserSet.parse(userset));
		return this;
	}

	private Tuple cloneCurrent()
	throws InvalidTupleException
	{
		if (!tuple.isValid()) throw new InvalidTupleException(tuple.toString());

		Tuple t = new Tuple(tuple);
		tuples.add(t);
		return t;
	}

	public NamespaceAccessControlList build(AccessControlList parent)
	{
		NamespaceAccessControlList acl = new NamespaceAccessControlList(parent, namespace);
		builders.stream().forEach(r -> acl.addRelation(r.build()));
		tuples.stream().forEach(t -> {
			try
			{
				acl.addTuple(t);
			}
			catch (RelationNotRegisteredException e)
			{
				e.printStackTrace();
			}
		});
		return acl;
	}

	public NamespaceAclBuilder namespace(String namespace)
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

	public NamespaceAclBuilder tuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException
	{
		return tuple(Resource.parse(resource), relation, UserSet.parse(userset));
	}

	public NamespaceAclBuilder tuple(Resource resource, String relation, UserSet userset)
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

	public NamespaceAclBuilder tuple(String tuple)
	throws ParseException
	{
		tuples.add(Tuple.parse(tuple));
		return this;
	}

	public class RelationBuilder
	{
		private String name;
		private NamespaceAclBuilder parent;
		private UnionBuilder union;
		private IntersectionBuilder intersection;
		private ExclusionBuilder exclusion;

		public RelationBuilder(String relation, NamespaceAclBuilder aclBuilder)
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

		public NamespaceAclBuilder tuple(String userset, String relation, String resource)
		throws ParseException, RelationNotRegisteredException
		{
			return parent.tuple(userset, relation, resource);
		}

		public NamespaceAclBuilder namespace(String namespace)
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
