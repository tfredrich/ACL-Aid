package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class ComputedUserSet
extends AbstractChild
{
	private String relation;

	public ComputedUserSet(RelationDefinition parent, String relation)
	{
		super(parent);
		setRelation(relation);
	}

	private void setRelation(String relation)
	{
		this.relation = relation;
	}

	@Override
	public TupleSet rewrite(TupleSet tuples, Tuple tuple)
	{
		TupleSet rewrites = new LocalTupleSet();
		TupleSet subset = tuples.read(relation, tuple.getResource());

		if (subset != null)
		{
			subset.stream().map(this::rewrite).forEach(rewrites::add);
		}

		return rewrites;
	}

	private Tuple rewrite(Tuple t)
	{
		return new Tuple(t.getUserset(), getParent().getName(), t.getResource());
	}
}
