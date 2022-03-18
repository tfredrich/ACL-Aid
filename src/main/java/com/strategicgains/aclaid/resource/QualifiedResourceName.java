package com.strategicgains.aclaid.resource;

import java.text.ParseException;
import java.util.Objects;

public class QualifiedResourceName
{
	public static final String QRN_PREFIX = "qrn:";
	public static final String SEPARATOR = ":";
	private static final int SEGMENT_COUNT = 3;

	private String namespace;
	private ResourcePath resourcePath;

	protected QualifiedResourceName()
	{
		super();
	}

	protected QualifiedResourceName(String namespace, String resourceType)
	{
		this(namespace, new ResourcePath(resourceType));
	}

	protected QualifiedResourceName(String namespace, String resourceType, String identifier)
	{
		this(namespace, new ResourcePath(resourceType, identifier));
	}

	protected QualifiedResourceName(String namespace, ResourcePath resourcePath)
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

	public QualifiedResourceName setNamespace(String namespace)
	{
		this.namespace = namespace;
		return this;
	}

	public ResourcePath getResourcePath()
	{
		return resourcePath;
	}

	public boolean hasResourcePath()
	{
		return resourcePath != null;
	}

	public QualifiedResourceName setResourcePath(ResourcePath resource)
	{
		this.resourcePath = resource;
		return this;
	}

	public boolean isResourceTypeWildcard()
	{
		return (hasResourcePath() && resourcePath.isResourceTypeWildcard());
	}

	@Override
	public boolean equals(Object that)
	{
		if (this == that) return true;
		if (!(that instanceof QualifiedResourceName)) return false;

		return equals((QualifiedResourceName) that);
	}

	public boolean equals(QualifiedResourceName that)
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

	protected void appendSegments(StringBuilder sb)
	{
		// Do nothing. No additional segments.
	}

	public boolean matches(String qrnString)
	throws ParseException
	{
		return matches(parse(qrnString));
	}

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

	protected boolean segmentsMatch(QualifiedResourceName that)
	{
		// No additional segments to match.
		return true;
	}

	public static QualifiedResourceName parse(String qrnString)
	throws ParseException
	{
		String[] segments = QualifiedResourceName.parseSegments(qrnString, SEGMENT_COUNT);

		QualifiedResourceName qrn = new QualifiedResourceName();
		qrn.setNamespace(segments[1].isEmpty() ? null : segments[1]);
		qrn.setResourcePath(ResourcePath.parse(segments[2]));
		return qrn;
	}

	protected static String[] parseSegments(String qrnString, int segmentCount)
	throws ParseException
	{
		if (qrnString == null) throw new ParseException("QRN strings cannot be null", 0);

		String[] segments = qrnString.split(SEPARATOR);

		if (!"qrn".equalsIgnoreCase(segments[0]))
		{
			throw new ParseException("QRNs must begin with 'qrn:'", 0);
		}
		else if (segments.length != segmentCount)
		{
			throw new ParseException(String.format("QRNs have five (%d) segments, beginning with 'qrn:'", segmentCount), qrnString.lastIndexOf(':'));
		}

		return segments;
	}
}
