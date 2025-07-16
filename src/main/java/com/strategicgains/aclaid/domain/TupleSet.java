package com.strategicgains.aclaid.domain;

/**
 * TupleSet represents a query filter against a tuple store.
 * 
 * From the Zanzibar document: Each tupleset specifies keys
 * of a set of relation tuples. The set can include a single
 * tuple key, or all tuples with a given object ID or userset
 * in a namespace, optionally constrained by a relation name.
 * With the tuplesets, clients can look up a specific
 * membership entry, read all entries in an ACL or group, or
 * look up all groups with a given user as a direct member.
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
	 * Constructs a TupleSet with the specified relation and user set.
	 *
	 * @param userset The userset to filter against.
	 * @param relation The relation to filter on.
	 */
	public TupleSet(UserSet userset, String relation)
	{
		this(userset, relation, null);
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

	public boolean isSingleTupleKey()
	{
		return hasUserset() && hasRelation() && hasObject();
	}

	public boolean isEmpty()
	{
		return (!hasUserset() && !hasRelation() && !hasObject());
	}

	public boolean isValid()
	{
		return (isSingleTupleKey()
			|| (hasUserset() && hasRelation())
			|| (hasUserset() && hasRelation())
			|| (hasUserset() || hasObject()));
	}
}
