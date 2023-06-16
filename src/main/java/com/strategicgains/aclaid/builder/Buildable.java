package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public interface Buildable
{
	ObjectDefinitionBuilder object(String name);

	RelationBuilder relation(String name);

	TupleBuilder tuples();

	ObjectDefinitionBuilder tuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException, InvalidTupleException;

	ObjectDefinitionBuilder tuple(UserSet userset, String relation, ResourceName resource)
	throws RelationNotRegisteredException, InvalidTupleException;

	ObjectDefinitionBuilder tuple(String tuple)
	throws ParseException, InvalidTupleException;
}