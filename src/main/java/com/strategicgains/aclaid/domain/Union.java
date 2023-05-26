package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Union
implements TupleSet
{
	private List<TupleSet> unions = new ArrayList<>();

	public Union(TupleSet... tuplesets)
	{
		super();
		unions.addAll(Arrays.asList(tuplesets));
	}

	@Override
	public int size()
	{
		return unions.size();
	}

	@Override
	public TupleSet read(String relation, ResourceName resource)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TupleSet read(UserSet userset, ResourceName resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TupleSet read(UserSet userset, String relation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple readOne(String userset, String relation, String resource) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple readOne(UserSet userset, String relation, ResourceName resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TupleSet add(Tuple tuple) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TupleSet add(String userset, String relation, String resource) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TupleSet add(UserSet userset, String relation, ResourceName resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TupleSet remove(Tuple tuple) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TupleSet remove(UserSet userset, String relation, ResourceName resource) {
		// TODO Auto-generated method stub
		return null;
	}

}
