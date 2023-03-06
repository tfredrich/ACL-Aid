package com.strategicgains.aclaid.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.UUID;

import org.junit.Test;

public class MultitenantResourceNameTest
{
	@Test
	public void shouldParseUuid()
	throws ParseException
	{
		MultitenantResourceName qrn = MultitenantResourceName.parse("test::c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
		assertQrn(qrn, "test", "c715912d-1075-4777-af98-0c1dbd1f0391", "directories/*");
		assertEquals("test::c715912d-1075-4777-af98-0c1dbd1f0391:directories/*", qrn.toString());
	}

	@Test
	public void shouldParseStrings()
	throws ParseException
	{
		MultitenantResourceName qrn = MultitenantResourceName.parse("test::c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
		assertQrn(qrn, "test", "c715912d-1075-4777-af98-0c1dbd1f0391", "directories/*");
		qrn = MultitenantResourceName.parse("b17544eb-f533-4c92-a4be-1a6391600ec5:::directories/*");
		assertQrn(qrn, "b17544eb-f533-4c92-a4be-1a6391600ec5", null, "directories/*");
		qrn = MultitenantResourceName.parse("::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertQrn(qrn, null, "c715912d-1075-4777-af98-0c1dbd1f0391", "directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		qrn = MultitenantResourceName.parse("b17544eb-f533-4c92-a4be-1a6391600ec5::c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
		assertQrn(qrn, "b17544eb-f533-4c92-a4be-1a6391600ec5", "c715912d-1075-4777-af98-0c1dbd1f0391", "directories/*");
		qrn = MultitenantResourceName.parse("test::c715912d-1075-4777-af98-0c1dbd1f0391:*");
		assertQrn(qrn, "test", "c715912d-1075-4777-af98-0c1dbd1f0391", "*");
	}

	@Test
	public void shouldMatch()
	throws ParseException
	{
		MultitenantResourceName a = MultitenantResourceName.parse("test::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		MultitenantResourceName b = MultitenantResourceName.parse("test::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = MultitenantResourceName.parse("test::c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = MultitenantResourceName.parse("test:::directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = MultitenantResourceName.parse("::c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));
	}

	@Test
	public void shouldNotMatch()
	throws ParseException
	{
		MultitenantResourceName a = MultitenantResourceName.parse("b17544eb-f533-4c92-a4be-1a6391600ec5::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		MultitenantResourceName b = MultitenantResourceName.parse("b17544eb-f533-4c92-a4be-1a6391600ec5::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = MultitenantResourceName.parse("b17544eb-f533-4c92-a4be-1a6391600ec5:::directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = MultitenantResourceName.parse("::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = MultitenantResourceName.parse(":::directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = MultitenantResourceName.parse("b17544eb-f533-4c92-a4be-1a6391600ec5::c715912d-1075-4777-af98-0c1dbd1f0391:applications/*");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = MultitenantResourceName.parse("b17544eb-f533-4c92-a4be-1a6391600ec5::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnResourceType()
	throws ParseException
	{
		MultitenantResourceName.parse("test::c715912d-1075-4777-af98-0c1dbd1f0391: ");
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnTooShort()
	throws ParseException
	{
		MultitenantResourceName.parse("test::c715912d-1075-4777-af98-0c1dbd1f0391");
	}

	@Test
	public void ShouldThrowOnBadAccount()
	{
		String qrn = "test::c715912d:directories/*";
		try
		{
			MultitenantResourceName.parse(qrn);
			fail("Did not throw");
		}
		catch(Throwable t)
		{
			ParseException p = (ParseException) t;
			assertEquals(3, p.getErrorOffset());
			assertEquals("Invalid Account ID: c715912d", p.getMessage());
		}
	}

	private void assertQrn(MultitenantResourceName qrn, String namespace, String accountId, String path)
	{
		assertEquals(namespace, qrn.getNamespace());
		assertEquals((accountId == null ? null : UUID.fromString(accountId)), qrn.getAccountId());
		assertEquals(path, qrn.getResourcePath().toString());
	}
}
