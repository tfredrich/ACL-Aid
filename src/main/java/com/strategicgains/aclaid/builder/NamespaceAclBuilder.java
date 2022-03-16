package com.strategicgains.aclaid.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private Set<RelationBuilder> relationBuilders = new HashSet<>();
	private List<Tuple> tuples = new ArrayList<>();
	private Tuple current;

	public NamespaceAclBuilder(AclBuilder parent, String namespace)
	{
		super();
		this.namespace = namespace;
		this.parent = parent;
	}

	public NamespaceAclBuilder forResource(String qrn)
	throws ParseException, InvalidTupleException
	{
		current = new Tuple();
		tuples.add(current);
		return withResource(qrn);
	}

	public NamespaceAclBuilder forUserset(String qrn)
	throws ParseException, InvalidTupleException
	{
		current = new Tuple();
		tuples.add(current);
		return withUserset(qrn);
	}

	public NamespaceAclBuilder withResource(String qrn)
	throws ParseException, InvalidTupleException
	{
		if (current.hasResource())
		{
			current = cloneCurrent();
		}

		current.setResource(Resource.parse(qrn));
		return this;
	}

	public NamespaceAclBuilder withRelation(String relation)
	throws InvalidTupleException
	{
		if (current.hasRelation())
		{
			current = cloneCurrent();
		}

		current.setRelation(relation);
		parent.registerRelation(relation);
		return this;
	}

	public NamespaceAclBuilder withUserset(String qrn)
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

	public NamespaceAccessControlList build(AccessControlList parent)
	{
		NamespaceAccessControlList acl = new NamespaceAccessControlList(parent, namespace);
		relationBuilders.stream().forEach(r -> acl.addRelation(r.build()));
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

	public RelationBuilder relation(String relation)
	{
		RelationBuilder rb = new RelationBuilder(relation, this);
		relationBuilders.add(rb);
		parent.registerRelation(relation);
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
		if (!parent.containsRelation(relation)) throw new RelationNotRegisteredException(relation);

		tuples.add(new Tuple(resource, relation, userset));
		return this;
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
