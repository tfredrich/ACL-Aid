package com.strategicgains.aclaid.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.UUID;

import org.junit.Test;

public class MultiTenantQualifiedResourceNameTest
{
	@Test
	public void shouldParseUuid()
	throws ParseException
	{
		MultiTenantQualifiedResourceName qrn = MultiTenantQualifiedResourceName.parse("qrn:test::c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
		assertQrn(qrn, "test", "c715912d-1075-4777-af98-0c1dbd1f0391", "directories/*");
	}

	@Test
	public void shouldParseStrings()
	throws ParseException
	{
		MultiTenantQualifiedResourceName qrn = MultiTenantQualifiedResourceName.parse("qrn:test::c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
		assertQrn(qrn, "test", "c715912d-1075-4777-af98-0c1dbd1f0391", "directories/*");
		qrn = MultiTenantQualifiedResourceName.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5:::directories/*");
		assertQrn(qrn, "b17544eb-f533-4c92-a4be-1a6391600ec5", null, "directories/*");
		qrn = MultiTenantQualifiedResourceName.parse("qrn:::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertQrn(qrn, null, "c715912d-1075-4777-af98-0c1dbd1f0391", "directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		qrn = MultiTenantQualifiedResourceName.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5::c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
		assertQrn(qrn, "b17544eb-f533-4c92-a4be-1a6391600ec5", "c715912d-1075-4777-af98-0c1dbd1f0391", "directories/*");
		qrn = MultiTenantQualifiedResourceName.parse("qrn:test::c715912d-1075-4777-af98-0c1dbd1f0391:*");
		assertQrn(qrn, "test", "c715912d-1075-4777-af98-0c1dbd1f0391", "*");
	}

	@Test
	public void shouldMatch()
	throws ParseException
	{
		MultiTenantQualifiedResourceName a = MultiTenantQualifiedResourceName.parse("qrn:test::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		MultiTenantQualifiedResourceName b = MultiTenantQualifiedResourceName.parse("qrn:test::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = MultiTenantQualifiedResourceName.parse("qrn:test::c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = MultiTenantQualifiedResourceName.parse("qrn:test:::directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = MultiTenantQualifiedResourceName.parse("qrn:::c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));
	}

	@Test
	public void shouldNotMatch()
	throws ParseException
	{
		MultiTenantQualifiedResourceName a = MultiTenantQualifiedResourceName.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		MultiTenantQualifiedResourceName b = MultiTenantQualifiedResourceName.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = MultiTenantQualifiedResourceName.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5:::directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = MultiTenantQualifiedResourceName.parse("qrn:::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = MultiTenantQualifiedResourceName.parse("qrn::::directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = MultiTenantQualifiedResourceName.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5::c715912d-1075-4777-af98-0c1dbd1f0391:applications/*");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = MultiTenantQualifiedResourceName.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnResourceType()
	throws ParseException
	{
		MultiTenantQualifiedResourceName.parse("qrn:test::c715912d-1075-4777-af98-0c1dbd1f0391: ");
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnTooShort()
	throws ParseException
	{
		MultiTenantQualifiedResourceName.parse("qrn:test::c715912d-1075-4777-af98-0c1dbd1f0391");
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnMissingPrefix()
	throws ParseException
	{
		MultiTenantQualifiedResourceName.parse("test::c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
	}

	@Test
	public void ShouldThrowOnBadAccount()
	{
		String qrn = "qrn:test::c715912d:directories/*";
		try
		{
			MultiTenantQualifiedResourceName.parse(qrn);
			fail("Did not throw");
		}
		catch(Throwable t)
		{
			ParseException p = (ParseException) t;
			assertEquals(8, p.getErrorOffset());
			assertEquals("Cannot parse Account ID in QRN: " + qrn, p.getMessage());
		}
	}

	private void assertQrn(MultiTenantQualifiedResourceName qrn, String namespace, String accountId, String path)
	{
		assertEquals(namespace, qrn.getNamespace());
		assertEquals((accountId == null ? null : UUID.fromString(accountId)), qrn.getAccountId());
		assertEquals(path, qrn.getResourcePath().toString());
	}
}
