package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;

public interface Buildable
{
	ObjectDefinitionBuilder object(String name);

	RelationBuilder relation(String name);

	TupleBuilder tuples();

	ObjectDefinitionBuilder tuple(String userset, String relation, String resource)
	throws ParseException, InvalidTupleException;

	ObjectDefinitionBuilder tuple(UserSet userset, String relation, ObjectId resource)
	throws InvalidTupleException;

	ObjectDefinitionBuilder tuple(String tuple)
	throws ParseException, InvalidTupleException;
}