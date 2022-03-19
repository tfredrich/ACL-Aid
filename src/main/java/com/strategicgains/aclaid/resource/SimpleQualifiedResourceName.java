package com.strategicgains.aclaid.resource;

import java.text.ParseException;
import java.util.Objects;

public class SimpleQualifiedResourceName
implements QualifiedResourceName
{
	public static final String QRN_PREFIX = "qrn:";
	public static final String SEPARATOR = ":";
	private static final int SEGMENT_COUNT = 3;

	private String namespace;
	private ResourcePath resourcePath;

	public SimpleQualifiedResourceName()
	{
		super();
	}

	public SimpleQualifiedResourceName(String namespace, String resourceType)
	{
		this(namespace, new ResourcePath(resourceType));
	}

	public SimpleQualifiedResourceName(String namespace, String resourceType, String identifier)
	{
		this(namespace, new ResourcePath(resourceType, identifier));
	}

	public SimpleQualifiedResourceName(String namespace, ResourcePath resourcePath)
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
		if (!(that instanceof SimpleQualifiedResourceName)) return false;

		return equals((SimpleQualifiedResourceName) that);
	}

	public boolean equals(SimpleQualifiedResourceName that)
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
		StringBuilder sb = new StringBuilder(QRN_PREFIX);

		sb.append(getNamespace());
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

	public static SimpleQualifiedResourceName parse(String qrnString)
	throws ParseException
	{
		String[] segments = SimpleQualifiedResourceName.toSegments(qrnString, SEGMENT_COUNT);

		SimpleQualifiedResourceName qrn = new SimpleQualifiedResourceName();
		qrn.setNamespace(segments[1].isEmpty() ? null : segments[1]);
		qrn.setResourcePath(ResourcePath.parse(segments[2]));
		return qrn;
	}

	protected static String[] toSegments(String qrnString, int segmentCount)
	throws ParseException
	{
		if (qrnString == null) throw new ParseException("QRN strings cannot be null", 0);

		String[] segments = qrnString.split(SEPARATOR);

		if (segments.length != segmentCount)
		{
			throw new ParseException(String.format("QRNs have five (%d) segments, beginning with 'qrn:'", segmentCount), qrnString.lastIndexOf(':'));
		}
		else if (!"qrn".equalsIgnoreCase(segments[0]))
		{
			throw new ParseException("QRNs must begin with 'qrn:'", 0);
		}

		return segments;
	}
}
