package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.strategicgains.aclaid.exception.InvalidTupleException;

/**
 * This is a TupleStore implementation that uses a local in-memory data structure
 * and is not persistent.
 * 
 * This implementation contains two indexes and follows the style implemented in
 * the Carta example:
 * https://medium.com/building-carta/user-authorization-in-less-than-10-milliseconds-f20d277fec47
 * 
 * @author Todd Fredrich
 */
public class SimpleTupleStore
implements TupleStore
{
	/**
	 * Index: MEMBER2GROUP containing direct relations from a User.
	 */
	private Map<ObjectId, Map<String, Set<Tuple>>> memberToGroup = new ConcurrentHashMap<>();

	/**
	 * Index: GROUP2GROUP containing indirect relations from a UserSet.
	 */
	private Map<UserSet, Map<String, Set<Tuple>>> groupToGroup = new ConcurrentHashMap<>();

	/**
	 * Reverse Index: OBJECT2GROUP containing direct relations by objectId.
	 * This is used to quickly find all direct tuples related to a specific objectId.
	 */
	private Map<ObjectId, Map<String, Set<Tuple>>> objectToGroup = new ConcurrentHashMap<>();

	/**
	 * Reverse Index: OBJECT2OBJECT containing indirect relations by objectId.
	 * This is used to quickly find all indirect tuples related to a specific objectId.
	 */
	private Map<ObjectId, Map<String, Set<Tuple>>> objectToObject = new ConcurrentHashMap<>();

	/**
	 * The collection of tuples in this set.
	 */
	private List<Tuple> tuples = new ArrayList<>();

	public SimpleTupleStore()
	{
	}

	public SimpleTupleStore(ObjectId objectId, String relation, Set<UserSet> usersets)
	throws InvalidTupleException
	{
		this();
		if (usersets == null) return;

		for (UserSet userset : usersets)
		{
			add(userset, relation, objectId);
		}
	}

	public SimpleTupleStore(UserSet userset, String relation, Set<ObjectId> objectIds)
	throws InvalidTupleException
	{
		this();
		if (objectIds == null) return;

		for (ObjectId resource : objectIds)
		{
			add(userset, relation, resource);
		}
	}

	public SimpleTupleStore(SimpleTupleStore that)
	{
		this(that.tuples);
	}

	public SimpleTupleStore(Collection<Tuple> tuples)
	{
		this();
		tuples.stream().forEach(this::add);
	}

	/**
	 * Answer whether the actor has the given relation to the objectId, following
	 * the relation tree as needed. Parses the actor and objectId strings into
	 * UserSet and ObjectId objects, respectively before calling
	 * {@link #check(UserSet, String, ObjectId)}.
	 * 
	 * @param actor    The UserSet acting on the objectId.
	 * @param relation The relation to check.
	 * @param objectId The ObjectId to check.
	 * @return true if the relation exists.
	 */
	public boolean check(String actor, String relation, String objectId)
	throws ParseException
	{
		return check(UserSet.parse(actor), relation, new ObjectId(objectId));
	}

	/**
	 * Answer whether the actor has the given relation to the objectId, following
	 * the relation tree as needed.
	 * 
	 * @param actor    The UserSet acting on the objectId.
	 * @param relation The relation to check.
	 * @param objectId The ObjectId to check.
	 * @return true if the relation exists.
	 */
	@Override
	public boolean check(UserSet actor, String relation, ObjectId objectId)
	{
		if (actor == null || relation == null || objectId == null) return false;

		Set<Tuple> direct = getDirectTuples(actor);
		if (hasDirectRelation(direct, relation, objectId)) return true;

		Set<Tuple> indirect = getIndirectTuples(objectId, relation);
		return intersects(direct, indirect);
	}

	@Override
	public TupleStore read(TupleSet tupleSet)
	{
		if (tupleSet.isEmpty() || !tupleSet.isValid()) throw new IllegalArgumentException("Invalid TupleSet: " + tupleSet);

		Set<Tuple> tuples = new HashSet<>();

		if (tupleSet.isSingleTupleKey())
		{
			Tuple tuple = readOne(tupleSet.getUserset(), tupleSet.getRelation(), tupleSet.getObject());

			if (tuple != null)
			{
				tuples.add(tuple);
			}
		}
		else if (tupleSet.hasObject())
		{
			if (tupleSet.hasRelation())
			{
				tuples.addAll(readAll(tupleSet.getObject(), tupleSet.getRelation()));
			}
			else
			{
				tuples.addAll(readAll(tupleSet.getObject()));
			}
		}
		else if (tupleSet.hasUserset())
		{
			if (tupleSet.hasRelation())
			{
				tuples.addAll(readAll(tupleSet.getUserset(), tupleSet.getRelation()));
			}
			else
			{
				tuples.addAll(readAll(tupleSet.getUserset()));
			}
		}

		return new SimpleTupleStore(tuples);
	}

	public Tuple readOne(String userset, String relation, String objectId)
	throws ParseException
	{
		return readOne(UserSet.parse(userset), relation, new ObjectId(objectId));
	}

	public Tuple readOne(UserSet userset, String relation, ObjectId objectId)
	{
		if (userset == null || relation == null || objectId == null) return null;

		return getDirectTuples(userset, relation, objectId).stream().findFirst().orElse(null);
	}

	public Set<Tuple> readAll(ObjectId objectId, String relation)
	{
		return getDirectTuples(objectId, relation);
	}

	public Set<Tuple> readAll(ObjectId objectId)
	{
		return getDirectTuples(objectId);
	}

	public Set<Tuple> readAll(UserSet userset, String relation)
	{
		return getDirectTuples(userset, relation);
	}

	public Set<Tuple> readAll(UserSet userset)
	{
		return getDirectTuples(userset);
	}

	/**
	 * Answer whether the direct relations contain a relation to the given objectId.
	 * 
	 * @param direct   UserSets of direct relations.
	 * @param relation The relation to check.
	 * @param objectId The ObjectId to check.
	 * @return true if the relation exists.
	 */
	private boolean hasDirectRelation(Set<Tuple> direct, String relation, ObjectId objectId)
	{
		return direct.stream().anyMatch(t -> t.appliesTo(objectId) && t.getRelation().equals(relation));
	}

	/**
	 * Answer whether the two sets intersect.
	 * 
	 * @param direct Tuples of direct relations.
	 * @param indirect Tuples of indirect relations.
	 * @return true if there is an intersection.
	 */
	private boolean intersects(Set<Tuple> direct, Set<Tuple> indirect)
	{
		return direct.stream().anyMatch(d -> indirect.stream().anyMatch(i -> i.getUserset().matches(d.getObjectId(), d.getRelation())));
	}

	private Set<Tuple> getDirectTuples(UserSet userset, String relation, ObjectId objectId)
	{
		if (userset == null || relation == null || objectId == null) return Collections.emptySet();

		Map<String, Set<Tuple>> relationSubtree = memberToGroup.get(userset.getObjectId());
		if (relationSubtree == null) return Collections.emptySet();

		Set<Tuple> relationTuples = relationSubtree.get(relation);
		if (relationTuples == null || relationTuples.isEmpty()) return Collections.emptySet();

		return relationTuples.stream()
			.filter(t -> t.appliesTo(objectId))
			.collect(Collectors.toSet());
	}

	private Set<Tuple> getDirectTuples(UserSet userset, String relation)
	{
		if (userset == null || relation == null) return Collections.emptySet();

		return getDirectTuples(userset.getObjectId(), relation);
	}

	/**
	 * Answer the set of tuples that have direct relations from the actor.
	 * 
	 * @param userset The UserSet acting on the objectId.
	 * @return The set of tuples that have direct relations from the actor.
	 */
	private Set<Tuple> getDirectTuples(UserSet userset)
	{
		if (userset == null) return Collections.emptySet();

		return getDirectTuples(userset.getObjectId());
	}

	private Set<Tuple> getDirectTuples(ObjectId objectId, String relation)
	{
		Map<String, Set<Tuple>> relationSubtree = objectToGroup.get(objectId);
		if (relationSubtree == null) return Collections.emptySet();

		Set<Tuple> relationTuples = relationSubtree.get(relation);
		if (relationTuples == null || relationTuples.isEmpty()) return Collections.emptySet();

		return relationTuples;
	}

	private Set<Tuple> getDirectTuples(ObjectId objectId)
	{
		Map<String, Set<Tuple>> relationSubtree = objectToGroup.get(objectId);
		if (relationSubtree == null) return Collections.emptySet();

		return relationSubtree.values().stream()
			.flatMap(s -> s.stream())
			.collect(Collectors.toSet());
	}

	/**
	 * Answer the set of tuples that have indirect relations from the actor.
	 * 
	 * @param actor The UserSet acting on the objectId.
	 * @return The set of tuples that have indirect relations from the actor.
	 */
	private Set<Tuple> getIndirectTuples(ObjectId target, String relation)
	{
		Map<String, Set<Tuple>> targetSubtree = groupToGroup.get(new UserSet(target));
		if (targetSubtree == null) return Collections.emptySet();

		// Ensure at least on relation exists to the object.
		Set<Tuple> relationTuples = targetSubtree.get(relation);
		if (relationTuples == null || relationTuples.isEmpty()) return Collections.emptySet();

		return targetSubtree.values().stream()
			.flatMap(s -> s.stream())
			.collect(Collectors.toSet());
	}

	public SimpleTupleStore add(String userset, String relation, String resource)
	throws ParseException, InvalidTupleException
	{
		return add(UserSet.parse(userset), relation, new ObjectId(resource));
	}

	public SimpleTupleStore add(UserSet userset, String relation, ObjectId resource)
	throws InvalidTupleException
	{
		return add(newLocalTuple(userset, relation, resource));
	}

	public SimpleTupleStore add(Tuple tuple)
	{
		tuples.add(tuple);
		addMemberToGroup(tuple);
		addGroupToGroup(tuple);
		addObjectToGroup(tuple);
		addObjectToObject(tuple);
		return this;
	}

	public SimpleTupleStore remove(Tuple tuple)
	{
		tuples.remove(tuple);
		removeMemberToGroup(tuple);
		removeGroupToGroup(tuple);
		removeObjectToGroup(tuple);
		removeObjectToObject(tuple);
		return this;
	}

	public SimpleTupleStore remove(UserSet userset, String relation, ObjectId resource)
	{
		return remove(new Tuple(userset, relation, resource));
	}

	private void addMemberToGroup(Tuple tuple)
	{
		if (!tuple.isDirectRelation()) return;

		Map<String, Set<Tuple>> relationSubtree = memberToGroup.computeIfAbsent(tuple.getUsersetResource(), t -> new ConcurrentHashMap<>());
		Set<Tuple> resources = relationSubtree.computeIfAbsent(tuple.getRelation(), s -> new HashSet<>());
		resources.add(tuple);
	}

	private void addGroupToGroup(Tuple tuple)
	{
		if (tuple.isDirectRelation()) return;

		Map<String, Set<Tuple>> relationSubtree = groupToGroup.computeIfAbsent(new UserSet(tuple.getObjectId()), t -> new HashMap<>());
		Set<Tuple> usersets = relationSubtree.computeIfAbsent(tuple.getRelation(), s -> new HashSet<>());
		usersets.add(tuple);
	}

	private void addObjectToGroup(Tuple tuple)
	{
		if (!tuple.isDirectRelation()) return;

		Map<String, Set<Tuple>> relationSubtree = objectToGroup.computeIfAbsent(tuple.getObjectId(), t -> new ConcurrentHashMap<>());
		Set<Tuple> resources = relationSubtree.computeIfAbsent(tuple.getRelation(), s -> new HashSet<>());
		resources.add(tuple);
	}

	private void addObjectToObject(Tuple tuple)
	{
		if (tuple.isDirectRelation()) return;

		Map<String, Set<Tuple>> relationSubtree = objectToObject.computeIfAbsent(tuple.getObjectId(), t -> new HashMap<>());
		Set<Tuple> usersets = relationSubtree.computeIfAbsent(tuple.getRelation(), s -> new HashSet<>());
		usersets.add(tuple);
	}

	private void removeMemberToGroup(Tuple tuple)
	{
		Map<String, Set<Tuple>> relationSubtree = memberToGroup.get(tuple.getUsersetResource());

		if (relationSubtree == null) return;

		Set<Tuple> resources = relationSubtree.get(tuple.getRelation());

		if (resources == null) return;

		resources.remove(tuple);

		// If we just removed the last ObjectId in the set, prune the branch.
		if (resources.isEmpty())
		{
			relationSubtree.remove(tuple.getRelation());
		}
	}

	private void removeGroupToGroup(Tuple tuple)
	{
		Map<String, Set<Tuple>> relationSubtree = groupToGroup.get(new UserSet(tuple.getObjectId()));

		if (relationSubtree == null) return;

		Set<Tuple> usersets = relationSubtree.get(tuple.getRelation());

		if (usersets == null) return;

		usersets.remove(tuple);

		// If we just removed the last UserSet in the set, prune the branch.
		if (usersets.isEmpty())
		{
			relationSubtree.remove(tuple.getRelation());
		}
	}

	private void removeObjectToGroup(Tuple tuple)
	{
		Map<String, Set<Tuple>> relationSubtree = objectToGroup.get(tuple.getObjectId());

		if (relationSubtree == null) return;

		Set<Tuple> resources = relationSubtree.get(tuple.getRelation());

		if (resources == null) return;

		resources.remove(tuple);

		// If we just removed the last ObjectId in the set, prune the branch.
		if (resources.isEmpty())
		{
			relationSubtree.remove(tuple.getRelation());
		}
	}

	private void removeObjectToObject(Tuple tuple)
	{
		Map<String, Set<Tuple>> relationSubtree = objectToObject.get(tuple.getObjectId());

		if (relationSubtree == null) return;

		Set<Tuple> usersets = relationSubtree.get(tuple.getRelation());

		if (usersets == null) return;

		usersets.remove(tuple);

		// If we just removed the last UserSet in the set, prune the branch.
		if (usersets.isEmpty())
		{
			relationSubtree.remove(tuple.getRelation());
		}
	}

	private Tuple newLocalTuple(UserSet userset, String relation, ObjectId resource)
	throws InvalidTupleException
	{
		if (resource.isWildcard()) throw new InvalidTupleException("Tuple resources cannot contain wildcards: " + resource.toString());
		return new Tuple(userset, relation, resource);
	}

	@Override
	public boolean isEmpty()
	{
		return tuples.isEmpty();
	}
}
