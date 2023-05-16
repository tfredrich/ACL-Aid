package com.strategicgains.aclaid.builder;

import java.util.ArrayList;
import java.util.List;

import com.strategicgains.aclaid.domain.Relation;

public class UsersetRewriteBuilder
extends AbstractChildBuildable<RelationBuilder>
{
	private List<UnionBuilder> unions;

	public UsersetRewriteBuilder(RelationBuilder parent)
	{
		super(parent);
	}

	public UnionBuilder union()
	{
		UnionBuilder builder = new UnionBuilder(this);

		if (unions == null)
		{
			unions = new ArrayList<>();
		}

		unions.add(builder);
		return builder;
	}

	public void apply(Relation r)
	{
		// TODO Auto-generated method stub		
	}
}
