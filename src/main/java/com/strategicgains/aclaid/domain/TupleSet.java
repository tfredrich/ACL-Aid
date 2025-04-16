package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.exception.InvalidTupleException;

/**
 * A TupleSet is a collection of Tuples. It is the primary data structure used to store and query access control data.
 * 
 * @author Todd Fredrich
 * @see Tuple, LocalTupleSet
 */
public interface TupleSet
{
	boolean isEmpty();

	/**
	 * Check if the provided user has the relation to the object in this tuple set.
	 * 
	 * From the Zanzibar document:
	 * A check request specifies a userset, represented by ⟨object#relation⟩, a
	 * putative user, often represented by an authentication token.
	 * 
	 * @param userset
	 * @param relation
	 * @param objectId
	 * @return
	 */
	boolean check(UserSet userset, String relation, ObjectId objectId);

	/**
	 * Add a tuple to this tuple set.
	 * 
	 * @param tuple
	 * @return
	 */
	TupleSet add(Tuple tuple);

	/**
	 * Create a new tuple using the given object ID, relation and userset then add it to the tuple set.
	 * 
	 * @param userset
	 * @param relation
	 * @param objectId
	 * @return
	 * @throws InvalidTupleException 
	 */
	TupleSet add(UserSet userset, String relation, ObjectId objectId)
	throws InvalidTupleException;

	/**
	 * Remove a tuple from this tuple set.
	 * 
	 * @param tuple
	 * @return
	 */
	TupleSet remove(Tuple tuple);

	/**
	 * Remove a tuple from this tuple set using the given object ID, relation and userset.
	 * 
	 * @param userset
	 * @param relation
	 * @param objectId
	 * @return
	 */
	TupleSet remove(UserSet userset, String relation, ObjectId objectId);
}