package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;

public interface Buildable
{
	ResourceDefinitionBuilder object(String name);

	RelationBuilder relation(String name);

	TupleBuilder tuples();

	ResourceDefinitionBuilder tuple(String userset, String relation, String resource)
	throws ParseException, InvalidTupleException;

	ResourceDefinitionBuilder tuple(UserSet userset, String relation, ResourceName resource)
	throws InvalidTupleException;

	ResourceDefinitionBuilder tuple(String tuple)
	throws ParseException, InvalidTupleException;
}