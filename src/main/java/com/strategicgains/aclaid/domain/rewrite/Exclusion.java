package com.strategicgains.aclaid.domain.rewrite;

import java.text.ParseException;
import java.util.stream.Stream;

import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

public class Exclusion
implements TupleSet
{

	@Override
	public TupleSet copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TupleSet read(String relation, ResourceName resource) {
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
	public TupleSet addAll(TupleSet tupleset) {
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

	@Override
	public Stream<Tuple> stream() {
		// TODO Auto-generated method stub
		return null;
	}
}
