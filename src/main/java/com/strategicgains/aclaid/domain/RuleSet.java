package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class RuleSet
{
	private LinkedList<UserSet> usersets = new LinkedList<>();
	private Map<UserSet, Map<String, Set<ResourceName>>> usersetTree = new HashMap<>();
	private Map<ResourceName, Map<String, Set<UserSet>>> resourceTree = new HashMap<>();

	public RuleSet()
	{
		super();
	}

	protected RuleSet(ResourceName resource, String relation, Set<UserSet> usersets)
	{
		this();
		if (usersets == null) return;

		usersets.stream().forEach(userset -> {
			add(userset, relation, resource);
		});
	}

	protected RuleSet(UserSet userset, String relation, Set<ResourceName> resources)
	{
		this();
		if (resources == null) return;

		resources.stream().forEach(resource -> {
			add(userset, relation, resource);
		});
	}

	// Read a single Rule.
	public Rule readOne(String userset, String relation, String resource)
	throws ParseException
	{
		return readOne(UserSet.parse(userset), relation, new ResourceName(resource));
	}

	// Read a single Rule.
	public Rule readOne(UserSet userset, String relation, ResourceName resource)
	{
		Map<String, Set<UserSet>> subtree1 = resourceTree.get(resource);

		if (subtree1 == null) return null;

		// these are the usersets with the direct relation.
		Set<UserSet> usersets = subtree1.get(relation);

		if (usersets == null) return null;
		if (usersets.stream().anyMatch(u -> u.matches(userset)))
		{
			return new Rule(userset, relation, resource);
		}

		//Recursively check memberships...
		if (usersets.stream().filter(set -> set.hasRelation()).map(set -> readOne(userset, set.getRelation(), set.getResource()))
			.filter(t -> (t != null)).count() > 0)
		{
			return new Rule(userset, relation, resource);
		}

		return null;
	}

	public RuleSet add(String userset, String relation, String resource)
	throws ParseException
	{
		return add(UserSet.parse(userset), relation, new ResourceName(resource));
	}

	public RuleSet add(UserSet userset, String relation, ResourceName resource)
	{
		return add(new Rule(userset, relation, resource));
	}

	public RuleSet add(Rule rule)
	{
		writeUsersetTree(rule);
		writeResourceTree(rule);
		usersets.add(rule.getUserset());
		return this;
	}

	public RuleSet remove(Rule rule)
	{
		removeUsersetTree(rule);
		removeResourceTree(rule);
		return this;
	}

	public RuleSet remove(UserSet userset, String relation, ResourceName resource)
	{
		return remove(new Rule(userset, relation, resource));
	}

	private void writeUsersetTree(Rule rule)
	{
		Map<String, Set<ResourceName>> relationSubtree = usersetTree.computeIfAbsent(rule.getUserset(), t -> new HashMap<>());
		Set<ResourceName> resources = relationSubtree.computeIfAbsent(rule.getRelation(), s -> new HashSet<>());
		resources.add(rule.getResource());
	}

	private void writeResourceTree(Rule rule)
	{
		Map<String, Set<UserSet>> relationSubtree = resourceTree.computeIfAbsent(rule.getResource(), t -> new HashMap<>());
		Set<UserSet> usersets = relationSubtree.computeIfAbsent(rule.getRelation(), s -> new HashSet<>());
		usersets.add(rule.getUserset());
	}

	private void removeUsersetTree(Rule rule)
	{
		Map<String, Set<ResourceName>> relationSubtree = usersetTree.get(rule.getUserset());

		if (relationSubtree == null) return;

		Set<ResourceName> resources = relationSubtree.computeIfAbsent(rule.getRelation(), s -> new HashSet<>());

		if (resources == null) return;

		resources.remove(rule.getResource());
	}

	private void removeResourceTree(Rule rule)
	{
		Map<String, Set<UserSet>> relationSubtree = resourceTree.get(rule.getResource());

		if (relationSubtree == null) return;

		Set<UserSet> usersets = relationSubtree.computeIfAbsent(rule.getRelation(), s -> new HashSet<>());

		if (usersets == null) return;

		usersets.remove(rule.getUserset());
	}
}
