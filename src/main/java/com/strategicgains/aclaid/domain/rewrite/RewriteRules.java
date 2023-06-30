package com.strategicgains.aclaid.domain.rewrite;

import java.util.ArrayList;
import java.util.List;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class RewriteRules
implements Rewritable
{
	private List<Rewritable> rules = new ArrayList<>();

	public void add(Rewritable rule)
	{
		rules.add(rule);
	}

	@Override
	public TupleSet rewrite(TupleSet inputSet, Tuple tupleKey)
	{
		// TODO: Remove this duplicate code (duplicated in Union.rewrite())
		TupleSet rewrites = new LocalTupleSet();
		rules
			.stream()
			.map(r -> r.rewrite(inputSet, tupleKey))
			.forEach(rewrites::addAll);
		return rewrites;
	}
}
