package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;

public abstract class AbstractChildBuildable<T extends Buildable>
implements Buildable
{
	private T parent;

	protected AbstractChildBuildable(T parent)
	{
		super();
		this.parent = parent;
	}

	protected T getParent()
	{
		return parent;
	}

	@Override
	public ObjectDefinitionBuilder object(String name)
	{
		return parent.object(name);
	}

	@Override
	public RelationBuilder relation(String name)
	{
		return parent.relation(name);
	}

	@Override
	public ObjectDefinitionBuilder tuple(String userset, String relation, String resource)
	throws ParseException, InvalidTupleException
	{
		return parent.tuple(userset, relation, resource);
	}

	@Override
	public ObjectDefinitionBuilder tuple(UserSet userset, String relation, ObjectId resource)
	throws InvalidTupleException
	{
		return parent.tuple(userset, relation, resource);
	}

	@Override
	public ObjectDefinitionBuilder tuple(String tuple)
	throws ParseException, InvalidTupleException
	{
		return parent.tuple(tuple);
	}

	@Override
	public TupleBuilder tuples()
	{
		return parent.tuples();
	}
}
