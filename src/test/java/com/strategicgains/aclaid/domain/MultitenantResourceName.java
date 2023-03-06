package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.UUID;

public class MultitenantResourceName
extends ResourceName
{
	private UUID accountId;
	private UUID organizationId;

	public MultitenantResourceName(String string)
	throws ParseException
	{
		super(string, DEFAULT_SEGMENT_COUNT + 2);
	}

	@Override
	protected void appendSegments(StringBuilder sb)
	{
		sb.append(SEPARATOR);

		if (hasOrganizationId())
		{
			sb.append(organizationId.toString());
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

	public UUID getOrganizationId()
	{
		return organizationId;
	}

	public boolean hasOrganizationId()
	{
		return (organizationId != null);
	}

	@Override
	protected boolean segmentsMatch(ResourceName other)
	{
		MultitenantResourceName that = (MultitenantResourceName) other;
		boolean orgMatches = (!this.hasOrganizationId() || !that.hasOrganizationId());

		if (!orgMatches)
		{
			orgMatches = this.getOrganizationId().equals(that.getOrganizationId());
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
				organizationId = UUID.fromString(segments[1]);
			}
			catch(IllegalArgumentException e)
			{
				throw new ParseException("Invalid Organization ID: " + segments[1], 2);
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
				throw new ParseException("Invalid Account ID: " + segments[2], 3);
			}
		}

		super.setSegments(segments);
	}

	public static MultitenantResourceName parse(String string)
	throws ParseException
	{
		return new MultitenantResourceName(string);
	}
}
