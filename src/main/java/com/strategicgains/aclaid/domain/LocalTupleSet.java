package com.strategicgains.aclaid.domain;

import java.text.ParseException;
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
	public static final LocalTupleSet EMPTY_SET = new LocalTupleSet();

	/**
	 * The directional index of relation tuples by user set and relation. 
	 */
	private Map<UserSet, Map<String, Set<Tuple>>> usersetTree = new HashMap<>();

	/**
	 * The directional index of relation tuples by objectId and relation.
	 */
	private Map<ObjectId, Map<String, Set<Tuple>>> objectTree = new HashMap<>();

	public LocalTupleSet()
	{
		super();
	}

	public LocalTupleSet(ObjectId objectId, String relation, Set<UserSet> usersets)
	throws InvalidTupleException
	{
		this();
		if (usersets == null) return;

		for (UserSet userset : usersets)
		{
			add(userset, relation, objectId);
		}
	}

	public LocalTupleSet(UserSet userset, String relation, Set<ObjectId> objectIds)
	throws InvalidTupleException
	{
		this();
		if (objectIds == null) return;

		for (ObjectId resource : objectIds)
		{
			add(userset, relation, resource);
		}
	}

	public LocalTupleSet(LocalTupleSet that)
	{
		this();
		this.usersetTree = new HashMap<>(that.usersetTree);
		this.objectTree = new HashMap<>(that.objectTree);
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

	/**
	 * Read all the usersets having a relation on an object ID.
	 */
	@Override
	public LocalTupleSet read(String relation, ObjectId objectId)
	{
		Map<String, Set<Tuple>> subtree = objectTree.get(objectId);

		if (subtree == null) return null;

		Set<Tuple> tuples = subtree.get(relation);
		if (tuples == null) return null;
		LocalTupleSet results = new LocalTupleSet(tuples);
		
		//Recursively add tuples for usersets with a relation on this resource.
		tuples.stream().filter(t -> t.getUserset().hasRelation()).forEach(t -> results.addAll(read(t.getUsersetRelation(), t.getUsersetResource())));
		return results;
	}

	/**
	 * Read all the relations a userset has on a resource.
	 */
	@Override
	public LocalTupleSet read(UserSet userset, ObjectId resource)
	{
		return null;
	}

	/**
	 * Read all the tuples for objects a userset has with this relation directly.
	 */
	@Override
	public LocalTupleSet read(UserSet userset, String relation)
	{
		Map<String, Set<Tuple>> subtree = usersetTree.get(userset);
		if (subtree == null) return null;

		Set<Tuple> tuples = subtree.get(relation);
		return (tuples != null ? new LocalTupleSet(tuples) : null);
	}

	/**
	 * Read a single tuple, navigating the user set tree.
	 */
	@Override
	public Tuple readOne(String userset, String relation, String resource)
	throws ParseException
	{
		return readOne(UserSet.parse(userset), relation, new ObjectId(resource));
	}

	/**
	 * Read a single tuple, navigating the user set tree.
	 */
	@Override
	public Tuple readOne(UserSet userset, String relation, ObjectId resource)
	{
		Map<String, Set<Tuple>> resourceSubtree = objectTree.get(resource);

		if (resourceSubtree == null) return null;

		// these are the usersets with the direct relation.
		Set<Tuple> resourceUsersets = resourceSubtree.get(relation);

		if (resourceUsersets == null) return null;

		//Check for direct membership...
		if (resourceUsersets.stream().anyMatch(t -> t.matches(userset, relation, resource)))
		{
			return new Tuple(userset, relation, resource);
		}

		//Recursively check indirect memberships...
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
		return add(UserSet.parse(userset), relation, new ObjectId(resource));
	}

	@Override
	public LocalTupleSet add(UserSet userset, String relation, ObjectId resource)
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
	public LocalTupleSet remove(UserSet userset, String relation, ObjectId resource)
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
		Map<String, Set<Tuple>> relationSubtree = objectTree.computeIfAbsent(tuple.getObjectId(), t -> new HashMap<>());
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
		Map<String, Set<Tuple>> relationSubtree = objectTree.get(tuple.getObjectId());

		if (relationSubtree == null) return;

		Set<Tuple> usersets = relationSubtree.get(tuple.getRelation());

		if (usersets == null) return;

		usersets.remove(tuple);
	}

	private Tuple newDynamicTuple(UserSet userset, String relation, ObjectId resource)
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
		return usersetTree.values().stream()
			.flatMap(m -> m.values().stream())
			.flatMap(Collection::stream)
			.map(Tuple::new);
	}

	@Override
	public Stream<UserSet> userSets()
	{
		return usersetTree.keySet().stream();
	}
}
