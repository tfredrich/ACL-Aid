package com.strategicgains.aclaid;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.builder.AccessControlListBuilder;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

/**
 * A collection of NamespaceConfiguration instances that compose the tuples and 
 * userset rewrite rules, including Relations and Policies for Access Control
 * within an application.
 * 
 * @author tfredrich
 * @since Mar 18, 2022
 * @see AccessControlListBuilder
 */
public class AccessControlList
{
	private Map<String, NamespaceConfiguration> namespaces = new HashMap<>();
	private TupleSet tuples = new TupleSet();

	public AccessControlList addTuple(String resource, String relation, String userset)
	throws ParseException, RelationNotRegisteredException
	{
		return addTuple(new Tuple(resource, relation, userset));
	}

	public AccessControlList addTuple(ResourceName resource, String relation, UserSet userset)
	throws RelationNotRegisteredException
	{
		if (!containsRelation(relation)) throw new RelationNotRegisteredException(relation);

		tuples.add(resource, relation, userset);
		return this;
	}

	public AccessControlList addTuple(Tuple tuple)
	throws RelationNotRegisteredException
	{
		return addTuple(tuple.getResource(), tuple.getRelation(), tuple.getUserset());
	}

	public AccessControlList removeTuple(ResourceName resource, String relation, UserSet userset)
	throws RelationNotRegisteredException
	{
		tuples.remove(resource, relation, userset);
		return this;
	}

	/**
	 * Get an existing NamespaceConfiguration by name or create a new, empty one.
	 * Changes to the instance make changes to this AccessControlList.
	 * 
	 * @param namespace
	 * @return an existing or new, empty NamespaceConfiguration instance.
	 */
	public NamespaceConfiguration namespace(String namespace)
	{
		return namespaces.computeIfAbsent(namespace, n -> new NamespaceConfiguration(this));
	}

	/**
	 * Check to see if the given relation name is defined in any of the namespaces.
	 * 
	 * @param relation
	 * @return
	 */
	public boolean containsRelation(String relation)
	{
		return namespaces.values().stream().anyMatch(n -> n.containsRelation(relation));
	}

	/**
	 * Make an authorization check.
	 * 
	 * @param userset
	 * @param relation
	 * @param resource
	 * @return
	 * @throws ParseException
	 */
	public boolean check(String userset, String relation, String resource)
	throws ParseException
	{
		return check(UserSet.parse(userset), relation, ResourceName.parse(resource));
	}

	/**
	 * Make an authorization check.
	 * 
	 * @param userset
	 * @param relation
	 * @param resource
	 * @return
	 */
	public boolean check(UserSet userset, String relation, ResourceName resource)
	{
		NamespaceConfiguration acl = namespaces.get(resource.getNamespace());

		if (acl != null && acl.check(this, userset, relation, resource)) return true;

		return checkTuples(userset, relation, resource);
	}

	@Override
	public String toString()
	{
		return String.format("namespaces=(%s)", namespaces.keySet().stream().collect(Collectors.joining(", ")));
	}

	private boolean checkTuples(UserSet userset, String relation, ResourceName resource)
	{
		return (tuples.readOne(userset, relation, resource) != null);
	}
}
