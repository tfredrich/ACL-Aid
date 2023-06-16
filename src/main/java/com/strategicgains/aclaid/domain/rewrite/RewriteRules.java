package com.strategicgains.aclaid.domain.rewrite;

import java.util.ArrayList;
import java.util.List;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class RewriteRules
implements RewriteRule
{
	private List<RewriteRule> rules = new ArrayList<>();

	public void add(RewriteRule rule)
	{
		rules.add(rule);
	}

	@Override
	public TupleSet rewrite(TupleSet set, Tuple tuple)
	{
		// TODO: Remove this duplicate code (duplicated in Union.rewrite())
		TupleSet rewrites = new LocalTupleSet();
		rules
			.stream()
			.map(r -> r.rewrite(set, tuple))
			.forEach(rewrites::addAll);
		return rewrites;
	}
}