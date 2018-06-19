package com.strategicgains.aclaid.resource;

import java.text.ParseException;

public interface QualifiedResourceName<T>
{
	String getResourceId();
	boolean matches(String qrnString) throws ParseException;
	boolean matches(T that);
}