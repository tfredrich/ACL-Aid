package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public interface Buildable
{
	ResourceDefinitionBuilder object(String name);

	RelationBuilder relation(String name);

	TupleBuilder tuples();

	ResourceDefinitionBuilder tuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException, InvalidTupleException;

	ResourceDefinitionBuilder tuple(UserSet userset, String relation, ResourceName resource)
	throws RelationNotRegisteredException, InvalidTupleException;

	ResourceDefinitionBuilder tuple(String tuple)
	throws ParseException, InvalidTupleException;
}