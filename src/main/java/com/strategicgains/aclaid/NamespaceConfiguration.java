package com.strategicgains.aclaid;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import com.strategicgains.aclaid.domain.Relation;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

public class NamespaceConfiguration
{
	private AccessControlList rootAcl;
	private Set<Relation> relations = new HashSet<>();
	private TupleSet tuples;

	public NamespaceConfiguration(AccessControlList parent)
	{
		super();
		this.rootAcl = parent;
	}

	public void addRelation(Relation relation)
	{
		relations.add(relation);
	}

	public boolean containsRelation(String relation)
	{
		return relations.stream().anyMatch(r -> r.getName().equals(relation));
	}

	public boolean check(AccessControlList acl, String userset, String relation, String resource)
	throws ParseException
	{
		return check(acl, UserSet.parse(userset), relation, ResourceName.parse(resource));
	}

	public boolean check(AccessControlList acl, UserSet userset, String relation, ResourceName resource)
	{
//		for (Tuple tuple : tuples)
//		{
//			if (tuple.matches(userset, relation, resource))
//			{
//				return true;
//			}
//
//			//TODO: beware the recursion stack overflow!
//			if (tuple.getUserset().hasRelation()
//				&& tuple.applies(resource, relation)
//				&& acl.check(userset, tuple.getUserset().getRelation(), tuple.getUserset().getResource()))
//				return true;
//		}

		return false;
	}
}
