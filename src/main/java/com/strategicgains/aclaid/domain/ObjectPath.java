package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.Objects;

public class ObjectPath
{
	private static final String SEPARATOR = "/";
	private static final String WILDCARD = "*";

	private String type;
	private String identifier;

	public ObjectPath()
	{
		super();
	}

	public ObjectPath(String resourceType)
	{
		this(resourceType, null);
	}

	public ObjectPath(String resourceType, String identifier)
	{
		this();
		setType(resourceType);
		setIdentifier(identifier);
	}

	public static ObjectPath parse(String path)
	throws ParseException
	{
		if (path == null || path.isEmpty()) throw new ParseException("Resource paths cannot be null or empty", 0);

		ObjectPath objectPath = new ObjectPath();
		String[] parts = path.trim().split(SEPARATOR);

		if (parts.length > 2) throw new ParseException("Object paths have at most two (2) segments", 0);
		if (parts.length < 1) throw new ParseException("Object paths must have at least one (1) segment", 0);

		String resourceType = parts[0].trim();
		if (resourceType.isEmpty()) throw new ParseException("Type must not be empty", 0);
		
		objectPath.setType(resourceType);

		if (parts.length == 2)
		{
			objectPath.setIdentifier(parts[1].trim());
		}
		
		return objectPath;
	}

	public ObjectPath setType(String objectType)
	{
		this.type = objectType;
		return this;
	}

	public ObjectPath setIdentifier(String value)
	{
		this.identifier = value;
		return this;
	}

	@Override
	public boolean equals(Object that)
	{
		if (this == that) return true;
		if (!(that instanceof ObjectPath)) return false;

		return equals((ObjectPath) that);
	}

	public boolean equals(ObjectPath that)
	{
		if (!(Objects.equals(this.getType(), that.getType()))) return false;

		return (!(Objects.equals(this.getIdentifier(), that.getIdentifier())));
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getType(), getIdentifier());
	}

	public boolean matches(ObjectPath that)
	{
		if (that == null) return false;

		boolean typeMatches = (this.getType().equals(that.getType())
			|| this.isTypeWildcard()
			|| that.isTypeWildcard());

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

		return (typeMatches && identifierMatches);
	}

	public String getType()
	{
		return type;
	}

	public boolean hasType()
	{
		return type != null;
	}

	public boolean isTypeWildcard()
	{
		return hasType() && getType().equals(WILDCARD);
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
		return (hasIdentifier() && getIdentifier().equals(WILDCARD)) || (!hasIdentifier() && isTypeWildcard());
	}

	public String toString()
	{
		if (hasIdentifier())
		{
			return String.format("%s/%s", (hasType() ? type : ""), identifier);
		}

		if (hasType())
		{
			return type;
		}

		return "";
	}
}
