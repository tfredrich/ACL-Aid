package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.strategicgains.aclaid.exception.InvalidTupleException;

public class LocalTupleSet
implements TupleSet
{
	public static final LocalTupleSet EMPTY = new LocalTupleSet();

	// The directional index of relation tuples by user set and relation. 
	private Map<UserSet, Map<String, Set<Tuple>>> usersetTree = new HashMap<>();

	// The directional index of relation tuples by resource and relation.
	private Map<ResourceName, Map<String, Set<Tuple>>> resourceTree = new HashMap<>();

	public LocalTupleSet()
	{
		super();
	}

	protected LocalTupleSet(ResourceName resource, String relation, Set<UserSet> usersets)
	throws InvalidTupleException
	{
		this();
		if (usersets == null) return;

		for (UserSet userset : usersets)
		{
			add(userset, relation, resource);
		}
	}

	protected LocalTupleSet(UserSet userset, String relation, Set<ResourceName> resources)
	throws InvalidTupleException
	{
		this();
		if (resources == null) return;

		for (ResourceName resource : resources)
		{
			add(userset, relation, resource);
		}
	}

	public LocalTupleSet(LocalTupleSet that)
	{
		this();
		this.usersetTree = new HashMap<>(that.usersetTree);
		this.resourceTree = new HashMap<>(that.resourceTree);
	}

	public LocalTupleSet(Collection<Tuple> tuples)
	{
		this();
		tuples.stream().forEach(this::add);
	}

	@Override
	public int size()
	{
		return usersetTree.size();
	}

	// Read all the usersets having a relation on a resource.
	@Override
	public LocalTupleSet read(String relation, ResourceName resource)
	{
		Map<String, Set<Tuple>> subtree = resourceTree.get(resource);

		if (subtree == null) return null;

		Set<Tuple> tuples = subtree.get(relation);
		return (tuples != null ? new LocalTupleSet(tuples) : null);
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
		Map<String, Set<Tuple>> subtree = usersetTree.get(userset);

		if (subtree == null) return null;

		Set<Tuple> tuples = subtree.get(relation);
		return (tuples != null ? new LocalTupleSet(tuples) : null);
	}

	// Read a single tuple.
	@Override
	public Tuple readOne(String userset, String relation, String resource)
	throws ParseException
	{
		return readOne(UserSet.parse(userset), relation, new ResourceName(resource));
	}

	// Read a single tuple.
	@Override
	public Tuple readOne(UserSet userset, String relation, ResourceName resource)
	{
		Map<String, Set<Tuple>> resourceSubtree = resourceTree.get(resource);

		if (resourceSubtree == null) return null;

		// these are the usersets with the direct relation.
		Set<Tuple> resourceUsersets = resourceSubtree.get(relation);

		if (resourceUsersets == null) return null;
		if (resourceUsersets.stream().anyMatch(t -> t.matches(userset, relation, resource)))
		{
			return new Tuple(userset, relation, resource);
		}

		//Recursively check memberships...
		if (resourceUsersets.stream().filter(t -> t.getUserset().hasRelation()).anyMatch(t -> readOne(userset, t.getUsersetRelation(), t.getUsersetResource()) != null))
		{
			return new Tuple(userset, relation, resource);
		}

		return null;
	}

	@Override
	public LocalTupleSet add(String userset, String relation, String resource)
	throws ParseException, InvalidTupleException
	{
		return add(UserSet.parse(userset), relation, new ResourceName(resource));
	}

	@Override
	public LocalTupleSet add(UserSet userset, String relation, ResourceName resource)
	throws InvalidTupleException
	{
		return add(newDynamicTuple(userset, relation, resource));
	}

	@Override
	public LocalTupleSet add(Tuple tuple)
	{
		writeUsersetTree(tuple);
		writeResourceTree(tuple);
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
		return remove(new Tuple(userset, relation, resource));
	}

	@Override
	public TupleSet copy()
	{
		return new LocalTupleSet(this);
	}

	private void writeUsersetTree(Tuple tuple)
	{
		Map<String, Set<Tuple>> relationSubtree = usersetTree.computeIfAbsent(tuple.getUserset(), t -> new HashMap<>());
		Set<Tuple> resources = relationSubtree.computeIfAbsent(tuple.getRelation(), s -> new HashSet<>());
		resources.add(tuple);
	}

	private void writeResourceTree(Tuple tuple)
	{
		Map<String, Set<Tuple>> relationSubtree = resourceTree.computeIfAbsent(tuple.getResource(), t -> new HashMap<>());
		Set<Tuple> usersets = relationSubtree.computeIfAbsent(tuple.getRelation(), s -> new HashSet<>());
		usersets.add(tuple);
	}

	private void removeUsersetTree(Tuple tuple)
	{
		Map<String, Set<Tuple>> relationSubtree = usersetTree.get(tuple.getUserset());

		if (relationSubtree == null) return;

		Set<Tuple> resources = relationSubtree.get(tuple.getRelation());

		if (resources == null) return;

		resources.remove(tuple);
	}

	private void removeResourceTree(Tuple tuple)
	{
		Map<String, Set<Tuple>> relationSubtree = resourceTree.get(tuple.getResource());

		if (relationSubtree == null) return;

		Set<Tuple> usersets = relationSubtree.get(tuple.getRelation());

		if (usersets == null) return;

		usersets.remove(tuple);
	}

	private Tuple newDynamicTuple(UserSet userset, String relation, ResourceName resource)
	throws InvalidTupleException
	{
		if (resource.isWildcard()) throw new InvalidTupleException("Tuple resources cannot contain wildcards: " + resource.toString());
		return new Tuple(userset, relation, resource);
	}

	@Override
	public TupleSet addAll(TupleSet tupleset)
	{
		if (tupleset != null)
		{
			tupleset.stream().forEach(this::add);
		}

		return this;
	}

	@Override
	public Stream<Tuple> stream()
	{
		Collection<Tuple> tuples = new ArrayList<>();
		usersetTree.forEach((u, m) -> 
			m.forEach((r, s) -> 
				s.stream().forEach(t -> tuples.add(new Tuple(t)))
			)
		);
		return tuples.stream();
	}
}
