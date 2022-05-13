package com.strategicgains.aclaid.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

import com.strategicgains.aclaid.domain.ResourceName;

public class ResourceNameTest
{
	@Test
	public void shouldParseUuid()
	throws ParseException
	{
		ResourceName qrn = ResourceName.parse("test:directories/*");
		assertQrn(qrn, "test", "directories/*");
	}

	@Test
	public void shouldParseStrings()
	throws ParseException
	{
		ResourceName qrn = ResourceName.parse("test:directories/*");
		assertQrn(qrn, "test", "directories/*");
		qrn = ResourceName.parse("b17544eb-f533-4c92-a4be-1a6391600ec5:directories/*");
		assertQrn(qrn, "b17544eb-f533-4c92-a4be-1a6391600ec5", "directories/*");
		qrn = ResourceName.parse(":directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertQrn(qrn, null, "directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		qrn = ResourceName.parse("b17544eb-f533-4c92-a4be-1a6391600ec5:directories/*");
		assertQrn(qrn, "b17544eb-f533-4c92-a4be-1a6391600ec5", "directories/*");
		qrn = ResourceName.parse("test:*");
		assertQrn(qrn, "test", "*");
	}

	@Test
	public void shouldMatch()
	throws ParseException
	{
		ResourceName a = ResourceName.parse("test:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		ResourceName b = ResourceName.parse("test:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = ResourceName.parse("test:directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = ResourceName.parse("test:directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = ResourceName.parse(":directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));
	}

	@Test
	public void shouldNotMatch()
	throws ParseException
	{
		ResourceName a = ResourceName.parse("b17544eb-f533-4c92-a4be-1a6391600ec5:directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		ResourceName b = ResourceName.parse("b17544eb-f533-4c92-a4be-1a6391600ec5:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = ResourceName.parse("b17544eb-f533-4c92-a4be-1a6391600ec5:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = ResourceName.parse(":directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = ResourceName.parse(":directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = ResourceName.parse("b17544eb-f533-4c92-a4be-1a6391600ec5:applications/*");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));

		b = ResourceName.parse("b17544eb-f533-4c92-a4be-1a6391600ec5:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnResourceType()
	throws ParseException
	{
		ResourceName.parse("test: ");
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnTooShort()
	throws ParseException
	{
		ResourceName.parse("test");
	}

	@Test(expected=ParseException.class)
	public void ShouldThrowOnWrongPrefix()
	throws ParseException
	{
		ResourceName.parse("x:test:directories/*");
	}

	private void assertQrn(ResourceName qrn, String namespace, String path)
	{
		assertEquals(namespace, qrn.getNamespace());
		assertEquals(path, qrn.getResourcePath().toString());
	}
}
