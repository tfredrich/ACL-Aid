package com.strategicgains.aclaid.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.UUID;

import org.junit.Test;

public class PolicyStatementTest
{
	@Test
	public void testToString()
	{
		assertEquals("{resources: [], allowed: [], denied: [], hasCondition: false}", new PolicyStatement().toString());
	}

	@Test
	public void testResourceMatches()
	throws ParseException
	{
		PolicyStatement p = new PolicyStatement();
		p.allow("test:do:one", "test:do:two");
		p.deny("test:dont:one", "test:dont:two");
		p.setResource("test:blogPost/*", "test:user/*");

		assertTrue(p.appliesToResource(new TransientResource("test", "blogPost", UUID.randomUUID())));
		assertTrue(p.appliesToResource(new TransientResource("test", "user", UUID.randomUUID())));
		assertFalse(p.appliesToResource(new TransientResource("test", "foo", UUID.randomUUID())));

		assertTrue(p.evaluate(null, "test:do:one"));
		assertTrue(p.evaluate(null, "test:do:two"));
		assertFalse(p.evaluate(null, "test:dont:one"));
		assertFalse(p.evaluate(null, "test:dont:two"));
		assertFalse(p.evaluate(null, "test:foo:bar"));
	}

	@Test
	public void testResourceTypeWildcard()
	throws ParseException
	{
		PolicyStatement p = new PolicyStatement();
		p.allow("test:do:one", "test:do:two");
		p.deny("test:dont:one", "test:dont:two");
		p.setResource("test:*");

		assertTrue(p.appliesToResource(new TransientResource("test", "blogPost", UUID.randomUUID())));
		assertTrue(p.appliesToResource(new TransientResource("test", "user", UUID.randomUUID())));
		assertTrue(p.appliesToResource(new TransientResource("test", "foo", UUID.randomUUID())));

		assertTrue(p.evaluate(null, "test:do:one"));
		assertTrue(p.evaluate(null, "test:do:two"));
		assertFalse(p.evaluate(null, "test:dont:one"));
		assertFalse(p.evaluate(null, "test:dont:two"));
		assertFalse(p.evaluate(null, "test:foo:bar"));
	}

	@Test
	public void testPermissionWildcard()
	throws ParseException
	{
		PolicyStatement p = new PolicyStatement();
		p.allow("test:do:*");
		p.deny("test:do:three");
		p.setResource("test:*");

		assertTrue(p.evaluate(null, "test:do:*"));
		assertFalse(p.evaluate(null, "test:do:two"));
		assertFalse(p.evaluate(null, "test:do:three"));
		assertFalse(p.evaluate(null, "test:do:four"));
		assertFalse(p.evaluate(null, "test:foo:bar"));
	}
}
