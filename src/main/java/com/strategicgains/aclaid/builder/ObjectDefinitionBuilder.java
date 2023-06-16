package com.strategicgains.aclaid.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.AccessControl;
import com.strategicgains.aclaid.ObjectDefinition;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class ObjectDefinitionBuilder
implements Buildable
{
	private AccessControlBuilder parent;
	private String name;
	private Set<RelationBuilder> relationBuilders = new HashSet<>();
	private List<TupleBuilder> tupleBuilders = new ArrayList<>();
	private List<Tuple> tuples = new ArrayList<>();

	public ObjectDefinitionBuilder(AccessControlBuilder parent, String objectName)
	{
		super();
		this.name = objectName;
		this.parent = parent;
	}

	public ObjectDefinition buildRelations(AccessControl parent)
	{
		ObjectDefinition namespaceConfiguration = parent.namespace(name);
		relationBuilders.stream().forEach(r -> namespaceConfiguration.addRelation(r.build()));
		return namespaceConfiguration;
	}

	public AccessControl buildTuples(AccessControl parent)
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

	public ObjectDefinitionBuilder object(String namespace)
	{
		return parent.object(namespace);
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

	public ObjectDefinitionBuilder tuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException, InvalidTupleException
	{
		return tuple(UserSet.parse(userset), relation, new ResourceName(resource));
	}

	public ObjectDefinitionBuilder tuple(UserSet userset, String relation, ResourceName resource)
	throws RelationNotRegisteredException, InvalidTupleException
	{
		if (!containsRelation(relation)) throw new RelationNotRegisteredException(relation);

		tuples.add(new Tuple(userset, relation, resource));
		return this;
	}

	private boolean containsRelation(String relation)
	{
		return parent.containsRelation(relation);
	}

	public ObjectDefinitionBuilder tuple(String tuple)
	throws ParseException, InvalidTupleException
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