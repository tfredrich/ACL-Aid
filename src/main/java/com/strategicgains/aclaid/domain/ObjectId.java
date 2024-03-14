package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.Objects;

public class ObjectId
{
	public static final String SEPARATOR = ":";
	protected static final int DEFAULT_SEGMENT_COUNT = 2;

	private String namespace;
	private ObjectPath path;

	public ObjectId()
	{
		super();
	}

	public ObjectId(String objectId)
	throws ParseException
	{
		this(objectId, DEFAULT_SEGMENT_COUNT);
	}

	protected ObjectId(String objectId, int segmentCount)
	throws ParseException
	{
		this();
		String[] segments = parseSegments(objectId, segmentCount);		
		setSegments(segments);
	}

	public ObjectId(String namespace, String type)
	{
		this(namespace, new ObjectPath(type));
	}

	public ObjectId(String namespace, String resourceType, String identifier)
	{
		this(namespace, new ObjectPath(resourceType, identifier));
	}

	public ObjectId(String namespace, ObjectPath path)
	{
		this();
		setNamespace(namespace);
		setPath(path);
	}

	public ObjectId(ObjectId that)
	{
		this(that.getNamespace(), that.getPath());
	}

	public String getType()
	{
		return (hasPath() ? getPath().getType() : null);
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

	public ObjectPath getPath()
	{
		return path;
	}

	public boolean hasPath()
	{
		return path != null;
	}

	public void setPath(ObjectPath path)
	{
		this.path = path;
	}

	public boolean isTypeWildcard()
	{
		return (hasPath() && path.isTypeWildcard());
	}

	public boolean isIdentifierWildcard()
	{
		return (hasPath() && path.isIdentifierWildcard());
	}

	public boolean isWildcard()
	{
		return (isTypeWildcard() || isIdentifierWildcard());
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

		return (!Objects.equals(this.getPath(), that.getPath()));
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getNamespace(), getPath());
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder(getNamespace());
		appendSegments(sb);

		if (hasPath())
		{
			sb.append(SEPARATOR);
			sb.append(getPath().toString());
		}

		return sb.toString();
	}

	/**
	 * Determines if this ObjectId matches another Object (by String), allowing for wildcards.
	 * 
	 * @param objectId
	 * @return
	 * @throws ParseException
	 */
	public boolean matches(String objectId)
	throws ParseException
	{
		return matches(new ObjectId(objectId));
	}

	/**
	 * Determines if this ObjectId matches another ObjectId instance, allowing for wildcards.
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

		return (namespaceMatches && segmentsMatch(that) && this.getPath().matches(that.getPath()));
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

	protected String[] parseSegments(String objectId, int segmentCount)
	throws ParseException
	{
		if (objectId == null) throw new ParseException("Object IDs cannot be null", 0);

		String[] segments = objectId.split(SEPARATOR);

		if (segments.length != segmentCount)
		{
			throw new ParseException(String.format("Object IDs have %d segments, beginning with a namespace", segmentCount), objectId.lastIndexOf(':'));
		}

		return segments;
	}

	protected void setSegments(String... segments)
	throws ParseException
	{
		setNamespace(segments[0].isEmpty() ? null : segments[0]);
		setPath(ObjectPath.parse(segments[segments.length - 1]));
	}
}
