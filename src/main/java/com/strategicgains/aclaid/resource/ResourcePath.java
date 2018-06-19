package com.strategicgains.aclaid.resource;

import java.text.ParseException;

public class ResourcePath
{
	private static final String SEPARATOR = "/";
	private static final String WILDCARD = "*";

	private String resourceType;
	private String identifier;

	public ResourcePath()
	{
		super();
	}

	public static ResourcePath parse(String path)
	throws ParseException {
		if (path == null || path.isEmpty()) throw new ParseException("Resource paths cannot be null or empty", 0);

		ResourcePath resourcePath = new ResourcePath();
		String[] parts = path.trim().split(SEPARATOR);

		if (parts.length > 2) throw new ParseException("Resource paths have at most two (2) segments", 0);
		if (parts.length < 1) throw new ParseException("Resource paths must have at least one (1) segment", 0);

		String resourceType = parts[0].trim();
		if (resourceType.isEmpty()) throw new ParseException("Resource type must not be empty", 0);
		
		resourcePath.setResourceType(resourceType);

		if (parts.length == 2) {
			resourcePath.setIdentifier(parts[1]);
		}
		
		return resourcePath;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public void setIdentifier(String value) {
		this.identifier = value;
	}

	public boolean matches(ResourcePath that) {
		if (that == null) return false;

		boolean resourceTypeMatches = (this.getResourceType().equals(that.getResourceType())
			|| this.isResourceTypeWildcard()
			|| that.isResourceTypeWildcard());

		boolean identifierMatches = (this.isIdentifierWildcard() || that.isIdentifierWildcard());

		if (!identifierMatches) {
			if (this.hasIdentifier()) {
				identifierMatches = this.getIdentifier().equals(that.getIdentifier());
			}
			else if (that.hasIdentifier()) {
				identifierMatches = that.getIdentifier().equals(this.getIdentifier());
			}
			else {	// Ignore identifier
				identifierMatches = true;
			}
		}

		return (resourceTypeMatches && identifierMatches);
	}

	public String getResourceType() {
		return resourceType;
	}

	public boolean hasResourceType() {
		return resourceType != null;
	}

	public boolean isResourceTypeWildcard() {
		return hasResourceType() && getResourceType().equals(WILDCARD);
	}

	public String getIdentifier() {
		return identifier;
	}

	public boolean hasIdentifier() {
		return identifier != null;
	}

	public boolean isIdentifierWildcard()
	{
		return (hasIdentifier() && getIdentifier().equals(WILDCARD)) || (!hasIdentifier() && isResourceTypeWildcard());
	}

	public String toString()
	{
		if (hasIdentifier())
		{
			return String.format("%s/%s", (hasResourceType() ? resourceType : ""), identifier);
		}

		if (hasResourceType())
		{
			return resourceType;
		}

		return "";
	}
}
