package com.strategicgains.aclaid.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

import com.strategicgains.aclaid.domain.ResourcePath;

public class ResourcePathTest
{
	@Test
	public void shouldParseStrings()
	throws ParseException
	{
		ResourcePath rp = ResourcePath.parse("directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertResourcePath(rp, "directories", "d79e866b-a24c-4a27-906c-5985dbc6e377");

		rp = ResourcePath.parse("directories/*");
		assertResourcePath(rp, "directories", "*");

		rp = ResourcePath.parse("*/*");
		assertResourcePath(rp, "*", "*");

		rp = ResourcePath.parse("*");
		assertResourcePath(rp, "*", null);
	}

	@Test
	public void shouldMatch()
	throws ParseException
	{
		ResourcePath a = ResourcePath.parse("directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		ResourcePath b = ResourcePath.parse("directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));
		
		b = ResourcePath.parse("directories/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = ResourcePath.parse("*/*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = ResourcePath.parse("*");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));

		b = ResourcePath.parse("*/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertTrue(a.matches(b));
		assertTrue(b.matches(a));
	}

	@Test
	public void shouldNotMatch()
	throws ParseException
	{
		ResourcePath a = ResourcePath.parse("directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		ResourcePath b = ResourcePath.parse("directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));		

		b = ResourcePath.parse("applications/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));		

		b = ResourcePath.parse("applications/*");
		assertFalse(a.matches(b));
		assertFalse(b.matches(a));		
	}

	private void assertResourcePath(ResourcePath rp, String resourceType, String value)
	{
		assertEquals(resourceType, rp.getResourceType());
		assertEquals(value, rp.getIdentifier());
	}
}
