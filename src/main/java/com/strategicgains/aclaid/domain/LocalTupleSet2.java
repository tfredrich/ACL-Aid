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
 * This is a TupleSet implementation that uses a local in-memory data structure
 * and is not persistent.
 * 
 * This implementation contains two indexes and follows the style implemented in
 * the Carta example:
 * https://medium.com/building-carta/user-authorization-in-less-than-10-milliseconds-f20d277fec47
 * 
 * @author Todd Fredrich
 */
public class LocalTupleSet2
implements TupleSet
{
	/**
	 * Index: MEMBER2GROUP containing direct relations.
	 */
	private Map<ObjectId, Map<String, Set<Tuple>>> memberToGroup = new ConcurrentHashMap<>();

	/**
	 * Index: GROUP2GROUP containing indirect relations.
	 */
	private Map<UserSet, Map<String, Set<Tuple>>> groupToGroup = new ConcurrentHashMap<>();

	/**
	 * The collection of tuples in this set.
	 */
	private List<Tuple> tuples = new ArrayList<>();

	public LocalTupleSet2()
	{
	}

	public LocalTupleSet2(ObjectId objectId, String relation, Set<UserSet> usersets)
	throws InvalidTupleException
	{
		this();
		if (usersets == null) return;

		for (UserSet userset : usersets)
		{
			add(userset, relation, objectId);
		}
	}

	public LocalTupleSet2(UserSet userset, String relation, Set<ObjectId> objectIds)
	throws InvalidTupleException
	{
		this();
		if (objectIds == null) return;

		for (ObjectId resource : objectIds)
		{
			add(userset, relation, resource);
		}
	}

	public LocalTupleSet2(LocalTupleSet2 that)
	{
		this(that.tuples);
	}

	public LocalTupleSet2(Collection<Tuple> tuples)
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

		Set<UserSet> direct = getDirectRelations(actor);

		if (hasDirectRelation(direct, relation, objectId)) return true;

		Set<UserSet> groups = getIndirectRelations(new UserSet(objectId));

		return intersects(direct, groups);
	}

	/**
	 * Answer whether the direct relations contain a relation to the given objectId.
	 * 
	 * @param direct   UserSets of direct relations.
	 * @param relation The relation to check.
	 * @param objectId The ObjectId to check.
	 * @return true if the relation exists.
	 */
	private boolean hasDirectRelation(Set<UserSet> direct, String relation, ObjectId objectId)
	{
		return direct.stream().anyMatch(d -> d.matches(objectId, relation));
	}

	/**
	 * Answer whether the two sets intersect.
	 * 
	 * @param direct UserSets of direct relations.
	 * @param groups UserSets of indirect relations.
	 * @return true if there is an intersection.
	 */
	private boolean intersects(Set<UserSet> direct, Set<UserSet> groups)
	{
		return direct.stream().anyMatch(d -> groups.stream().anyMatch(d::matches));
	}

	private Set<UserSet> getDirectRelations(UserSet actor)
	{
		Map<String, Set<Tuple>> relationSubtree = memberToGroup.get(actor.getObjectId());
		if (relationSubtree == null) return Collections.emptySet();

		return relationSubtree.values().stream()
			.flatMap(s -> s.stream().map(t -> new UserSet(t.getObjectId(), t.getRelation())))
			.collect(Collectors.toSet());
	}

	private Set<UserSet> getIndirectRelations(UserSet target)
	{
		Map<String, Set<Tuple>> targetSubtree = groupToGroup.get(target);
		if (targetSubtree == null) return Collections.emptySet();
	
		return targetSubtree.values().stream()
			.flatMap(s -> s.stream().map(Tuple::getUserset))
			.collect(Collectors.toSet());
	}

	public LocalTupleSet2 add(String userset, String relation, String resource)
	throws ParseException, InvalidTupleException
	{
		return add(UserSet.parse(userset), relation, new ObjectId(resource));
	}

	public LocalTupleSet2 add(UserSet userset, String relation, ObjectId resource)
	throws InvalidTupleException
	{
		return add(newDynamicTuple(userset, relation, resource));
	}

	public LocalTupleSet2 add(Tuple tuple)
	{
		tuples.add(tuple);
		writeMemberToGroup(tuple);
		writeGroupToGroup(tuple);
		return this;
	}

	public LocalTupleSet2 remove(Tuple tuple)
	{
		tuples.remove(tuple);
		removeMemberToGroup(tuple);
		removeGroupToGroup(tuple);
		return this;
	}

	public LocalTupleSet2 remove(UserSet userset, String relation, ObjectId resource)
	{
		return remove(new Tuple(userset, relation, resource));
	}

	private void writeMemberToGroup(Tuple tuple)
	{
		if (!tuple.isDirectRelation()) return;

		Map<String, Set<Tuple>> relationSubtree = memberToGroup.computeIfAbsent(tuple.getUsersetResource(), t -> new ConcurrentHashMap<>());
		Set<Tuple> resources = relationSubtree.computeIfAbsent(tuple.getRelation(), s -> new HashSet<>());
		resources.add(tuple);
	}

	private void writeGroupToGroup(Tuple tuple)
	{
		if (tuple.isDirectRelation()) return;

		Map<String, Set<Tuple>> relationSubtree = groupToGroup.computeIfAbsent(new UserSet(tuple.getObjectId()), t -> new HashMap<>());
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

	private Tuple newDynamicTuple(UserSet userset, String relation, ObjectId resource)
	throws InvalidTupleException
	{
		if (resource.isWildcard()) throw new InvalidTupleException("Tuple resources cannot contain wildcards: " + resource.toString());
		return new Tuple(userset, relation, resource);
	}

	@Override
	public boolean isEmpty() {
		return memberToGroup.isEmpty() && groupToGroup.isEmpty();
	}
}
