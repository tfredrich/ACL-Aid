package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.strategicgains.aclaid.exception.InvalidTupleException;

public class LocalTupleSet
implements TupleSet
{
	boolean allowWildcards = false;
	private LinkedList<UserSet> usersets = new LinkedList<>();
	private Map<UserSet, Map<String, Set<ResourceName>>> usersetTree = new HashMap<>();
	private Map<ResourceName, Map<String, Set<UserSet>>> resourceTree = new HashMap<>();

	public LocalTupleSet()
	{
		super();
	}

	public LocalTupleSet(boolean allowWildcards)
	{
		super();
		this.allowWildcards = allowWildcards;
	}

	protected LocalTupleSet(ResourceName resource, String relation, Set<UserSet> usersets)
	{
		this();
		if (usersets == null) return;

		usersets.stream().forEach(userset -> {
			add(userset, relation, resource);
		});
	}

	protected LocalTupleSet(UserSet userset, String relation, Set<ResourceName> resources)
	{
		this();
		if (resources == null) return;

		resources.stream().forEach(resource -> {
			add(userset, relation, resource);
		});
	}

	@Override
	public int size()
	{
		return usersets.size();
	}

	// Read all the usersets having a relation on a resource.
	@Override
	public LocalTupleSet read(String relation, ResourceName resource)
	{
		Map<String, Set<UserSet>> subtree = resourceTree.get(resource);

		if (subtree == null) return null;

		Set<UserSet> usersets = subtree.get(relation);
		return new LocalTupleSet(resource, relation, usersets);
	}

	// Read all the relations a userset has on a resource.
	@Override
	public LocalTupleSet read(UserSet userset, ResourceName resource)
	{
		return null;
	}

	// Read all the resources a userset has with this relation.
	@Override
	public LocalTupleSet read(UserSet userset, String relation)
	{
		Map<String, Set<ResourceName>> subtree = usersetTree.get(userset);

		if (subtree == null) return null;

		Set<ResourceName> resources = subtree.get(relation);
		return new LocalTupleSet(userset, relation, resources);
	}

	// Read a single tuple.
	@Override
	public Tuple readOne(String userset, String relation, String resource)
	throws ParseException
	{
		return readOne(UserSet.parse(userset), relation, ResourceName.parse(resource));
	}

	// Read a single tuple.
	@Override
	public Tuple readOne(UserSet userset, String relation, ResourceName resource)
	{
		Map<String, Set<UserSet>> subtree1 = resourceTree.get(resource);

		if (subtree1 == null) return null;

		// these are the usersets with the direct relation.
		Set<UserSet> usersets = subtree1.get(relation);

		if (usersets == null) return null;
		if (usersets.stream().anyMatch(u -> u.matches(userset)))
		{
			return newDynamicTuple(userset, relation, resource);
		}

		//Recursively check memberships...
		if (usersets.stream().filter(set -> set.hasRelation()).map(set -> readOne(userset, set.getRelation(), set.getResource()))
			.filter(t -> (t != null)).count() > 0)
		{
			return newDynamicTuple(userset, relation, resource);
		}

		return null;
	}

	@Override
	public LocalTupleSet add(String userset, String relation, String resource)
	throws ParseException
	{
		return add(UserSet.parse(userset), relation, ResourceName.parse(resource));
	}

	@Override
	public LocalTupleSet add(UserSet userset, String relation, ResourceName resource)
	{
		return add(newDynamicTuple(userset, relation, resource));
	}

	@Override
	public LocalTupleSet add(Tuple tuple)
	{
		writeUsersetTree(tuple);
		writeResourceTree(tuple);
		usersets.add(tuple.getUserset());
		return this;
	}

	@Override
	public LocalTupleSet remove(Tuple tuple)
	{
		removeUsersetTree(tuple);
		removeResourceTree(tuple);
		return this;
	}

	@Override
	public LocalTupleSet remove(UserSet userset, String relation, ResourceName resource)
	{
		return remove(newDynamicTuple(userset, relation, resource));
	}

	private void writeUsersetTree(Tuple tuple)
	{
		Map<String, Set<ResourceName>> relationSubtree = usersetTree.computeIfAbsent(tuple.getUserset(), t -> new HashMap<>());
		Set<ResourceName> resources = relationSubtree.computeIfAbsent(tuple.getRelation(), s -> new HashSet<>());
		resources.add(tuple.getResource());
	}

	private void writeResourceTree(Tuple tuple)
	{
		Map<String, Set<UserSet>> relationSubtree = resourceTree.computeIfAbsent(tuple.getResource(), t -> new HashMap<>());
		Set<UserSet> usersets = relationSubtree.computeIfAbsent(tuple.getRelation(), s -> new HashSet<>());
		usersets.add(tuple.getUserset());
	}

	private void removeUsersetTree(Tuple tuple)
	{
		Map<String, Set<ResourceName>> relationSubtree = usersetTree.get(tuple.getUserset());

		if (relationSubtree == null) return;

		Set<ResourceName> resources = relationSubtree.computeIfAbsent(tuple.getRelation(), s -> new HashSet<>());

		if (resources == null) return;

		resources.remove(tuple.getResource());
	}

	private void removeResourceTree(Tuple tuple)
	{
		Map<String, Set<UserSet>> relationSubtree = resourceTree.get(tuple.getResource());

		if (relationSubtree == null) return;

		Set<UserSet> usersets = relationSubtree.computeIfAbsent(tuple.getRelation(), s -> new HashSet<>());

		if (usersets == null) return;

		usersets.remove(tuple.getUserset());
	}

	private Tuple newDynamicTuple(UserSet userset, String relation, ResourceName resource)
	{
		try
		{
			return new Tuple(userset, relation, resource);
		}
		catch (InvalidTupleException e)
		{
			return null;
		}
	}
}
