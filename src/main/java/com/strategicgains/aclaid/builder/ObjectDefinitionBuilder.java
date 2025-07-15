package com.strategicgains.aclaid.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.AccessControl;
import com.strategicgains.aclaid.domain.ObjectDefinition;
import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;

public class ObjectDefinitionBuilder
implements Buildable
{
	private AccessControlBuilder aclBuilder;
	private String name;
	private Set<RelationBuilder> relationBuilders = new LinkedHashSet<>();
	private List<TupleBuilder> tupleBuilders = new ArrayList<>();
	private List<Tuple> tuples = new ArrayList<>();

	public ObjectDefinitionBuilder(AccessControlBuilder aclBuilder, String objectName)
	{
		super();
		this.name = objectName;
		this.aclBuilder = aclBuilder;
	}

	public ObjectDefinitionBuilder object(String objectName)
	{
		return aclBuilder.object(objectName);
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
	throws ParseException, InvalidTupleException
	{
		return tuple(UserSet.parse(userset), relation, new ObjectId(resource));
	}

	public ObjectDefinitionBuilder tuple(UserSet userset, String relation, ObjectId resource)
	throws InvalidTupleException
	{
		if (!containsRelation(relation, resource)) throw new InvalidTupleException(String.format("Relation '%s' not registered in object '%s': ", relation, name));

		tuples.add(new Tuple(userset, relation, resource));
		return this;
	}

	public boolean containsRelation(String relation)
	{
		return relationBuilders.stream().anyMatch(r -> r.getName().equals(relation));
	}

	private boolean containsRelation(String relation, ObjectId resource)
	{
		if (name.equals(resource.getType()))
		{
			return containsRelation(relation);
		}

		return aclBuilder.containsRelation(relation, resource.getType());
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

	public void build(AccessControl acl)
	{
		buildRelations(acl);
		buildTuples(acl);
	}

	private ObjectDefinition buildRelations(AccessControl acl)
	{
		ObjectDefinition objectDefinition = acl.object(name);
		relationBuilders.stream().forEach(r -> objectDefinition.addRelation(r.build(objectDefinition)));
		return objectDefinition;
	}

	private AccessControl buildTuples(AccessControl acl)
	{
		tupleBuilders.stream().forEach(b -> tuples.addAll(b.build()));
		tuples.stream().forEach(t -> {
			try
			{
				acl.addTuple(t);
			}
			catch (InvalidTupleException e)
			{
				e.printStackTrace();
			}
		});
		return acl;
	}
}
