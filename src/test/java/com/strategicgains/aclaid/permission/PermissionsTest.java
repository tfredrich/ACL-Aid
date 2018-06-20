package com.strategicgains.aclaid.permission;

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
		PermissionImpl permission = PermissionImpl.parse("iam:create:user");
		assertPermission(permission, "iam", "create", "user");
		permission = PermissionImpl.parse("iam:*:user");
		assertPermission(permission, "iam", "*", "user");
		permission = PermissionImpl.parse("iam:create:*");
		assertPermission(permission, "iam", "create", "*");
		permission = PermissionImpl.parse("iam:*:*");
		assertPermission(permission, "iam", "*", "*");
		permission = PermissionImpl.parse("iam:send_message");
		assertPermission(permission, "iam", "send_message", null);
	}

	@Test
	public void shouldMatchStrings()
	throws ParseException
	{
		Permissions.match("iam:create:user", "iam:create:*");
		Permissions.match("iam:create:user", "iam:*:user");
		Permissions.match("iam:create:*", PermissionImpl.parse("iam:create:user"));
	}

	@Test
	public void shouldMatch()
	throws ParseException
	{
		PermissionImpl a = PermissionImpl.parse("iam:create:user");
		PermissionImpl b = PermissionImpl.parse("iam:create:user");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));

		b = PermissionImpl.parse("iam:*:user");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));

		b = PermissionImpl.parse("iam:create:*");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));

		b = PermissionImpl.parse("iam:*:*");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));

		b = PermissionImpl.parse("iam:*");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));
	}

	@Test
	public void shouldMatchActionOnly()
	throws ParseException
	{
		PermissionImpl a = PermissionImpl.parse("iam:create_user");
		PermissionImpl b = PermissionImpl.parse("iam:create_user");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));

		b = PermissionImpl.parse("iam:*");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));

		b = PermissionImpl.parse("iam:*:*");
		assertTrue(Permissions.match(a, b));
		assertTrue(Permissions.match(b, a));
	}

	@Test
	public void shouldNotMatch()
	throws ParseException
	{
		PermissionImpl a = PermissionImpl.parse("iam:create:user");
		PermissionImpl b = PermissionImpl.parse("iam:create:directory");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = PermissionImpl.parse("mfa:create:user");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = PermissionImpl.parse("mfa:*:*");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = PermissionImpl.parse("mfa:*");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = PermissionImpl.parse("iam:update:user");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = PermissionImpl.parse("iam:create");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = PermissionImpl.parse("iam:update:*");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = PermissionImpl.parse("iam:*:directory");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));

		b = PermissionImpl.parse("iam:create_user");
		assertFalse(Permissions.match(a, b));
		assertFalse(Permissions.match(b, a));
	}

	@Test(expected=ParseException.class)
	public void shouldThrowOnEmptyNode()
	throws ParseException
	{
		PermissionImpl.parse("iam:");
	}

	@Test(expected=ParseException.class)
	public void shouldThrowOnSingleNode()
	throws ParseException
	{
		PermissionImpl.parse("iam");
	}

	private void assertPermission(PermissionImpl permission, String namepsace, String action, String classifier)
	{
		assertEquals(namepsace, permission.getNamespace());
		assertEquals(action, permission.getAction());
		assertEquals(classifier, permission.getClassifier());
	}
}
