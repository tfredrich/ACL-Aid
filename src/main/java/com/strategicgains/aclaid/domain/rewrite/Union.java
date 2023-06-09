package com.strategicgains.aclaid.domain.rewrite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

public class Union
implements RewriteRule
{
	private List<Child> children = new ArrayList<>();

	public Union child(Child child)
	{
		children.add(child);
		return this;
	}

	private TupleSet union(Child a, Child b)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TupleSet rewrite(TupleSet set, Tuple tuple)
	{
		if (children.isEmpty()) return new LocalTupleSet();

		Iterator<Child> iterator = children.iterator();
		Child a = iterator.next();
		TupleSet result = null;
		while(iterator.hasNext())
		{
			result = union(a, iterator.next());
		}
		return result;
	}
}
