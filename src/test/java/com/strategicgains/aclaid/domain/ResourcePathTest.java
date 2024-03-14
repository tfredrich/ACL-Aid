package com.strategicgains.aclaid.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

public class ResourcePathTest
{
	@Test
	public void shouldParseStrings()
	throws ParseException
	{
		ObjectPath rp = ObjectPath.parse("directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertResourcePath(rp, "directories", "d79e866b-a24c-4a27-906c-5985dbc6e377");

		rp = ObjectPath.parse("directories/*");
		assertResourcePath(rp, "directories", "*");

		rp = ObjectPath.parse("*/*");
		assertResourcePath(rp, "*", "*");

		rp = ObjectPath.parse("*");
		assertResourcePath(rp, "*", null);
	}

	@Test
	public void shouldMatch()
	throws ParseException
	{
		ObjectPath a = ObjectPath.parse("directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		ObjectPath b = ObjectPath.parse("directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));
		
		b = ObjectPath.parse("directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = ObjectPath.parse("*/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = ObjectPath.parse("*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = ObjectPath.parse("*/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));
	}

	@Test
	public void shouldNotMatch()
	throws ParseException
	{
		ObjectPath a = ObjectPath.parse("directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		ObjectPath b = ObjectPath.parse("directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));		

		b = ObjectPath.parse("applications/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));		

		b = ObjectPath.parse("applications/*");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));		
	}

	private void assertResourcePath(ObjectPath rp, String resourceType, String value)
	{
		assertEquals(resourceType, rp.getType());
		assertEquals(value, rp.getIdentifier());
	}
}
