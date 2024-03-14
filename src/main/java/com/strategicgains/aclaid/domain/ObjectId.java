package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.Objects;

public class ObjectId
{
	public static final String SEPARATOR = ":";
	protected static final int DEFAULT_SEGMENT_COUNT = 2;

	private String namespace;
	private ObjectPath objectPath;

	public ObjectId()
	{
		super();
	}

	public ObjectId(String resourceName)
	throws ParseException
	{
		this(resourceName, DEFAULT_SEGMENT_COUNT);
	}

	protected ObjectId(String resourceName, int segmentCount)
	throws ParseException
	{
		this();
		String[] segments = parseSegments(resourceName, segmentCount);		
		setSegments(segments);
	}

	public ObjectId(String namespace, String resourceType)
	{
		this(namespace, new ObjectPath(resourceType));
	}

	public ObjectId(String namespace, String resourceType, String identifier)
	{
		this(namespace, new ObjectPath(resourceType, identifier));
	}

	public ObjectId(String namespace, ObjectPath resourcePath)
	{
		this();
		setNamespace(namespace);
		setObjectPath(resourcePath);
	}

	public ObjectId(ObjectId that)
	{
		this(that.getNamespace(), that.getObjectPath());
	}

	public String getResourceType()
	{
		return (hasResourcePath() ? getObjectPath().getType() : null);
	}

	public String getNamespace()
	{
		return namespace;
	}

	public boolean hasNamespace()
	{
		return namespace != null;
	}

	public void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}

	public ObjectPath getObjectPath()
	{
		return objectPath;
	}

	public boolean hasResourcePath()
	{
		return objectPath != null;
	}

	public void setObjectPath(ObjectPath resource)
	{
		this.objectPath = resource;
	}

	public boolean isObjectTypeWildcard()
	{
		return (hasResourcePath() && objectPath.isTypeWildcard());
	}

	public boolean isIdentifierWildcard()
	{
		return (hasResourcePath() && objectPath.isIdentifierWildcard());
	}

	public boolean isWildcard()
	{
		return (isObjectTypeWildcard() || isIdentifierWildcard());
	}

	@Override
	//TODO: What about additional segments?
	public boolean equals(Object that)
	{
		if (this == that) return true;
		if (!(that instanceof ObjectId)) return false;

		return equals((ObjectId) that);
	}

	//TODO: What about additional segments?
	public boolean equals(ObjectId that)
	{
		if (!this.getNamespace().equals(that.getNamespace())) return false;

		return (!Objects.equals(this.getObjectPath(), that.getObjectPath()));
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getNamespace(), getObjectPath());
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder(getNamespace());
		appendSegments(sb);

		if (hasResourcePath())
		{
			sb.append(SEPARATOR);
			sb.append(getObjectPath().toString());
		}

		return sb.toString();
	}

	/**
	 * Determines if this ResourceName matches another ResourceName (by String), allowing for wildcards.
	 * 
	 * @param resourceName
	 * @return
	 * @throws ParseException
	 */
	public boolean matches(String resourceName)
	throws ParseException
	{
		return matches(new ObjectId(resourceName));
	}

	/**
	 * Determines if this ResourceName matches another ResourceName, allowing for wildcards.
	 * 
	 * @param that
	 * @return
	 */
	public boolean matches(ObjectId that)
	{
		if (that == null) return false;

		boolean namespaceMatches = (!this.hasNamespace() || !that.hasNamespace());

		if (!namespaceMatches)
		{
			namespaceMatches = this.getNamespace().equals(that.getNamespace());
		}

		return (namespaceMatches && segmentsMatch(that) && this.getObjectPath().matches(that.getObjectPath()));
	}

	/**
	 * Subclasses must implement to add segments to the string.
	 * 
	 * @param sb
	 */
	protected void appendSegments(StringBuilder sb)
	{
		// Do nothing. No additional segments.
	}

	/**
	 * Subclasses must implement to compare additional segments.
	 * @param <T>
	 * 
	 * @param that
	 * @return
	 */
	protected boolean segmentsMatch(ObjectId that)
	{
		// No additional segments to match.
		return true;
	}

	protected String[] parseSegments(String resourceName, int segmentCount)
	throws ParseException
	{
		if (resourceName == null) throw new ParseException("Resource names cannot be null", 0);

		String[] segments = resourceName.split(SEPARATOR);

		if (segments.length != segmentCount)
		{
			throw new ParseException(String.format("Resource names have %d segments, beginning with a namespace", segmentCount), resourceName.lastIndexOf(':'));
		}

		return segments;
	}

	protected void setSegments(String... segments)
	throws ParseException
	{
		setNamespace(segments[0].isEmpty() ? null : segments[0]);
		setObjectPath(ObjectPath.parse(segments[segments.length - 1]));
	}

	public void get(String descriptor)
	{
		// TODO Auto-generated method stub
		
	}
}
