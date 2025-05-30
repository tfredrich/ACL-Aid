package com.strategicgains.aclaid.domain;

/**
 * TupleSet represents a query filter against a tuple store.
 * 
 * @author Todd Fredrich
 * @since 29 May 2025
 * @see {@link TupleStore}
 */
public class TupleSet
{
	private UserSet userset;
	private String relation;
	private ObjectId object;
	
	/**
	 * Constructs a TupleSet with the specified userset.
	 *
	 * @param userset The userset to filter against.
	 */
	public TupleSet(UserSet userset, String relation, ObjectId object)
	{
		this.userset = userset;
		this.relation = relation;
		this.object = object;
	}

	/**
	 * Constructs a TupleSet with the specified relation and object ID.
	 *
	 * @param relation The relation to filter on.
	 * @param object The object ID to filter against.
	 */
	public TupleSet(String relation, ObjectId object)
	{
		this(null, relation, object);
	}

	/**
	 * Constructs a TupleSet with the specified userset.
	 *
	 * @param userset The userset to filter against.
	 */
	public TupleSet(UserSet userset)
	{
		this(userset, null, null);
	}

	public UserSet getUserset()
	{
		return userset;
	}

	public boolean hasUserset()
	{
		return userset != null;
	}

	public String getRelation()
	{
		return relation;
	}

	public boolean hasRelation()
	{
		return relation != null;
	}

	public ObjectId getObject()
	{
		return object;
	}

	public boolean hasObject()
	{
		return object != null;
	}
}
