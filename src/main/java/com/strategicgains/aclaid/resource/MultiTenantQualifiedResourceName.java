package com.strategicgains.aclaid.resource;

import java.text.ParseException;
import java.util.Objects;
import java.util.UUID;

public class MultiTenantQualifiedResourceName
extends QualifiedResourceName
{
	public static final int SEGMENT_COUNT = 5;

	private UUID organizationId;
	private UUID accountId;

	protected MultiTenantQualifiedResourceName()
	{
		super();
	}

	protected MultiTenantQualifiedResourceName(String namespace, UUID accountId, String resourceType)
	{
		this(namespace, accountId, new ResourcePath(resourceType));
	}

	protected MultiTenantQualifiedResourceName(String namespace, UUID accountId, String resourceType, String identifier)
	{
		this(namespace, null, accountId, resourceType, identifier);
	}

	protected MultiTenantQualifiedResourceName(String namespace, UUID accountId, ResourcePath resourcePath)
	{
		this(namespace, null, accountId, resourcePath);
	}

	protected MultiTenantQualifiedResourceName(String namespace, UUID organizationId, UUID accountId, String resourceType, String identifier)
	{
		this(namespace, organizationId, accountId, new ResourcePath(resourceType, identifier));
	}

	protected MultiTenantQualifiedResourceName(String namespace, UUID organizationId, UUID accountId, ResourcePath resourcePath)
	{
		super(namespace, resourcePath);
		setOrganizationId(organizationId);
		setAccountId(accountId);
	}

	public UUID getOrganizationId()
	{
		return organizationId;
	}

	public boolean hasOrganizationId()
	{
		return organizationId != null;
	}

	public MultiTenantQualifiedResourceName setOrganizationId(UUID organizationId)
	{
		this.organizationId = organizationId;
		return this;
	}

	public UUID getAccountId()
	{
		return accountId;
	}

	public boolean hasAccountId()
	{
		return accountId != null;
	}

	public MultiTenantQualifiedResourceName setAccountId(UUID accountId)
	{
		this.accountId = accountId;
		return this;
	}

	@Override
	public boolean equals(Object that)
	{
		if (this == that) return true;
		if (!(that instanceof MultiTenantQualifiedResourceName)) return false;

		return equals((MultiTenantQualifiedResourceName) that);
	}

	public boolean equals(MultiTenantQualifiedResourceName that)
	{
		if (!super.equals(that)) return false;
		
		if (!Objects.equals(this.getOrganizationId(), that.getOrganizationId())) return false;

		return (!Objects.equals(this.getAccountId(), that.getAccountId()));
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getNamespace(), getOrganizationId(), getAccountId(), getResourcePath());
	}

	@Override
	protected void appendSegments(StringBuilder sb)
	{
		sb.append(SEPARATOR);
		if (hasOrganizationId()) sb.append(formatUuid(getOrganizationId()));

		sb.append(SEPARATOR);
		if (hasAccountId()) sb.append(formatUuid(getAccountId()));
	}

	@Override
	protected boolean segmentsMatch(QualifiedResourceName qrn)
	{
		MultiTenantQualifiedResourceName that = (MultiTenantQualifiedResourceName) qrn; 
		boolean orgMatches = (!this.hasOrganizationId() || !that.hasOrganizationId());

		if (!orgMatches)
		{
			orgMatches = this.getOrganizationId().equals(that.getOrganizationId());
		}

		boolean accountMatches = (!this.hasAccountId() || !that.hasAccountId());

		if (!accountMatches)
		{
			accountMatches = this.getAccountId().equals(that.getAccountId());
		}

		return (orgMatches && accountMatches);
	}

	protected static UUID parseUuid(String uuidString)
	{
		if (uuidString == null || uuidString.isEmpty()) return null;
		return UUID.fromString(uuidString);			
	}

	protected String formatUuid(UUID uuid)
	{
		if (uuid == null) return "";
		return uuid.toString();
	}

	public static MultiTenantQualifiedResourceName parse(String qrnString)
	throws ParseException
	{
		String[] segments = QualifiedResourceName.parseSegments(qrnString, SEGMENT_COUNT);

		MultiTenantQualifiedResourceName qrn = new MultiTenantQualifiedResourceName();
		qrn.setNamespace(segments[1].isEmpty() ? null : segments[1]);
		setOrganization(qrn, segments[2], qrnString);
		setAccount(qrn, segments[3], qrnString);
		qrn.setResourcePath(ResourcePath.parse(segments[4]));

		return qrn;
	}

	private static void setOrganization(MultiTenantQualifiedResourceName qrn, String orgId, String qrnString)
	throws ParseException
	{
		try
		{
			qrn.setOrganizationId(orgId.isEmpty() ? null : parseUuid(orgId));
		}
		catch (IllegalArgumentException e)
		{
			int offset = QRN_PREFIX.length() + (qrn.hasNamespace() ? qrn.getNamespace().length() : 0) + 1;
			throw new ParseException("Cannot parse Organization ID in QRN: " + qrnString, qrnString.indexOf(":", offset));
		}
	}

	private static void setAccount(MultiTenantQualifiedResourceName qrn, String accountId, String qrnString)
	throws ParseException
	{
		try
		{
			qrn.setAccountId(parseUuid(accountId));
		}
		catch (IllegalArgumentException e)
		{
			int offset = QRN_PREFIX.length() + (qrn.hasNamespace() ? qrn.getNamespace().length() : 0) + (qrn.hasOrganizationId() ? qrn.getOrganizationId().toString().length() : 0) + 1;
			throw new ParseException("Cannot parse Account ID in QRN: " + qrnString, qrnString.indexOf(":", offset));
		}
	}
}
