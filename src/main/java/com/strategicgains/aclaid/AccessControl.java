package com.strategicgains.aclaid;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.builder.AccessControlBuilder;
import com.strategicgains.aclaid.domain.SimpleTupleStore;
import com.strategicgains.aclaid.domain.ObjectDefinition;
import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleStore;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;

/**
 * This AccessControl class is equivalent to the Namespace Configuration in the Zanzibar paper.
 * 
 * It specifies its objects and relations as well as contains the relation tuples for the namespace.
 * 
 * Each relation has a name, which is a client-defined string such as viewer or editor, and a relation configuration,
 * such as rewrite rules, which are used to determine the set of tuples that apply to a given object at runtime.
 * 
 * Therefore, AccessControl maintains configuration and tuples for a namespace, and it is used to check authorization
 * within an application.
 * 
 * @author Todd Fredrich
 * @since Mar 18, 2022
 * @see AccessControlBuilder
 */
public class AccessControl
{
	private Map<String, ObjectDefinition> objectsByName = new HashMap<>();
	private TupleStore tuples = new SimpleTupleStore();

	public AccessControl addTuple(String userset, String relation, String objectId)
	throws ParseException, InvalidTupleException
	{
		return addTuple(new Tuple(userset, relation, objectId));
	}

	public AccessControl addTuple(UserSet userset, String relation, ObjectId objectId)
	throws InvalidTupleException
	{
		if (!containsRelation(relation)) throw new InvalidTupleException("Relation not registered: " + relation);
		if (!objectsByName.containsKey(objectId.getType())) throw new InvalidTupleException("Object not defined: " + objectId.getType());

		tuples.add(userset, relation, objectId);
		return this;
	}

	public AccessControl addTuple(Tuple tuple)
	throws InvalidTupleException
	{
		return addTuple(tuple.getUserset(), tuple.getRelation(), tuple.getObjectId());
	}

	public AccessControl removeTuple(UserSet userset, String relation, ObjectId resource)
	{
		tuples.remove(userset, relation, resource);
		return this;
	}

	/**
	 * Get an existing ObjectDefinition by name or create a new, empty one.
	 * Changes to the instance make changes to this AccessControl.
	 * 
	 * @param objectName the name of the resource being defined.
	 * @return an existing or new, empty ObjectDefinition instance.
	 */
	public ObjectDefinition object(String objectName)
	{
		return objectsByName.computeIfAbsent(objectName, ObjectDefinition::new);
	}

	/**
	 * Check to see if the given relation name is defined in any of the object definitions.
	 * 
	 * @param relation
	 * @return
	 */
	public boolean containsRelation(String relation)
	{
		return objectsByName.values().stream().anyMatch(n -> n.containsRelation(relation));
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
		return check(UserSet.parse(userset), relation, new ObjectId(objectId));
	}

	/**
	 * Make an authorization check.
	 * 
	 * Zanzibar evaluates ACL checks by converting check requests to boolean
	 * expressions. In a simple case, when there are no userset rewrite rules,
	 * checking a user U against a userset ⟨object#relation⟩ can be expressed as
	 * 
	 * CHECK(U,⟨object#relation⟩) = ∃ tuple ⟨object#relation@U⟩ ∨∃ tuple
	 * ⟨object#relation@U′⟩, where U′ = ⟨object′#relation′⟩ s.t. CHECK(U,U′).
	 * 
	 * Finding a valid U′ = ⟨object′ #relation′ ⟩ involves evaluating membership on
	 * all indirect ACLs or groups, recursively. This kind of “pointer chasing”
	 * works well for most types of ACLs and groups, but can be expensive when
	 * indirect ACLs or groups are deep or wide.
	 * 
	 * Userset rewrite rules are also translated to boolean expressions as part of
	 * check evaluation. Evaluation of leaf nodes usually involves reading relation
	 * tuples from databases.
	 * 
	 * @param userset
	 * @param relation
	 * @param objectId
	 * @return
	 */
	public boolean check(UserSet userset, String relation, ObjectId objectId)
	{
		ObjectDefinition objectDefinition = objectsByName.get(objectId.getType());
		if (objectDefinition == null) return false;
		return objectDefinition.check(tuples, userset, relation, objectId);
	}

	@Override
	public String toString()
	{
		return String.format("resources=(%s)", objectsByName.keySet().stream().collect(Collectors.joining(", ")));
	}
}
