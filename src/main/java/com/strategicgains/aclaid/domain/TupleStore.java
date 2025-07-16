package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.exception.InvalidTupleException;

/**
 * A TupleStore is a collection of Tuples. It is the primary data structure used to store and query access control data.
 * 
 * @author Todd Fredrich
 * @see Tuple, LocalTupleStore
 */
public interface TupleStore
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
	 * Perform a read operation against the tuple store using the provided tuple set.
	 * This is used to retrieve tuples that match the criteria specified in the tuple set.
	 * 
	 * @param tupleSet A tuple set that defines the criteria for the read operation.
	 * @return a collection of tuples that match the criteria defined in the tuple set.
	 */
	TupleStore read(TupleSet tupleSet);

	/**
	 * Add a tuple to this tuple set.
	 * 
	 * @param tuple
	 * @return
	 */
	TupleStore add(Tuple tuple);

	/**
	 * Create a new tuple using the given object ID, relation and userset then add it to the tuple set.
	 * 
	 * @param userset
	 * @param relation
	 * @param objectId
	 * @return
	 * @throws InvalidTupleException 
	 */
	TupleStore add(UserSet userset, String relation, ObjectId objectId)
	throws InvalidTupleException;

	/**
	 * Remove a tuple from this tuple set.
	 * 
	 * @param tuple
	 * @return
	 */
	TupleStore remove(Tuple tuple);

	/**
	 * Remove a tuple from this tuple set using the given object ID, relation and userset.
	 * 
	 * @param userset
	 * @param relation
	 * @param objectId
	 * @return
	 */
	TupleStore remove(UserSet userset, String relation, ObjectId objectId);
}