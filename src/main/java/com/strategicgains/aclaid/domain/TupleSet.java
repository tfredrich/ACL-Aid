package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class TupleSet
{
	private LinkedList<UserSet> usersets = new LinkedList<>();
	private Map<UserSet, Map<String, Set<ResourceName>>> usersetTree = new HashMap<>();
	private Map<ResourceName, Map<String, Set<UserSet>>> resourceTree = new HashMap<>();

	public TupleSet()
	{
		super();
	}

	protected TupleSet(ResourceName resource, String relation, Set<UserSet> usersets)
	{
		this();
		if (usersets == null) return;

		usersets.stream().forEach(userset -> {
			addTuple(resource, relation, userset);
		});
	}

	protected TupleSet(UserSet userset, String relation, Set<ResourceName> resources)
	{
		this();
		if (resources == null) return;

		resources.stream().forEach(resource -> {
			addTuple(resource, relation, userset);
		});
	}

	public int size()
	{
		return usersets.size();
	}

	// Read all the usersets having a relation on a resource.
	public TupleSet read(String relation, ResourceName resource)
	{
		Map<String, Set<UserSet>> subtree = resourceTree.get(resource);

		if (subtree == null) return null;

		Set<UserSet> usersets = subtree.get(relation);
		return new TupleSet(resource, relation, usersets);
	}

	// Read all the relations a userset has on a resource.
	public TupleSet read(UserSet userset, ResourceName resource)
	{
		return null;
	}

	// Read all the resources a userset has with this relation.
	public TupleSet read(UserSet userset, String relation)
	{
		Map<String, Set<ResourceName>> subtree = usersetTree.get(userset);

		if (subtree == null) return null;

		Set<ResourceName> resources = subtree.get(relation);
		return new TupleSet(userset, relation, resources);
	}

	// Read a single tuple.
	public Tuple readOne(String userset, String relation, String resource)
	throws ParseException
	{
		return readOne(UserSet.parse(userset), relation, ResourceName.parse(resource));
	}

	// Read a single tuple.
	public Tuple readOne(UserSet userset, String relation, ResourceName resource)
	{
		Map<String, Set<UserSet>> subtree1 = resourceTree.get(resource);

		if (subtree1 == null) return null;

		// these are the usersets with the direct relation.
		Set<UserSet> usersets = subtree1.get(relation);

		if (usersets == null) return null;
		if (usersets.contains(userset)) return new Tuple(resource, relation, userset);

		//TODO: recursively check memberships...
		if (usersets.stream().filter(set -> set.hasRelation()).map(set -> readOne(userset, set.getRelation(), set.getResource()))
			.filter(t -> (t != null)).count() > 0)
		{
			return new Tuple(resource, relation, userset);
		}

		return null;
	}

	public TupleSet addTuple(String resource, String relation, String userset)
	throws ParseException
	{
		return addTuple(ResourceName.parse(resource), relation, UserSet.parse(userset));
	}

	public TupleSet addTuple(ResourceName resource, String relation, UserSet userset)
	{
		writeUsersetTree(resource, relation, userset);
		writeResourceTree(resource, relation, userset);
		usersets.add(userset);
		return this;
	}

	public TupleSet addTuple(Tuple tuple)
	{
		return addTuple(tuple.getResource(), tuple.getRelation(), tuple.getUserset());
	}

	private void writeUsersetTree(ResourceName resource, String relation, UserSet userset)
	{
		Map<String, Set<ResourceName>> relationSubtree = usersetTree.computeIfAbsent(userset, t -> new HashMap<>());
		Set<ResourceName> resources = relationSubtree.computeIfAbsent(relation, s -> new HashSet<>());
		resources.add(resource);
	}

	private void writeResourceTree(ResourceName resource, String relation, UserSet userset)
	{
		Map<String, Set<UserSet>> relationSubtree = resourceTree.computeIfAbsent(resource, t -> new HashMap<>());
		Set<UserSet> usersets = relationSubtree.computeIfAbsent(relation, s -> new HashSet<>());
		usersets.add(userset);
	}
}
