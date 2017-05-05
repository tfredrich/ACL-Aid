package com.strategicgains.aclaid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

public class PermissionTest
{
	@Test
	public void shouldParseStrings()
	throws ParseException
	{
		Permission permission = Permission.parse("iam:create:user");
		assertPermission(permission, "iam", "create", "user");
		permission = Permission.parse("iam:*:user");
		assertPermission(permission, "iam", "*", "user");
		permission = Permission.parse("iam:create:*");
		assertPermission(permission, "iam", "create", "*");
		permission = Permission.parse("iam:*:*");
		assertPermission(permission, "iam", "*", "*");
		permission = Permission.parse("iam:send_message");
		assertPermission(permission, "iam", "send_message", null);
	}

	@Test
	public void shouldMatchStrings()
	throws ParseException
	{
		Permission.match("iam:create:user", "iam:create:*");
		Permission.match("iam:create:user", "iam:*:user");
		Permission.match("iam:create:*", Permission.parse("iam:create:user"));
	}

	@Test
	public void shouldMatch()
	throws ParseException
	{
		Permission a = Permission.parse("iam:create:user");
		Permission b = Permission.parse("iam:create:user");
		assertTrue(Permission.match(a, b));
		assertTrue(Permission.match(b, a));

		b = Permission.parse("iam:*:user");
		assertTrue(Permission.match(a, b));
		assertTrue(Permission.match(b, a));

		b = Permission.parse("iam:create:*");
		assertTrue(Permission.match(a, b));
		assertTrue(Permission.match(b, a));

		b = Permission.parse("iam:*:*");
		assertTrue(Permission.match(a, b));
		assertTrue(Permission.match(b, a));

		b = Permission.parse("iam:*");
		assertTrue(Permission.match(a, b));
		assertTrue(Permission.match(b, a));
	}

	@Test
	public void shouldMatchActionOnly()
	throws ParseException {
		Permission a = Permission.parse("iam:create_user");
		Permission b = Permission.parse("iam:create_user");
		assertTrue(Permission.match(a, b));
		assertTrue(Permission.match(b, a));

		b = Permission.parse("iam:*");
		assertTrue(Permission.match(a, b));
		assertTrue(Permission.match(b, a));

		b = Permission.parse("iam:*:*");
		assertTrue(Permission.match(a, b));
		assertTrue(Permission.match(b, a));
	}

	@Test
	public void shouldNotMatch()
	throws ParseException {
		Permission a = Permission.parse("iam:create:user");
		Permission b = Permission.parse("iam:create:directory");
		assertFalse(Permission.match(a, b));
		assertFalse(Permission.match(b, a));

		b = Permission.parse("mfa:create:user");
		assertFalse(Permission.match(a, b));
		assertFalse(Permission.match(b, a));

		b = Permission.parse("mfa:*:*");
		assertFalse(Permission.match(a, b));
		assertFalse(Permission.match(b, a));

		b = Permission.parse("mfa:*");
		assertFalse(Permission.match(a, b));
		assertFalse(Permission.match(b, a));

		b = Permission.parse("iam:update:user");
		assertFalse(Permission.match(a, b));
		assertFalse(Permission.match(b, a));

		b = Permission.parse("iam:create");
		assertFalse(Permission.match(a, b));
		assertFalse(Permission.match(b, a));

		b = Permission.parse("iam:update:*");
		assertFalse(Permission.match(a, b));
		assertFalse(Permission.match(b, a));

		b = Permission.parse("iam:*:directory");
		assertFalse(Permission.match(a, b));
		assertFalse(Permission.match(b, a));

		b = Permission.parse("iam:create_user");
		assertFalse(Permission.match(a, b));
		assertFalse(Permission.match(b, a));
	}

	private void assertPermission(Permission permission, String service, String action, String resourceType) {
		assertEquals(service, permission.getNamespace());
		assertEquals(action, permission.getAction());
		assertEquals(resourceType, permission.getClassifier());
	}
}
