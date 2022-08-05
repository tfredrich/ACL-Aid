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
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class NamespaceConfigurationBuilder
implements Buildable
{
	private AccessControlListBuilder parent;
	private String namespace;
	private Set<RelationBuilder> relationBuilders = new HashSet<>();
	private List<TupleBuilder> tupleBuilders = new ArrayList<>();
	private List<Tuple> tuples = new ArrayList<>();

	public NamespaceConfigurationBuilder(AccessControlListBuilder parent, String namespace)
	{
		super();
		this.namespace = namespace;
		this.parent = parent;
	}

	public NamespaceConfiguration buildRelations(AccessControlList parent)
	{
		NamespaceConfiguration acl = parent.namespace(namespace);
		relationBuilders.stream().forEach(r -> acl.addRelation(r.build()));
		return acl;
	}

	public AccessControlList buildTuples(AccessControlList parent)
	{
		tupleBuilders.stream().forEach(b -> tuples.addAll(b.build()));
		tuples.stream().forEach(t -> {
			try
			{
				parent.addTuple(t);
			}
			catch (RelationNotRegisteredException e)
			{
				e.printStackTrace();
			}
		});
		return parent;
	}

	public NamespaceConfigurationBuilder namespace(String namespace)
	{
		return parent.namespace(namespace);
	}

	public Collection<String> getRelationNames()
	{
		return relationBuilders.stream().map(RelationBuilder::getName).collect(Collectors.toList());
	}

	public RelationBuilder relation(String relation)
	{
		RelationBuilder rb = new RelationBuilder(relation, this);
		relationBuilders.add(rb);
		return rb;
	}

	public NamespaceConfigurationBuilder tuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException
	{
		return tuple(UserSet.parse(userset), relation, ResourceName.parse(resource));
	}

	public NamespaceConfigurationBuilder tuple(UserSet userset, String relation, ResourceName resource)
	throws RelationNotRegisteredException
	{
		if (!containsRelation(relation)) throw new RelationNotRegisteredException(relation);

		tuples.add(new Tuple(userset, relation, resource));
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

	public TupleBuilder tuples()
	{
		TupleBuilder tb = new TupleBuilder(this);
		tupleBuilders.add(tb);
		return tb;
	}
}
