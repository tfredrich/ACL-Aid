package com.strategicgains.aclaid;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.builder.AccessControlBuilder;
import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

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
	private Map<String, Namespace> namespaces = new HashMap<>();
	private TupleSet tuples = new LocalTupleSet();

	public AccessControl addTuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException, InvalidTupleException
	{
		return addTuple(new Tuple(userset, relation, resource));
	}

	public AccessControl addTuple(UserSet userset, String relation, ResourceName resource)
	throws RelationNotRegisteredException
	{
		if (!containsRelation(relation)) throw new RelationNotRegisteredException(relation);

		tuples.add(userset, relation, resource);
		return this;
	}

	public AccessControl addTuple(Tuple tuple)
	throws RelationNotRegisteredException
	{
		return addTuple(tuple.getUserset(), tuple.getRelation(), tuple.getResource());
	}

	public AccessControl removeTuple(UserSet userset, String relation, ResourceName resource)
	{
		tuples.remove(userset, relation, resource);
		return this;
	}

	/**
	 * Get an existing NamespaceConfiguration by name or create a new, empty one.
	 * Changes to the instance make changes to this AccessControl.
	 * 
	 * @param namespace the name of the namespace.
	 * @return an existing or new, empty NamespaceConfiguration instance.
	 */
	public Namespace namespace(String namespace)
	{
		return namespaces.computeIfAbsent(namespace, n -> new Namespace(namespace));
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
		return check(UserSet.parse(userset), relation, new ResourceName(resource));
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
		TupleSet rewritten = rewrite(userset, relation, resource);
		return (rewritten.readOne(userset, relation, resource) != null);
	}

	private TupleSet rewrite(UserSet userset, String relation, ResourceName resource)
	{
		Namespace usersetNamespace = namespaces.get(userset.getNamespace());
		return usersetNamespace.rewrite(tuples, userset, relation, resource);
	}

	@Override
	public String toString()
	{
		return String.format("namespaces=(%s)", namespaces.keySet().stream().collect(Collectors.joining(", ")));
	}
}
