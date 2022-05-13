package com.strategicgains.aclaid.builder;

import java.text.ParseException;

import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public interface Buildable
{
	NamespaceConfigurationBuilder namespace(String name);

	RelationBuilder relation(String name);

	TupleBuilder tuples();

	NamespaceConfigurationBuilder tuple(String resource, String relation, String userset)
	throws ParseException, RelationNotRegisteredException;

	NamespaceConfigurationBuilder tuple(ResourceName resource, String relation, UserSet userset)
	throws RelationNotRegisteredException;

	NamespaceConfigurationBuilder tuple(String tuple) throws ParseException;
}