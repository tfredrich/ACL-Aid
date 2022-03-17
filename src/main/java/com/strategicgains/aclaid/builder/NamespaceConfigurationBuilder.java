package com.strategicgains.aclaid.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.AccessControlList;
import com.strategicgains.aclaid.NamespaceConfiguration;
import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class NamespaceConfigurationBuilder
{
	private AccessControlListBuilder parent;
	private String namespace;
	private Set<RelationBuilder> builders = new HashSet<>();
	private List<Tuple> tuples = new ArrayList<>();
	private Tuple workingTuple;

	public NamespaceConfigurationBuilder(AccessControlListBuilder parent, String namespace)
	{
		super();
		this.namespace = namespace;
		this.parent = parent;
	}

	public NamespaceConfigurationBuilder forResource(String resource)
	throws ParseException, InvalidTupleException
	{
		workingTuple = new Tuple();
		tuples.add(workingTuple);
		return withResource(resource);
	}

	public NamespaceConfigurationBuilder forUserset(String userset)
	throws ParseException, InvalidTupleException
	{
		workingTuple = new Tuple();
		tuples.add(workingTuple);
		return withUserset(userset);
	}

	public NamespaceConfigurationBuilder withResource(String resource)
	throws ParseException, InvalidTupleException
	{
		if (workingTuple.hasResource())
		{
			workingTuple = cloneCurrent();
		}

		workingTuple.setResource(Resource.parse(resource));
		return this;
	}

	public NamespaceConfigurationBuilder withRelation(String relation)
	throws InvalidTupleException
	{
		if (workingTuple.hasRelation())
		{
			workingTuple = cloneCurrent();
		}

		workingTuple.setRelation(relation);
		return this;
	}

	public NamespaceConfigurationBuilder withUserset(String userset)
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

	public NamespaceConfiguration buildRelations(AccessControlList parent)
	{
		NamespaceConfiguration acl = parent.namespace(namespace);
		builders.stream().forEach(r -> acl.addRelation(r.build()));
		return acl;
	}

	public NamespaceConfiguration buildTuples(AccessControlList parent)
	{
		NamespaceConfiguration acl = parent.namespace(namespace);
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

	public NamespaceConfigurationBuilder namespace(String namespace)
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

	public NamespaceConfigurationBuilder tuple(String resource, String relation, String userset)
	throws ParseException, RelationNotRegisteredException
	{
		return tuple(Resource.parse(resource), relation, UserSet.parse(userset));
	}

	public NamespaceConfigurationBuilder tuple(Resource resource, String relation, UserSet userset)
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

	public NamespaceConfigurationBuilder tuple(String tuple)
	throws ParseException
	{
		tuples.add(Tuple.parse(tuple));
		return this;
	}
}
