package com.strategicgains.aclaid.builder;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import com.strategicgains.aclaid.domain.ChildOfRewriteRule;
import com.strategicgains.aclaid.domain.Relation;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.Rule;
import com.strategicgains.aclaid.domain.RuleSet;
import com.strategicgains.aclaid.domain.RulesSetRewriteRule;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class UsersetRewriteBuilder
{
	private RelationBuilder parentBuilder;
	private Set<String> parentRelations;
	private RuleSet rules;

	public UsersetRewriteBuilder(RelationBuilder parent)
	{
		super();
		this.parentBuilder = parent;
	}

	public UsersetRewriteBuilder childOf(String parentRelation)
	{
		if (this.parentRelations == null)
		{
			this.parentRelations = new HashSet<>();
		}

		this.parentRelations.add(parentRelation);
		return this;
	}

	public UsersetRewriteBuilder rule(String userset, String relation, String resource)
	throws ParseException
	{
		return rule(UserSet.parse(userset), relation, ResourceName.parse(resource));
	}

	public UsersetRewriteBuilder rule(UserSet userset, String relation, ResourceName resource)
	{
		if (rules == null)
		{
			rules = new RuleSet();
		}

		rules.add(new Rule(userset, relation, resource));
		return this;
	}

	public UsersetRewriteBuilder tupleToUserSet(String relation, TUPLE tupleObject)
	{
		// TODO Auto-generated method stub
		return this;
	}

	public NamespaceConfigurationBuilder namespace(String namespace)
	{
		return parentBuilder.namespace(namespace);
	}

	public RelationBuilder relation(String name)
	{
		return parentBuilder.relation(name);
	}

	public NamespaceConfigurationBuilder tuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException, InvalidTupleException
	{
		return parentBuilder.tuple(userset, relation, resource);
	}

	public void apply(Relation r)
	{
		addChildRewriteRules(r);
		addRuleRewrites(r);
	}

	private void addChildRewriteRules(Relation r)
	{
		if (parentRelations != null)
		{
			parentRelations.stream().forEach(p -> r.addRewriteRule(new ChildOfRewriteRule(p)));
		}
	}

	private void addRuleRewrites(Relation r)
	{
		if (rules != null)
		{
			r.addRewriteRule(new RulesSetRewriteRule(rules));
		}
	}

	public TupleBuilder tuples()
	{
		return parentBuilder.tuples();
	}
}
