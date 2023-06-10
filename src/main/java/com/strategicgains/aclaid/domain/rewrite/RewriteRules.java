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
		TupleSet intermediate = set;

		for (RewriteRule rule : rules)
		{
			intermediate = rule.rewrite(intermediate, tuple);
			rewrites.addAll(intermediate);
		}

		return rewrites;
	}
}
