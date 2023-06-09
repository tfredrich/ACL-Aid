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
		TupleSet rewrites = new LocalTupleSet();

		rules
			.stream()
			.map(rule -> rule.rewrite(set, tuple))
			.forEach(rewrites::addAll);
		return rewrites;
	}
}
