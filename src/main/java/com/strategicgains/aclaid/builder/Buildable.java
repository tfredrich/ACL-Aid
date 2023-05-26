package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidTupleException;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public interface Buildable
{
	NamespaceBuilder namespace(String name);

	RelationBuilder relation(String name);

	TupleBuilder tuples();

	NamespaceBuilder tuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException, InvalidTupleException;

	NamespaceBuilder tuple(UserSet userset, String relation, ResourceName resource)
	throws RelationNotRegisteredException, InvalidTupleException;

	NamespaceBuilder tuple(String tuple)
	throws ParseException, InvalidTupleException;
}