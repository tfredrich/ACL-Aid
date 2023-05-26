package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.Namespace;
import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.Relation;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.RewriteRuleImpl;

public class RelationBuilder
extends AbstractChildBuildable<NamespaceBuilder>
{
	private String name;
	private TupleSet rewriteRules = new LocalTupleSet(true);

	public RelationBuilder(String relation, NamespaceBuilder parent)
	{
		super(parent);
		this.name = relation;
	}

	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return (String.format("Relation: %s", name));
	}

	Relation build(Namespace namespace)
	{
		Relation r = new Relation(namespace, name);
		r.addRewriteRule(new RewriteRuleImpl(rewriteRules));
		return r;
	}

	public RelationBuilder childOf(String relation)
	throws ParseException
	{
		rewriteRules.add("relation:" + relation, "parent", "relation:" + name);
		return this;
	}
}
