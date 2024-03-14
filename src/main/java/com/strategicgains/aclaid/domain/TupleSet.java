package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.stream.Stream;

import com.strategicgains.aclaid.exception.InvalidTupleException;

public interface TupleSet
extends Copyable<TupleSet>
{
	int size();
	
	/**
	 * Read all the usersets having a relation on an object.
	 * 
	 * @param relation
	 * @param objectId
	 * @return
	 */
	TupleSet read(String relation, ObjectId objectId);

	/**
	 * Read all the relations a userset has on an object.
	 * 
	 * @param userset
	 * @param objectId
	 * @return
	 */
	TupleSet read(UserSet userset, ObjectId objectId);

	/**
	 * Read all the resources a userset has with this relation.
	 * 
	 * @param userset
	 * @param relation
	 * @return
	 */
	TupleSet read(UserSet userset, String relation);

	/**
	 * Read a single tuple.
	 * 
	 * @param userset
	 * @param relation
	 * @param objectId
	 * @return
	 * @throws ParseException
	 */
	Tuple readOne(String userset, String relation, String objectId)
	throws ParseException;

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
	 * Add all the tuples from one tuple set to this one.
	 * 
	 * @param tupleset
	 * @return
	 */
	TupleSet addAll(TupleSet tupleset);

	/**
	 * Create a new tuple using the given object ID, relation and userset then add it to the tuple set.
	 * 
	 * @param userset
	 * @param relation
	 * @param objectId
	 * @return
	 * @throws ParseException
	 * @throws InvalidTupleException 
	 */
	TupleSet add(String userset, String relation, String objectId)
	throws ParseException, InvalidTupleException;

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

	/**
	 * Stream the Tuples in this TupleSet.
	 * 
	 * @return Stream<Tuple>. Never null.
	 */
	Stream<Tuple> stream();

	/**
	 * Stream the UserSets in this TupleSet.
	 * 
	 * @return Stream<UserSet>. Never null.
	 */
	Stream<UserSet> userSets();
}