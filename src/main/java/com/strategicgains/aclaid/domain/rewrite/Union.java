package com.strategicgains.aclaid.domain.rewrite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.TupleSet;

public class Union
implements Computable<TupleSet>
{
	private List<Child<?>> children = new ArrayList<>();

	public Union child(Child<?> child)
	{
		children.add(child);
		return this;
	}

	@Override
	public TupleSet compute()
	{
		if (children.isEmpty()) return new LocalTupleSet();

		Iterator<Child<?>> iterator = children.iterator();
		Child<?> a = iterator.next();
		TupleSet result = null;
		while(iterator.hasNext())
		{
			result = union(a, iterator.next());
		}
		return result;
	}

	private TupleSet union(Child<?> a, Child<?> b)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
