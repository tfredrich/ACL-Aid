package com.strategicgains.aclaid.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

public class QualifiedResourceNameTest
{
	@Test
	public void shouldParseUuid()
	throws ParseException
	{
		QualifiedResourceName qrn = QualifiedResourceName.parse("qrn:test:directories/*");
		assertQrn(qrn, "test", "directories/*");
	}

	@Test
	public void shouldParseStrings()
	throws ParseException
	{
		QualifiedResourceName qrn = QualifiedResourceName.parse("qrn:test:directories/*");
		assertQrn(qrn, "test", "directories/*");
		qrn = QualifiedResourceName.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5:directories/*");
		assertQrn(qrn, "b17544eb-f533-4c92-a4be-1a6391600ec5", "directories/*");
		qrn = QualifiedResourceName.parse("qrn::directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertQrn(qrn, null, "directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		qrn = QualifiedResourceName.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5:directories/*");
		assertQrn(qrn, "b17544eb-f533-4c92-a4be-1a6391600ec5", "directories/*");
		qrn = QualifiedResourceName.parse("qrn:test:*");
		assertQrn(qrn, "test", "*");
	}

	@Test
	public void shouldMatch()
	throws ParseException
	{
		QualifiedResourceName a = QualifiedResourceName.parse("qrn:test:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		QualifiedResourceName b = QualifiedResourceName.parse("qrn:test:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = QualifiedResourceName.parse("qrn:test:directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = QualifiedResourceName.parse("qrn:test:directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = QualifiedResourceName.parse("qrn::directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));
	}

	@Test
	public void shouldNotMatch()
	throws ParseException
	{
		QualifiedResourceName a = QualifiedResourceName.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		QualifiedResourceName b = QualifiedResourceName.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = QualifiedResourceName.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = QualifiedResourceName.parse("qrn::directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = QualifiedResourceName.parse("qrn::directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = QualifiedResourceName.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5:applications/*");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = QualifiedResourceName.parse("qrn:b17544eb-f533-4c92-a4be-1a6391600ec5:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnResourceType()
	throws ParseException
	{
		QualifiedResourceName.parse("qrn:test: ");
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnTooShort()
	throws ParseException
	{
		QualifiedResourceName.parse("qrn:test");
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnMissingPrefix()
	throws ParseException
	{
		QualifiedResourceName.parse("test:directories/*");
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnWrongPrefix()
	throws ParseException
	{
		QualifiedResourceName.parse("x:test:directories/*");
	}

	private void assertQrn(QualifiedResourceName qrn, String namespace, String path)
	{
		assertEquals(namespace, qrn.getNamespace());
		assertEquals(path, qrn.getResourcePath().toString());
	}
}
