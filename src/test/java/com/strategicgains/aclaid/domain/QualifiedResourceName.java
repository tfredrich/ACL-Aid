package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.UUID;

public class QualifiedResourceName
extends ObjectId
{
	private static final String INVALID_ACCOUNT_ID = "Invalid Account ID: ";
	private static final String INVALID_TENANT_ID = "Invalid Tenant ID: ";

	private UUID accountId;
	private UUID tenantId;

	public QualifiedResourceName(String string)
	throws ParseException
	{
		super(string, DEFAULT_SEGMENT_COUNT + 2);
	}

	@Override
	protected void appendSegments(StringBuilder sb)
	{
		sb.append(SEPARATOR);

		if (hasTenantId())
		{
			sb.append(tenantId.toString());
		}

		sb.append(SEPARATOR);

		if (hasAccountId())
		{
			sb.append(accountId.toString());
		}
	}

	public UUID getAccountId()
	{
		return accountId;
	}

	public boolean hasAccountId()
	{
		return (accountId != null);
	}

	public UUID getTenantId()
	{
		return tenantId;
	}

	public boolean hasTenantId()
	{
		return (tenantId != null);
	}

	@Override
	protected boolean segmentsMatch(ObjectId other)
	{
		QualifiedResourceName that = (QualifiedResourceName) other;
		boolean orgMatches = (!this.hasTenantId() || !that.hasTenantId());

		if (!orgMatches)
		{
			orgMatches = this.getTenantId().equals(that.getTenantId());
		}

		if (!orgMatches) return false;

		boolean acctMatches = (!this.hasAccountId() || !that.hasAccountId());

		if (!acctMatches)
		{
			return this.getAccountId().equals(that.getAccountId());
		}

		return true;
	}

	@Override
	protected void setSegments(String... segments)
	throws ParseException
	{
		if (!segments[1].isEmpty())
		{
			try
			{
				tenantId = UUID.fromString(segments[1]);
			}
			catch(IllegalArgumentException e)
			{
				throw new ParseException(INVALID_TENANT_ID + segments[1], 2);
			}
		}

		if (!segments[2].isEmpty())
		{
			try
			{
				accountId = UUID.fromString(segments[2]);
			}
			catch(IllegalArgumentException e)
			{
				throw new ParseException(INVALID_ACCOUNT_ID + segments[2], 3);
			}
		}

		super.setSegments(segments);
	}

	public static QualifiedResourceName parse(String string)
	throws ParseException
	{
		return new QualifiedResourceName(string);
	}
}
