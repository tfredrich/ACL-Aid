package com.strategicgains.aclaid.resource;

import java.text.ParseException;

import com.strategicgains.aclaid.domain.ResourcePath;

public interface QualifiedResourceName
{
	String getNamespace();
	boolean hasNamespace();
	void setNamespace(String namespace);

	ResourcePath getResourcePath();
	boolean hasResourcePath();
	void setResourcePath(ResourcePath resource);

	String getResourceType();
	boolean isResourceTypeWildcard();

	boolean matches(String qrnString) throws ParseException;
	boolean matches(QualifiedResourceName that);
}