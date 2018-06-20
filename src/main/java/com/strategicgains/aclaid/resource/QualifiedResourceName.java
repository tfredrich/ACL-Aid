package com.strategicgains.aclaid.resource;

public interface QualifiedResourceName
extends Resource
{
	boolean matches(QualifiedResourceName that);
}