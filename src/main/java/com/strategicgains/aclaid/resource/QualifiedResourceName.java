package com.strategicgains.aclaid.resource;

import java.text.ParseException;

public interface QualifiedResourceName
{

	String getNamespace();
	boolean hasNamespace();
	void setNamespace(String namespace);

	ResourcePath getResourcePath();
	String getResourceType();
	boolean hasResourcePath();
	boolean isResourceTypeWildcard();
	void setResourcePath(ResourcePath resource);

	boolean matches(String qrnString) throws ParseException;
	boolean matches(QualifiedResourceName that);
}