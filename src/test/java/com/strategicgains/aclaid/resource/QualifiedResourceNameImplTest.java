package com.strategicgains.aclaid.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.UUID;

import org.junit.Test;

public class QualifiedResourceNameImplTest
{
	@Test
	public void shouldParseStrings()
	throws ParseException
	{
		QualifiedResourceNameImpl qrn = QualifiedResourceNameImpl.parse("qrn:test:c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
		assertQrn(qrn, "test", "c715912d-1075-4777-af98-0c1dbd1f0391", "directories/*");
		qrn = QualifiedResourceNameImpl.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5::directories/*");
		assertQrn(qrn, "b17544eb-f533-4c92-a4be-1a6391600ec5", null, "directories/*");
		qrn = QualifiedResourceNameImpl.parse("qrn::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertQrn(qrn, null, "c715912d-1075-4777-af98-0c1dbd1f0391", "directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		qrn = QualifiedResourceNameImpl.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5:c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
		assertQrn(qrn, "b17544eb-f533-4c92-a4be-1a6391600ec5", "c715912d-1075-4777-af98-0c1dbd1f0391", "directories/*");
		qrn = QualifiedResourceNameImpl.parse("qrn:test:c715912d-1075-4777-af98-0c1dbd1f0391:*");
		assertQrn(qrn, "test", "c715912d-1075-4777-af98-0c1dbd1f0391", "*");
	}

	@Test
	public void shouldMatch()
	throws ParseException
	{
		QualifiedResourceNameImpl a = QualifiedResourceNameImpl.parse("qrn:test:c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		QualifiedResourceNameImpl b = QualifiedResourceNameImpl.parse("qrn:test:c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = QualifiedResourceNameImpl.parse("qrn:test:c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = QualifiedResourceNameImpl.parse("qrn:test::directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = QualifiedResourceNameImpl.parse("qrn::c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));
	}

	@Test
	public void shouldNotMatch()
	throws ParseException
	{
		QualifiedResourceNameImpl a = QualifiedResourceNameImpl.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5:c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		QualifiedResourceNameImpl b = QualifiedResourceNameImpl.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5:c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = QualifiedResourceNameImpl.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5::directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = QualifiedResourceNameImpl.parse("qrn::c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = QualifiedResourceNameImpl.parse("qrn:::directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = QualifiedResourceNameImpl.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5:c715912d-1075-4777-af98-0c1dbd1f0391:applications/*");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = QualifiedResourceNameImpl.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5:c715912d-1075-4777-af98-0c1dbd1f0391:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnResourceType()
	throws ParseException
	{
		QualifiedResourceNameImpl.parse("qrn:test:c715912d-1075-4777-af98-0c1dbd1f0391: ");
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnTooShort()
	throws ParseException
	{
		QualifiedResourceNameImpl.parse("qrn:test:c715912d-1075-4777-af98-0c1dbd1f0391");
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnMissingPrefix()
	throws ParseException
	{
		QualifiedResourceNameImpl.parse("test:c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnWrongPrefix()
	throws ParseException
	{
		QualifiedResourceNameImpl.parse("x:test:c715912d-1075-4777-af98-0c1dbd1f0391:directories/*");
	}

	@Test
	public void ShouldThrowOnBadAccount()
	{
		try
		{
			QualifiedResourceNameImpl.parse("qrn:test:c715912d:directories/*");
			fail("Did not throw");
		}
		catch(Throwable t)
		{
			ParseException p = (ParseException) t;
			assertEquals(8, p.getErrorOffset());
			assertEquals("Cannot parse Account ID in QRN", p.getMessage());
		}
	}

	private void assertQrn(QualifiedResourceNameImpl qrn, String namespace, String accountId, String path)
	{
		assertEquals(namespace, qrn.getNamespace());
		assertEquals((accountId == null ? null : UUID.fromString(accountId)), qrn.getAccountId());
		assertEquals(path, qrn.getResourcePath().toString());
	}
}
