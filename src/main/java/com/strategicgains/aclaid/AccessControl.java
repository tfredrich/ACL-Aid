package com.strategicgains.aclaid;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.builder.AccessControlBuilder;
import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.ResourceDefinition;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;

/**
 * A collection of NamespaceConfiguration instances that compose the tuples and 
 * userset rewrite rules, including Relations and Policies for Access Control
 * within an application.
 * 
 * @author tfredrich
 * @since Mar 18, 2022
 * @see AccessControlBuilder
 */
public class AccessControl
{
	private Map<String, ResourceDefinition> resourcesByName = new HashMap<>();
	private TupleSet tuples = new LocalTupleSet();

	public AccessControl addTuple(String userset, String relation, String resource)
	throws ParseException, InvalidTupleException
	{
		return addTuple(new Tuple(userset, relation, resource));
	}

	public AccessControl addTuple(UserSet userset, String relation, ResourceName resource)
	throws InvalidTupleException
	{
		if (!containsRelation(relation)) throw new InvalidTupleException("Relation not registered: " + relation);
		if (!resourcesByName.containsKey(resource.getResourceType())) throw new InvalidTupleException("Resource not defined: " + resource.getResourceType());

		tuples.add(userset, relation, resource);
		return this;
	}

	public AccessControl addTuple(Tuple tuple)
	throws InvalidTupleException
	{
		return addTuple(tuple.getUserset(), tuple.getRelation(), tuple.getObjectId());
	}

	public AccessControl removeTuple(UserSet userset, String relation, ResourceName resource)
	{
		tuples.remove(userset, relation, resource);
		return this;
	}

	/**
	 * Get an existing ResourceDefinition by name or create a new, empty one.
	 * Changes to the instance make changes to this AccessControl.
	 * 
	 * @param resourceName the name of the resource being defined.
	 * @return an existing or new, empty ResourceDefinition instance.
	 */
	public ResourceDefinition resource(String resourceName)
	{
		return resourcesByName.computeIfAbsent(resourceName, n -> new ResourceDefinition(resourceName));
	}

	/**
	 * Check to see if the given relation name is defined in any of the object definitions.
	 * 
	 * @param relation
	 * @return
	 */
	public boolean containsRelation(String relation)
	{
		return resourcesByName.values().stream().anyMatch(n -> n.containsRelation(relation));
	}

	/**
	 * Make an authorization check.
	 * 
	 * @param userset
	 * @param relation
	 * @param objectId
	 * @return
	 * @throws ParseException
	 */
	public boolean check(String userset, String relation, String objectId)
	throws ParseException
	{
		return check(UserSet.parse(userset), relation, new ResourceName(objectId));
	}

	/**
	 * Make an authorization check.
	 * 
	 * @param userset
	 * @param relation
	 * @param objectId
	 * @return
	 */
	public boolean check(UserSet userset, String relation, ResourceName objectId)
	{
		TupleSet rewritten = usersetRewrite(objectId);
		return (rewritten.readOne(userset, relation, objectId) != null);
	}

	private TupleSet usersetRewrite(ResourceName objectId)
	{
		ResourceDefinition resourceDefinition = resourcesByName.get(objectId.getResourceType());
		if (resourceDefinition == null) return LocalTupleSet.EMPTY;
		return resourceDefinition.rewrite(tuples, objectId);
	}

	@Override
	public String toString()
	{
		return String.format("resources=(%s)", resourcesByName.keySet().stream().collect(Collectors.joining(", ")));
	}
}
