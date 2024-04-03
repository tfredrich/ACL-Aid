package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.strategicgains.aclaid.exception.InvalidTupleException;

/**
 * An InMemoryTupleSet is a graph of of tuples that can be read and written to. It is a simple in-memory
 * implementation of a TupleSet and is not thread-safe.
 * 
 * There are two indexes maintained by InMemoryTupleSet, both being essentially adjacency lists:
 * 1) From a UserSet to Relations to ObjectIds and
 * 2) From an ObjectID to Relations to UserSets
 * 
 * This gives us the ability to navigate either way through the tuples to navigate the graph.
 * 
 * @author Todd Fredrich
 */
public class InMemoryTupleSet
implements TupleSet
{
	/**
	 * The directional index of ObjectIds by user set and relation.
	 */
	private Map<UserSet, Map<String, Set<ObjectId>>> usersetTree = new HashMap<>();

	/**
	 * The directional index of UserSets by objectId and relation.
	 */
	private Map<ObjectId, Map<String, Set<UserSet>>> objectTree = new HashMap<>();

	public InMemoryTupleSet()
	{
		super();
	}

	public InMemoryTupleSet(ObjectId objectId, String relation, Set<UserSet> usersets)
	throws InvalidTupleException
	{
		this();
		if (usersets == null) return;

		for (UserSet userset : usersets)
		{
			add(userset, relation, objectId);
		}
	}

	public InMemoryTupleSet(UserSet userset, String relation, Set<ObjectId> objectIds)
	throws InvalidTupleException
	{
		this();
		if (objectIds == null) return;

		for (ObjectId resource : objectIds)
		{
			add(userset, relation, resource);
		}
	}

	public InMemoryTupleSet(InMemoryTupleSet that)
	{
		this();
		this.usersetTree = new HashMap<>(that.usersetTree);
		this.objectTree = new HashMap<>(that.objectTree);
	}

	public InMemoryTupleSet(Collection<Tuple> tuples)
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
	 * Read all the usersets having a direct relation on an object ID.
	 */
	@Override
	public Set<UserSet> read(String relation, ObjectId objectId)
	{
		Map<String, Set<UserSet>> subtree = objectTree.get(objectId);
		if (subtree == null) return Collections.emptySet();

		Set<UserSet> tuples = subtree.get(relation);
		if (tuples == null) return Collections.emptySet();

		return Collections.unmodifiableSet(tuples);
	}

	/**
	 * Read all the usersets having a relation on an object ID, including indirect ACLs.
	 */
	// TODO: indirect ACLs are not currently returned by expand().
	@Override
	public Set<UserSet> expand(String relation, ObjectId objectId)
	{
		Set<UserSet> usersets = read(relation, objectId);
		Set<UserSet> results = new HashSet<>(usersets);
		
		//Recursively add other usersets with a relation component (e.g. group#member).
		//TODO: NOTE this only does one level of indirection--it should account for arbitrary depth/width of the tree.
		usersets.stream()
			.filter(UserSet::hasRelation)
			.forEach(u -> results.addAll(expand(u.getRelation(), u.getObjectId())));
		return results;
	}

	/**
	 * Read all the ObjectIds for objects a userset has with this relation directly.
	 */
	@Override
	public Set<ObjectId> read(UserSet userset, String relation)
	{
		Map<String, Set<ObjectId>> subtree = usersetTree.get(userset);
		if (subtree == null) return Collections.emptySet();

		Set<ObjectId> tuples = subtree.get(relation);
		return (tuples != null ? Collections.unmodifiableSet(tuples) : null);
	}

	/**
	 * Read a single tuple, navigating the user set tree.
	 */
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
		Map<String, Set<UserSet>> resourceSubtree = objectTree.get(resource);
		if (resourceSubtree == null) return null;

		// these are the usersets with the direct relation.
		Set<UserSet> resourceUsersets = resourceSubtree.get(relation);

		if (resourceUsersets == null) return null;

		//Check for direct membership...
		if (resourceUsersets.contains(userset))
		{
			return new Tuple(userset, relation, resource);
		}

		//Recursively check indirect memberships...
		if (resourceUsersets.stream().filter(t -> t.hasRelation()).anyMatch(t -> readOne(userset, t.getRelation(), t.getObjectId()) != null))
		{
			return new Tuple(userset, relation, resource);
		}

		return null;
	}

	public InMemoryTupleSet add(String userset, String relation, String resource)
	throws ParseException, InvalidTupleException
	{
		return add(UserSet.parse(userset), relation, new ObjectId(resource));
	}

	@Override
	public InMemoryTupleSet add(UserSet userset, String relation, ObjectId resource)
	throws InvalidTupleException
	{
		return add(newDynamicTuple(userset, relation, resource));
	}

	@Override
	public InMemoryTupleSet add(Tuple tuple)
	{
		writeUsersetTree(tuple);
		writeResourceTree(tuple);
		return this;
	}

	@Override
	public InMemoryTupleSet remove(Tuple tuple)
	{
		removeUsersetTree(tuple);
		removeResourceTree(tuple);
		return this;
	}

	@Override
	public InMemoryTupleSet remove(UserSet userset, String relation, ObjectId resource)
	{
		return remove(new Tuple(userset, relation, resource));
	}

	@Override
	public TupleSet copy()
	{
		return new InMemoryTupleSet(this);
	}

	private void writeUsersetTree(Tuple tuple)
	{
		Map<String, Set<ObjectId>> relationSubtree = usersetTree.computeIfAbsent(tuple.getUserset(), t -> new HashMap<>());
		Set<ObjectId> resources = relationSubtree.computeIfAbsent(tuple.getRelation(), s -> new HashSet<>());
		resources.add(tuple.getObjectId());
	}

	private void writeResourceTree(Tuple tuple)
	{
		Map<String, Set<UserSet>> relationSubtree = objectTree.computeIfAbsent(tuple.getObjectId(), t -> new HashMap<>());
		Set<UserSet> usersets = relationSubtree.computeIfAbsent(tuple.getRelation(), s -> new HashSet<>());
		usersets.add(tuple.getUserset());
	}

	private void removeUsersetTree(Tuple tuple)
	{
		Map<String, Set<ObjectId>> relationSubtree = usersetTree.get(tuple.getUserset());

		if (relationSubtree == null) return;

		Set<ObjectId> resources = relationSubtree.get(tuple.getRelation());

		if (resources == null) return;

		resources.remove(tuple.getObjectId());

		// If we just removed the last ObjectId in the set, prune the branch.
		if (resources.isEmpty())
		{
			relationSubtree.remove(tuple.getRelation());
		}
	}

	private void removeResourceTree(Tuple tuple)
	{
		Map<String, Set<UserSet>> relationSubtree = objectTree.get(tuple.getObjectId());

		if (relationSubtree == null) return;

		Set<UserSet> usersets = relationSubtree.get(tuple.getRelation());

		if (usersets == null) return;

		usersets.remove(tuple.getUserset());

		// If we just removed the last UserSet in the set, prune the branch.
		if (usersets.isEmpty())
		{
			relationSubtree.remove(tuple.getRelation());
		}
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
//			tupleset.stream().forEach(this::add);
		}

		return this;
	}
}
