package com.strategicgains.aclaid.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

public class ObjectIdTest
{
	@Test
	public void shouldParseUuid()
	throws ParseException
	{
		ObjectId objectId = new ObjectId("test:directories/*");
		assertobjectId(objectId, "test", "directories/*");
	}

	@Test
	public void shouldParseStrings()
	throws ParseException
	{
		ObjectId objectId = new ObjectId("test:directories/*");
		assertobjectId(objectId, "test", "directories/*");
		objectId = new ObjectId("b17544eb-f533-4c92-a4be-1a6391600ec5:directories/*");
		assertobjectId(objectId, "b17544eb-f533-4c92-a4be-1a6391600ec5", "directories/*");
		objectId = new ObjectId(":directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		assertobjectId(objectId, null, "directories/d79e866b-a24c-4a27-906c-5985dbc6e377");
		objectId = new ObjectId("b17544eb-f533-4c92-a4be-1a6391600ec5:directories/*");
		assertobjectId(objectId, "b17544eb-f533-4c92-a4be-1a6391600ec5", "directories/*");
		objectId = new ObjectId("test:*");
		assertobjectId(objectId, "test", "*");
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
		assertMultiobjectId(a, "namespace", "", "", "directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		QualifiedResourceName b = new QualifiedResourceName("namespace:::directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertMultiobjectId(b, "namespace", "", "", "directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		QualifiedResourceName c = new QualifiedResourceName("namespace:b17544eb-f533-4c92-a4be-1a6391600ec5:d79e866b-a24c-4a27-906c-5985dbc6e378:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertMultiobjectId(c, "namespace", "b17544eb-f533-4c92-a4be-1a6391600ec5", "d79e866b-a24c-4a27-906c-5985dbc6e378", "directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		QualifiedResourceName d = new QualifiedResourceName("namespace:b17544eb-f533-4c92-a4be-1a6391600ec5:d79e866b-a24c-4a27-906c-5985dbc6e378:directories/d79e866b-a24c-4a27-906c-5985dbc6e378");
		assertMultiobjectId(d, "namespace", "b17544eb-f533-4c92-a4be-1a6391600ec5", "d79e866b-a24c-4a27-906c-5985dbc6e378", "directories/d79e866b-a24c-4a27-906c-5985dbc6e378");

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

	private void assertMultiobjectId(QualifiedResourceName objectId, String namespace, String org, String account, String path)
	{
		assertobjectId(objectId, namespace, path);
		assertEquals(org, objectId.hasTenantId() ? objectId.getTenantId().toString() : "");
		assertEquals(account, objectId.hasAccountId() ? objectId.getAccountId().toString() : "");
	}

	private void assertobjectId(ObjectId objectId, String namespace, String path)
	{
		assertEquals(namespace, objectId.getNamespace());
		assertEquals(path, objectId.getPath().toString());
	}
}
