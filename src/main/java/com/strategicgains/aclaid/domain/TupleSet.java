package com.strategicgains.aclaid.domain;

import java.util.Set;

import com.strategicgains.aclaid.exception.InvalidTupleException;

/**
 * A TupleSet is a collection of Tuples. It is the primary data structure used to store and query access control data.
 * 
 * @author Todd Fredrich
 * @see Tuple, LocalTupleSet
 */
public interface TupleSet
extends Copyable<TupleSet>
{
	int size();
	
	/**
	 * Read all the usersets having a direct relation on an object.
	 * 
	 * @param relation
	 * @param objectId
	 * @return
	 */
	Set<UserSet> read(String relation, ObjectId objectId);

	/**
	 * Read all the usersets having a relation on an object including indirect ACLs.
	 * 
	 * @param relation
	 * @param objectId
	 * @return
	 */
	Set<UserSet> expand(String relation, ObjectId objectId);

	/**
	 * Read all the resources a userset has with this relation.
	 * 
	 * @param userset
	 * @param relation
	 * @return
	 */
	Set<ObjectId> read(UserSet userset, String relation);

	/**
	 * Read a single tuple.
	 * 
	 * @param userset
	 * @param relation
	 * @param objectId
	 * @return
	 */
	Tuple readOne(UserSet userset, String relation, ObjectId objectId);

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