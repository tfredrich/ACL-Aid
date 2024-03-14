package com.strategicgains.aclaid.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

public class ResourceNameTest
{
	@Test
	public void shouldParseUuid()
	throws ParseException
	{
		ObjectId qrn = new ObjectId("test:directories/*");
		assertQrn(qrn, "test", "directories/*");
	}

	@Test
	public void shouldParseStrings()
	throws ParseException
	{
		ObjectId qrn = new ObjectId("test:directories/*");
		assertQrn(qrn, "test", "directories/*");
		qrn = new ObjectId("b17544eb-f533-4c92-a4be-1a6391600ec5:directories/*");
		assertQrn(qrn, "b17544eb-f533-4c92-a4be-1a6391600ec5", "directories/*");
		qrn = new ObjectId(":directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertQrn(qrn, null, "directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		qrn = new ObjectId("b17544eb-f533-4c92-a4be-1a6391600ec5:directories/*");
		assertQrn(qrn, "b17544eb-f533-4c92-a4be-1a6391600ec5", "directories/*");
		qrn = new ObjectId("test:*");
		assertQrn(qrn, "test", "*");
	}

	@Test
	public void shouldMatch()
	throws ParseException
	{
		ObjectId a = new ObjectId("test:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		ObjectId b = new ObjectId("test:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = new ObjectId("test:directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = new ObjectId("test:directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = new ObjectId(":directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));
	}

	@Test
	public void shouldNotMatch()
	throws ParseException
	{
		ObjectId a = new ObjectId("b17544eb-f533-4c92-a4be-1a6391600ec5:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		ObjectId b = new ObjectId("b17544eb-f533-4c92-a4be-1a6391600ec5:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = new ObjectId("b17544eb-f533-4c92-a4be-1a6391600ec5:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = new ObjectId(":directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = new ObjectId(":directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = new ObjectId("b17544eb-f533-4c92-a4be-1a6391600ec5:applications/*");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = new ObjectId("b17544eb-f533-4c92-a4be-1a6391600ec5:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnResourceType()
	throws ParseException
	{
		new ObjectId("test: ");
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnTooShort()
	throws ParseException
	{
		new ObjectId("test");
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnWrongPrefix()
	throws ParseException
	{
		new ObjectId("x:test:directories/*");
	}

	@Test
	public void ShouldParseMultitenantStrings()
	throws ParseException
	{
		QualifiedResourceName a = new QualifiedResourceName("namespace:::directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertMultiQrn(a, "namespace", "", "", "directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		QualifiedResourceName b = new QualifiedResourceName("namespace:::directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertMultiQrn(b, "namespace", "", "", "directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		QualifiedResourceName c = new QualifiedResourceName("namespace:b17544eb-f533-4c92-a4be-1a6391600ec5:d79e866b-a24c-4a27-906c-5985dbc6e378:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertMultiQrn(c, "namespace", "b17544eb-f533-4c92-a4be-1a6391600ec5", "d79e866b-a24c-4a27-906c-5985dbc6e378", "directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		QualifiedResourceName d = new QualifiedResourceName("namespace:b17544eb-f533-4c92-a4be-1a6391600ec5:d79e866b-a24c-4a27-906c-5985dbc6e378:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertMultiQrn(d, "namespace", "b17544eb-f533-4c92-a4be-1a6391600ec5", "d79e866b-a24c-4a27-906c-5985dbc6e378", "directories/d79e866b-a24c-4a27-906c-5985dbc6e378");

		assertTrue(a.matches(b));
		assertTrue(b.matches(a));
		assertTrue(c.matches(d));
		assertTrue(d.matches(c));
		assertTrue(a.matches(c));
		assertTrue(c.matches(a));
	}

	@Test
	public void ShouldMatchMultitenantToString()
	throws ParseException
	{
		String aString = "namespace:::directories/d79e866b-a24c-4a27-906c-5985dbc6e378";
		QualifiedResourceName a = new QualifiedResourceName(aString);
		String bString = "namespace:b17544eb-f533-4c92-a4be-1a6391600ec5:d79e866b-a24c-4a27-906c-5985dbc6e378:directories/d79e866b-a24c-4a27-906c-5985dbc6e378";
		QualifiedResourceName b = new QualifiedResourceName(bString);

		assertEquals(aString, a.toString());
		assertEquals(bString, b.toString());
	}

	private void assertMultiQrn(QualifiedResourceName qrn, String namespace, String org, String account, String path)
	{
		assertQrn(qrn, namespace, path);
		assertEquals(org, qrn.hasTenantId() ? qrn.getTenantId().toString() : "");
		assertEquals(account, qrn.hasAccountId() ? qrn.getAccountId().toString() : "");
	}

	private void assertQrn(ObjectId qrn, String namespace, String path)
	{
		assertEquals(namespace, qrn.getNamespace());
		assertEquals(path, qrn.getObjectPath().toString());
	}
}
