package com.strategicgains.aclaid.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;

public class TupleBuilder
extends AbstractChildBuildable<ResourceDefinitionBuilder>
{
	private List<Tuple> tuples = new ArrayList<>();
	private Tuple workingTuple;

	public TupleBuilder(ResourceDefinitionBuilder parent)
	{
		super(parent);
	}

	public TupleBuilder forResource(String resource)
	throws ParseException, InvalidTupleException
	{
		workingTuple = new Tuple();
		tuples.add(workingTuple);
		return withResource(resource);
	}

	public TupleBuilder forUserset(String userset)
	throws ParseException, InvalidTupleException
	{
		workingTuple = new Tuple();
		tuples.add(workingTuple);
		return withUserset(userset);
	}

	public TupleBuilder withResource(String resource)
	throws ParseException, InvalidTupleException
	{
		if (workingTuple.hasResource())
		{
			workingTuple = cloneCurrent();
		}

		workingTuple.setResource(new ResourceName(resource));
		return this;
	}

	public TupleBuilder withRelation(String relation)
	throws InvalidTupleException
	{
		if (workingTuple.hasRelation())
		{
			workingTuple = cloneCurrent();
		}

		workingTuple.setRelation(relation);
		return this;
	}

	public TupleBuilder withUserset(String userset)
	throws ParseException, InvalidTupleException
	{
		if (workingTuple.hasUserset())
		{
			workingTuple = cloneCurrent();
		}

		workingTuple.setUserset(UserSet.parse(userset));
		return this;
	}

	public List<Tuple> build()
	{
		return tuples;
	}
	
	private Tuple cloneCurrent()
	throws InvalidTupleException
	{
		if (!workingTuple.isValid()) throw new InvalidTupleException(workingTuple.toString());

		Tuple t = new Tuple(workingTuple);
		tuples.add(t);
		return t;
	}

}
