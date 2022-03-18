package com.strategicgains.aclaid.resource;

import java.text.ParseException;
import java.util.Objects;

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

	public ResourcePath(String resourceType)
	{
		this(resourceType, null);
	}

	public ResourcePath(String resourceType, String identifier)
	{
		this();
		setResourceType(resourceType);
		setIdentifier(identifier);
	}

	public static ResourcePath parse(String path)
	throws ParseException
	{
		if (path == null || path.isEmpty()) throw new ParseException("Resource paths cannot be null or empty", 0);

		ResourcePath resourcePath = new ResourcePath();
		String[] parts = path.trim().split(SEPARATOR);

		if (parts.length > 2) throw new ParseException("Resource paths have at most two (2) segments", 0);
		if (parts.length < 1) throw new ParseException("Resource paths must have at least one (1) segment", 0);

		String resourceType = parts[0].trim();
		if (resourceType.isEmpty()) throw new ParseException("Resource type must not be empty", 0);
		
		resourcePath.setResourceType(resourceType);

		if (parts.length == 2)
		{
			resourcePath.setIdentifier(parts[1].trim());
		}
		
		return resourcePath;
	}

	public ResourcePath setResourceType(String resourceType)
	{
		this.resourceType = resourceType;
		return this;
	}

	public ResourcePath setIdentifier(String value)
	{
		this.identifier = value;
		return this;
	}

	@Override
	public boolean equals(Object that)
	{
		if (this == that) return true;
		if (!(that instanceof ResourcePath)) return false;

		return equals((ResourcePath) that);
	}

	public boolean equals(ResourcePath that)
	{
		if (!(Objects.equals(this.getResourceType(), that.getResourceType()))) return false;

		return (!(Objects.equals(this.getIdentifier(), that.getIdentifier())));
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getResourceType(), getIdentifier());
	}

	public boolean matches(ResourcePath that)
	{
		if (that == null) return false;

		boolean resourceTypeMatches = (this.getResourceType().equals(that.getResourceType())
			|| this.isResourceTypeWildcard()
			|| that.isResourceTypeWildcard());

		boolean identifierMatches = (this.isIdentifierWildcard() || that.isIdentifierWildcard());

		if (!identifierMatches)
		{
			if (this.hasIdentifier() && that.hasIdentifier())
			{
				identifierMatches = this.getIdentifier().equals(that.getIdentifier());
			}
			else if (this.hasIdentifier() || that.hasIdentifier())
			{
				identifierMatches = false;
			}
			else
			{	// Ignore identifier
				identifierMatches = true;
			}
		}

		return (resourceTypeMatches && identifierMatches);
	}

	public String getResourceType()
	{
		return resourceType;
	}

	public boolean hasResourceType()
	{
		return resourceType != null;
	}

	public boolean isResourceTypeWildcard()
	{
		return hasResourceType() && getResourceType().equals(WILDCARD);
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public boolean hasIdentifier()
	{
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
