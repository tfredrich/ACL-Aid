package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.Objects;

public class ResourceName
{
	public static final String SEPARATOR = ":";
	protected static final int DEFAULT_SEGMENT_COUNT = 2;

	private String namespace;
	private ResourcePath resourcePath;

	public ResourceName()
	{
		super();
	}

	public ResourceName(String namespace, String resourceType)
	{
		this(namespace, new ResourcePath(resourceType));
	}

	public ResourceName(String namespace, String resourceType, String identifier)
	{
		this(namespace, new ResourcePath(resourceType, identifier));
	}

	public ResourceName(String namespace, ResourcePath resourcePath)
	{
		this();
		setNamespace(namespace);
		setResourcePath(resourcePath);
	}

	public String getResourceType()
	{
		return (hasResourcePath() ? getResourcePath().getResourceType() : null);
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

	public ResourcePath getResourcePath()
	{
		return resourcePath;
	}

	public boolean hasResourcePath()
	{
		return resourcePath != null;
	}

	public void setResourcePath(ResourcePath resource)
	{
		this.resourcePath = resource;
	}

	public boolean isResourceTypeWildcard()
	{
		return (hasResourcePath() && resourcePath.isResourceTypeWildcard());
	}

	@Override
	//TODO: What about additional segments?
	public boolean equals(Object that)
	{
		if (this == that) return true;
		if (!(that instanceof ResourceName)) return false;

		return equals((ResourceName) that);
	}

	//TODO: What about additional segments?
	public boolean equals(ResourceName that)
	{
		if (!this.getNamespace().equals(that.getNamespace())) return false;

		return (!Objects.equals(this.getResourcePath(), that.getResourcePath()));
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getNamespace(), getResourcePath());
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder(getNamespace());
		appendSegments(sb);

		if (hasResourcePath())
		{
			sb.append(SEPARATOR);
			sb.append(getResourcePath().toString());
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
		return matches(parse(resourceName));
	}

	/**
	 * Determines if this ResourceName matches another ResourceName, allowing for wildcards.
	 * 
	 * @param that
	 * @return
	 */
	public boolean matches(ResourceName that)
	{
		if (that == null) return false;

		boolean namespaceMatches = (!this.hasNamespace() || !that.hasNamespace());

		if (!namespaceMatches)
		{
			namespaceMatches = this.getNamespace().equals(that.getNamespace());
		}

		return (namespaceMatches && segmentsMatch(that) && this.getResourcePath().matches(that.getResourcePath()));
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
	 * 
	 * @param that
	 * @return
	 */
	protected boolean segmentsMatch(ResourceName that)
	{
		// No additional segments to match.
		return true;
	}

	public static ResourceName parse(String resourceName)
	throws ParseException
	{
		if (resourceName == null) throw new ParseException("Resource names cannot be null", 0);

		String[] segments = resourceName.split(SEPARATOR);

		if (segments.length != DEFAULT_SEGMENT_COUNT)
		{
			throw new ParseException(String.format("Resource names have %d segments, beginning with a namespace", DEFAULT_SEGMENT_COUNT), resourceName.lastIndexOf(':'));
		}

		ResourceName rn = new ResourceName();
		fromSegments(rn, segments);
		return rn;
	}

	protected static void fromSegments(ResourceName rn, String... segments)
	throws ParseException
	{
		rn.setNamespace(segments[0].isEmpty() ? null : segments[0]);
		rn.setResourcePath(ResourcePath.parse(segments[1]));
	}
}
