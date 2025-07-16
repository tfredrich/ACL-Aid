package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.strategicgains.aclaid.exception.InvalidTupleException;

/**
 * A LocalTupleStore is a graph of of tuples that can be read and written to. It is a simple in-memory
 * implementation of a TupleStore and is not thread-safe and is not durable.
 * 
 * There are two indexes maintained by LocalTupleStore, both being essentially adjacency lists:
 * 1) From a UserSet to Relations to ObjectIds and
 * 2) From an ObjectID to Relations to UserSets
 * 
 * This gives us the ability to navigate either way through the tuples to navigate the graph.
 * 
 * @author Todd Fredrich
 */
public class LocalTupleStore
implements TupleStore
{
	/**
	 * The directional index of ObjectIds by user set and relation:
	 * Essentially MEMBER2GROUP containing direct relations.
	 */
	private Map<UserSet, Map<String, Set<ObjectId>>> usersetTree; 

	/**
	 * The directional index of UserSets by objectId and relation.
	 * Essentially GROUP2GROUP containing indirect relations.
	 */
	private Map<ObjectId, Map<String, Set<UserSet>>> objectTree; 

	public LocalTupleStore()
	{
		this(null, null);
	}

	public LocalTupleStore(ObjectId objectId, String relation, Set<UserSet> usersets)
	throws InvalidTupleException
	{
		this();
		if (usersets == null) return;

		for (UserSet userset : usersets)
		{
			add(userset, relation, objectId);
		}
	}

	public LocalTupleStore(UserSet userset, String relation, Set<ObjectId> objectIds)
	throws InvalidTupleException
	{
		this();
		if (objectIds == null) return;

		for (ObjectId resource : objectIds)
		{
			add(userset, relation, resource);
		}
	}

	public LocalTupleStore(LocalTupleStore that)
	{
		this(that.usersetTree, that.objectTree);
	}

	public LocalTupleStore(Collection<Tuple> tuples)
	{
		this();
		tuples.stream().forEach(this::add);
	}

	protected LocalTupleStore(Map<UserSet, Map<String, Set<ObjectId>>> usersetTree,
		Map<ObjectId, Map<String, Set<UserSet>>> objectTree)
	{
		this.usersetTree = usersetTree != null ? new ConcurrentHashMap<>(usersetTree) : new ConcurrentHashMap<>();
		this.objectTree = objectTree != null ? new ConcurrentHashMap<>(objectTree) : new ConcurrentHashMap<>();
	}

	@Override
	public boolean isEmpty() {
		return usersetTree.isEmpty() && objectTree.isEmpty();
	}

	/**
	 * Read all the usersets having a direct relation on an object ID.
	 */
	public Set<UserSet> readUserSets(String relation, ObjectId objectId)
	{
		Map<String, Set<UserSet>> subtree = objectTree.get(objectId);
		if (subtree == null) return Collections.emptySet();

		Set<UserSet> usersets = subtree.get(relation);
		if (usersets == null) return Collections.emptySet();

		return Collections.unmodifiableSet(usersets);
	}

	/**
	 * Read all the relation tuples having a direct relation on an object ID.
	 * 
	 * @param relation
	 * @param objectId
	 * @return a TupleStore of all the relation tuples having a direct relation on an object ID.
	 */
	public TupleStore readAll(String relation, ObjectId objectId)
	{
		Set<UserSet> usersets = readUserSets(relation, objectId);
		LocalTupleStore results = new LocalTupleStore();
		usersets.stream().forEach(u -> {
			try {
				results.add(u, relation, objectId);
			} catch (InvalidTupleException e) {
				e.printStackTrace();
			}
		});
		return results;
	}

	/**
	 * Read all the usersets having a relation on an object ID, including indirect ACLs.
	 */
	// TODO: indirect ACLs are not currently returned by expand().
	public Set<UserSet> expandUserSets(String relation, ObjectId objectId)
	{
		Set<UserSet> usersets = readUserSets(relation, objectId);
		Set<UserSet> results = new HashSet<>(usersets);

		//Recursively add usersets with a relation component (e.g. group#member).
		//TODO: Look out for deep (and wide) relationship graphs.
		usersets.stream()
			.forEach(u -> {
				if (u.hasRelation())
				{
					results.addAll(expandUserSets(u.getRelation(), u.getObjectId()));
				}
				else
				{
					results.addAll(readUserSets(relation, u.getObjectId()));
				}
			});
		return results;
	}

	/**
	 * Read all the ObjectIds for objects a userset has with this relation directly.
	 */
//	@Override
//	public Set<ObjectId> read(UserSet userset, String relation)
//	{
//		Map<String, Set<ObjectId>> subtree = usersetTree.get(userset);
//		if (subtree == null) return Collections.emptySet();
//
//		Set<ObjectId> tuples = subtree.get(relation);
//		return (tuples != null ? Collections.unmodifiableSet(tuples) : null);
//	}

	/**
	 * Read a single tuple, navigating the user set tree.
	 */
	public Tuple readOne(String userset, String relation, String resource)
	throws ParseException
	{
		return read(UserSet.parse(userset), relation, new ObjectId(resource));
	}

	/**
	 * Read a single tuple, navigating the user set tree.
	 */
	public Tuple read(UserSet userset, String relation, ObjectId objectId)
	{
		Map<String, Set<UserSet>> objectSubtree = objectTree.get(objectId);
		if (objectSubtree == null) return null;

		// these are the usersets with the direct relation.
		Set<UserSet> usersets = objectSubtree.get(relation);

		if (usersets == null) return null;

		//Check for direct membership...
		if (usersets.contains(userset))
		{
			return new Tuple(userset, relation, objectId);
		}

		//Recursively check indirect memberships...
		//TODO: Look out for deep (and wide) relationship graphs.
		if (usersets.stream()
			.filter(UserSet::hasRelation)
			.anyMatch(t -> read(userset, t.getRelation(), t.getObjectId()) != null))
		{
			return new Tuple(userset, relation, objectId);
		}

		return null;
	}

	public LocalTupleStore add(String userset, String relation, String resource)
	throws ParseException, InvalidTupleException
	{
		return add(UserSet.parse(userset), relation, new ObjectId(resource));
	}

	@Override
	public LocalTupleStore add(UserSet userset, String relation, ObjectId resource)
	throws InvalidTupleException
	{
		return add(newDynamicTuple(userset, relation, resource));
	}

	@Override
	public LocalTupleStore add(Tuple tuple)
	{
		writeUsersetTree(tuple);
		writeResourceTree(tuple);
		return this;
	}

	@Override
	public LocalTupleStore remove(Tuple tuple)
	{
		removeUsersetTree(tuple);
		removeResourceTree(tuple);
		return this;
	}

	@Override
	public LocalTupleStore remove(UserSet userset, String relation, ObjectId resource)
	{
		return remove(new Tuple(userset, relation, resource));
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

	public LocalTupleStore addAll(LocalTupleStore tupleset)
	{
		if (tupleset != null)
		{
			tupleset.stream().forEach(this::add);
		}

		return this;
	}

	private List<Tuple> stream()
	{
		return usersetTree.entrySet().stream()
			.flatMap(e -> e.getValue().entrySet().stream()
			.flatMap(f -> f.getValue().stream().map(o -> new Tuple(e.getKey(), f.getKey(), o))))
			.collect(java.util.stream.Collectors.toList());
	}

	@Override
	public boolean check(UserSet userset, String relation, ObjectId objectId) {
		return read(userset, relation, objectId) != null;
	}

	@Override
	public TupleStore read(TupleSet tupleSet) {
		// TODO Auto-generated method stub
		return null;
	}
}
