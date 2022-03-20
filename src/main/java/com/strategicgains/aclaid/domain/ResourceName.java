package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.Objects;

import com.strategicgains.aclaid.resource.QualifiedResourceName;

public class ResourceName
implements QualifiedResourceName
{
	public static final String SEPARATOR = ":";
	private static final int SEGMENT_COUNT = 2;

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

	@Override
	public String getResourceType()
	{
		return (hasResourcePath() ? getResourcePath().getResourceType() : null);
	}

	@Override
	public String getNamespace()
	{
		return namespace;
	}

	@Override
	public boolean hasNamespace()
	{
		return namespace != null;
	}

	@Override
	public void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}

	@Override
	public ResourcePath getResourcePath()
	{
		return resourcePath;
	}

	@Override
	public boolean hasResourcePath()
	{
		return resourcePath != null;
	}

	@Override
	public void setResourcePath(ResourcePath resource)
	{
		this.resourcePath = resource;
	}

	@Override
	public boolean isResourceTypeWildcard()
	{
		return (hasResourcePath() && resourcePath.isResourceTypeWildcard());
	}

	@Override
	public boolean equals(Object that)
	{
		if (this == that) return true;
		if (!(that instanceof ResourceName)) return false;

		return equals((ResourceName) that);
	}

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

	@Override
	public boolean matches(String qrnString)
	throws ParseException
	{
		return matches(parse(qrnString));
	}

	@Override
	public boolean matches(QualifiedResourceName that)
	{
		if (that == null) return false;

		boolean namespaceMatches = (!this.hasNamespace() || !that.hasNamespace());

		if (!namespaceMatches)
		{
			namespaceMatches = this.getNamespace().equals(that.getNamespace());
		}

		return (namespaceMatches && segmentsMatch(that) && this.getResourcePath().matches(that.getResourcePath()));
	}

	protected void appendSegments(StringBuilder sb)
	{
		// Do nothing. No additional segments.
	}

	protected boolean segmentsMatch(QualifiedResourceName that)
	{
		// No additional segments to match.
		return true;
	}

	public static ResourceName parse(String qrnString)
	throws ParseException
	{
		String[] segments = ResourceName.toSegments(qrnString, SEGMENT_COUNT);

		ResourceName rn = new ResourceName();
		rn.setNamespace(segments[0].isEmpty() ? null : segments[0]);
		rn.setResourcePath(ResourcePath.parse(segments[1]));
		return rn;
	}

	protected static String[] toSegments(String qrnString, int segmentCount)
	throws ParseException
	{
		if (qrnString == null) throw new ParseException("Resource names cannot be null", 0);

		String[] segments = qrnString.split(SEPARATOR);

		if (segments.length != segmentCount)
		{
			throw new ParseException(String.format("Resource names have %d segments, beginning with a namespace", segmentCount), qrnString.lastIndexOf(':'));
		}

		return segments;
	}
}
