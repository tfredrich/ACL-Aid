package com.strategicgains.aclaid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

public class PermissionsTest
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
		Permissions.match("iam:create:user", "iam:create:*");
		Permissions.match("iam:create:user", "iam:*:user");
		Permissions.match("iam:create:*", Permission.parse("iam:create:user"));
	}

	@Test
	public void shouldMatch()
	throws ParseException
	{
		Permission a = Permission.parse("iam:create:user");
		Permission b = Permission.parse("iam:create:user");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));

		b = Permission.parse("iam:*:user");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));

		b = Permission.parse("iam:create:*");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));

		b = Permission.parse("iam:*:*");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));

		b = Permission.parse("iam:*");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));
	}

	@Test
	public void shouldMatchActionOnly()
	throws ParseException
	{
		Permission a = Permission.parse("iam:create_user");
		Permission b = Permission.parse("iam:create_user");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));

		b = Permission.parse("iam:*");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));

		b = Permission.parse("iam:*:*");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));
	}

	@Test
	public void shouldNotMatch()
	throws ParseException
	{
		Permission a = Permission.parse("iam:create:user");
		Permission b = Permission.parse("iam:create:directory");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = Permission.parse("mfa:create:user");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = Permission.parse("mfa:*:*");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = Permission.parse("mfa:*");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = Permission.parse("iam:update:user");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = Permission.parse("iam:create");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = Permission.parse("iam:update:*");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = Permission.parse("iam:*:directory");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = Permission.parse("iam:create_user");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));
	}

	@Test(expected=ParseException.class)
	public void shouldThrowOnEmptyNode()
	throws ParseException
	{
		Permission.parse("iam:");
	}

	@Test(expected=ParseException.class)
	public void shouldThrowOnSingleNode()
	throws ParseException
	{
		Permission.parse("iam");
	}

	private void assertPermission(Permission permission, String namepsace, String action, String classifier)
	{
		assertEquals(namepsace, permission.getNamespace());
		assertEquals(action, permission.getAction());
		assertEquals(classifier, permission.getClassifier());
	}
}
