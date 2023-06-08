package com.strategicgains.aclaid.domain.rewrite;

import java.util.ArrayList;
import java.util.List;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

public class RewriteRules
{
	private List<ComputedUserSet> computedUserSets = new ArrayList<>();

	public TupleSet rewrite(TupleSet tuples, UserSet userset)
	{
		TupleSet rewrites = new LocalTupleSet();

		computedUserSets.stream().forEach(u -> {
			TupleSet rewrite = tuples.read(userset, u.getParent());
			rewrite.stream().forEach(t -> rewrites.add(u.compute(t)));
		});

		return rewrites;
	}

	public void add(ComputedUserSet computedUserSet)
	{
		computedUserSets.add(computedUserSet);
	}
}
