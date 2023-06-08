package com.strategicgains.aclaid.domain.rewrite;

import java.util.ArrayList;
import java.util.List;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;

public class RewriteRules
{
	private List<ComputedUserSet> computedUserSets = new ArrayList<>();

	public TupleSet rewrite(TupleSet tuples, UserSet userset, String originalRelation)
	{
		TupleSet rewrites = new LocalTupleSet();

		computedUserSets.stream().forEach(u -> {
			TupleSet rewrite = tuples.read(userset, u.getRelation());
			rewrite.stream().forEach(t -> {
				try
				{
					rewrites.add(new Tuple(t.getUserset(), originalRelation, t.getResource()));
				}
				catch (InvalidTupleException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		});

		return rewrites;
	}

	public void add(ComputedUserSet computedUserSet)
	{
		computedUserSets.add(computedUserSet);
	}
}
