package com.strategicgains.aclaid.domain;

import java.text.ParseException;

public interface TupleSet {

	int size();

	/**
	 * Read all the usersets having a relation on a resource.
	 * 
	 * @param relation
	 * @param resource
	 * @return
	 */
	TupleSet read(String relation, ResourceName resource);

	/**
	 * Read all the relations a userset has on a resource.
	 * 
	 * @param userset
	 * @param resource
	 * @return
	 */
	TupleSet read(UserSet userset, ResourceName resource);

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
	 * @param resource
	 * @return
	 * @throws ParseException
	 */
	Tuple readOne(String userset, String relation, String resource) throws ParseException;

	/**
	 * Read a single tuple.
	 * 
	 * @param userset
	 * @param relation
	 * @param resource
	 * @return
	 */
	Tuple readOne(UserSet userset, String relation, ResourceName resource);

	/**
	 * Add a tuple to this tuple set.
	 * 
	 * @param tuple
	 * @return
	 */
	TupleSet add(Tuple tuple);

	/**
	 * Create a new tuple using the given resource, relation and userset then add it to the tuple set.
	 * 
	 * @param userset
	 * @param relation
	 * @param resource
	 * @return
	 * @throws ParseException
	 */
	TupleSet add(String userset, String relation, String resource)
	throws ParseException;

	/**
	 * Create a new tuple using the given resource, relation and userset then add it to the tuple set.
	 * 
	 * @param userset
	 * @param relation
	 * @param resource
	 * @return
	 */
	TupleSet add(UserSet userset, String relation, ResourceName resource);

	/**
	 * Remove a tuple from this tuple set.
	 * 
	 * @param tuple
	 * @return
	 */
	TupleSet remove(Tuple tuple);

	/**
	 * Remove a tuple from this tuple set using the given resource, relation and userset.
	 * 
	 * @param userset
	 * @param relation
	 * @param resource
	 * @return
	 */
	TupleSet remove(UserSet userset, String relation, ResourceName resource);
}