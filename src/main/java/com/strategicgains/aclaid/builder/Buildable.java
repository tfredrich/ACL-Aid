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

	NamespaceConfigurationBuilder tuple(String userset, String relation, String resource)
	throws ParseException, RelationNotRegisteredException;

	NamespaceConfigurationBuilder tuple(UserSet userset, String relation, ResourceName resource)
	throws RelationNotRegisteredException;

	NamespaceConfigurationBuilder tuple(String tuple)
	throws ParseException;
}