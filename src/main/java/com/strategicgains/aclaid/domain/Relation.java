package com.strategicgains.aclaid.domain;

import java.util.ArrayList;
import java.util.List;

import com.strategicgains.aclaid.NamespaceConfiguration;

public class Relation
{
	private NamespaceConfiguration namespace;
	private String name;
	private List<UsersetRewriteRule> usersetRewriteRules;

	public Relation(NamespaceConfiguration namespace, String name)
	{
		super();
		this.namespace = namespace;
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String toString()
	{
		return String.format("Relation: %s", name);
	}

	public void addRewriteRule(UsersetRewriteRule rule)
	{
		if (usersetRewriteRules == null)
		{
			this.usersetRewriteRules = new ArrayList<>();
		}

		this.usersetRewriteRules.add(rule);
	}

	public boolean checkUsersetRewrites(UserSet userset, String relation, ResourceName resource)
	{
		return (usersetRewriteRules != null ? usersetRewriteRules.stream().anyMatch(r -> r.matches(namespace, userset, relation, resource)) : false);
	}
}
