package com.strategicgains.aclaid.resource;

import java.text.ParseException;
import java.util.UUID;

public class QualifiedResourceNameImpl
implements QualifiedResourceName<QualifiedResourceNameImpl>
{
	private static final String QRN_PREFIX = "qrn:";
	private static final String SEPARATOR = ":";

	private String namespace;
	private UUID accountId;
	private ResourcePath resourcePath;

	public QualifiedResourceNameImpl()
	{
		super();
	}

	public QualifiedResourceNameImpl(String namespace, UUID accountId, ResourcePath resourcePath)
	{
		this();
		setNamespace(namespace);
		setAccountId(accountId);
		setResourcePath(resourcePath);
	}

	@Override
	public String getResourceId()
	{
		return toString();
	}

	public UUID getAccountId()
	{
		return accountId;
	}

	public boolean hasAccountId()
	{
		return accountId != null;
	}

	public void setAccountId(UUID accountId)
	{
		this.accountId = accountId;
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

	public boolean matches(String qrnString)
	throws ParseException
	{
		return matches(parse(qrnString));
	}

	public boolean matches(QualifiedResourceNameImpl that)
	{
		if (that == null) return false;

		boolean namespaceMatches = (!this.hasNamespace() || !that.hasNamespace());

		if (!namespaceMatches)
		{
			namespaceMatches = this.getNamespace().equals(that.getNamespace());
		}

		boolean accountMatches = (!this.hasAccountId() || !that.hasAccountId());

		if (!accountMatches)
		{
			accountMatches = this.getAccountId().equals(that.getAccountId());
		}

		return (namespaceMatches && accountMatches && this.getResourcePath().matches(that.getResourcePath()));
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder(QRN_PREFIX);

		sb.append(getNamespace());
		sb.append(SEPARATOR);
		if (hasAccountId()) sb.append(formatUuid(getAccountId()));
		sb.append(SEPARATOR);
		if (hasResourcePath()) sb.append(getResourcePath());
		return sb.toString();
	}

	public static QualifiedResourceNameImpl parse(String qrnString)
	throws ParseException
	{
		if (qrnString == null) throw new ParseException("QRN strings cannot be null", 0);

		String[] parts = qrnString.split(SEPARATOR);

		if (parts.length != 4)
		    throw new ParseException("QRNs have four (4) segments", qrnString.lastIndexOf(':'));
		if (!"qrn".equalsIgnoreCase(parts[0])) throw new ParseException("QRNs must begin with 'qrn:'", 0);

		QualifiedResourceNameImpl qrn = new QualifiedResourceNameImpl();

		for (int i = 1; i < parts.length; ++i)
		{
			String part = parts[i];

			switch (i)
			{
				case 1:
					qrn.setNamespace(part.isEmpty() ? null : part);
					break;
				case 2:
					try
					{
						qrn.setAccountId(parseUuid(part));
					}
					catch (IllegalArgumentException e)
					{
						int offset = parts[0].length() + parts[1].length() + 1;
						throw new ParseException("Cannot parse Account ID in QRN", qrnString.indexOf(":", offset));
					}
					break;
				case 3:
					qrn.setResourcePath(ResourcePath.parse(part));
					break;
			}
		}

		return qrn;
	}

	protected static UUID parseUuid(String uuidString)
	{
		if (uuidString == null || uuidString.isEmpty()) return null;
		return UUID.fromString(uuidString);
	}

	protected static String formatUuid(UUID uuid)
	{
		if (uuid == null) return "";
		return uuid.toString();
	}
}
