package com.strategicgains.aclaid.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;

public class TupleBuilder
extends AbstractChildBuildable<ObjectDefinitionBuilder>
{
	private List<Tuple> tuples = new ArrayList<>();
	private Tuple workingTuple;

	public TupleBuilder()
	{
		super(null);
	}

	public TupleBuilder(ObjectDefinitionBuilder parent)
	{
		super(parent);
	}

	/**
	 * Begins building a new Tuple (or several) for the provided resource.
	 * 
	 * @param resource
	 * @return
	 * @throws ParseException
	 * @throws InvalidTupleException
	 */
	public TupleBuilder forResource(String resource)
	throws ParseException, InvalidTupleException
	{
		workingTuple = new Tuple();
		tuples.add(workingTuple);
		return withResource(resource);
	}

	/**
	 * Begins building a new Tuple (or several) for the provided userset.
	 * 
	 * @param userset
	 * @return
	 * @throws ParseException
	 * @throws InvalidTupleException
	 */
	public TupleBuilder forUserset(String userset)
	throws ParseException, InvalidTupleException
	{
		workingTuple = new Tuple();
		tuples.add(workingTuple);
		return withUserset(userset);
	}

	/**
	 * If the current working tuple already has a resource name, a new
	 * clone will be created and the resource name on the new clone will
	 * be set, keeping the previous working tuple in the tuple collection.
	 * 
	 * If the current working tuple does not have a resource name it will
	 * be set.
	 * 
	 * @param resource
	 * @return
	 * @throws ParseException
	 * @throws InvalidTupleException
	 */
	public TupleBuilder withResource(String resource)
	throws ParseException, InvalidTupleException
	{
		if (workingTuple.hasObjectId())
		{
			workingTuple = cloneCurrent();
		}

		workingTuple.setObjectId(new ObjectId(resource));
		return this;
	}

	/**
	 * If the current working tuple already has a relation, a new
	 * clone will be created and the relation on the new clone will
	 * be set, keeping the previous working tuple in the tuple collection.
	 * 
	 * If the current working tuple does not have a relation it will
	 * be set.
	 * 
	 * @param relation
	 * @return
	 * @throws InvalidTupleException
	 */
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

	/**
	 * If the current working tuple already has a userset, a new
	 * clone will be created and the userset on the new clone will
	 * be set, keeping the previous working tuple in the tuple collection.
	 * 
	 * If the current working tuple does not have a userset it will
	 * be set.
	 * 
	 * @param userset
	 * @return
	 * @throws ParseException
	 * @throws InvalidTupleException
	 */
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

	/**
	 * Builds all the tuples.
	 * 
	 * @return
	 */
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
