package com.strategicgains.aclaid;

import static org.junit.Assert.*;

import org.junit.Test;

import com.strategicgains.aclaid.exception.RoleNotRegisteredException;

public class AccessControlListTest
{

	@Test
	public void shouldReflectAccessRules()
	throws Exception
	{
		AccessControlList acl = new AccessControlList();

		// setup the various roles in our system
		acl.role("guest");
		acl.role("staff", "guest");
		acl.role("editor", "staff");
		acl.role("admin");

		// add the resources
		acl.resource("blogPost");

		// add privileges to roles and resource combinations
		acl.allow("guest", "blogPost", "view");
		acl.allow("staff", "blogPost", "view", "edit", "submit", "revise");
		acl.allow("editor", "blogPost", "view", "edit", "submit", "revise", "publish", "archive", "delete");

		// an admin can do anything.
		acl.allow("admin", "blogPost");

		assertTrue(acl.isAllowed("guest", "blogPost", "view"));
		assertFalse(acl.isAllowed("staff", "blogPost", "publish"));
		assertTrue(acl.isAllowed("staff", "blogPost", "revise"));
		assertTrue(acl.isAllowed("editor", "blogPost", "view"));
		assertFalse(acl.isAllowed("editor", "blogPost", "update"));
		assertTrue(acl.isAllowed("admin", "blogPost", "view"));
		assertTrue(acl.isAllowed("admin", "blogPost", "update"));
	}

	@Test(expected = RoleNotRegisteredException.class)
	public void shouldThrowRoleNotRegistered()
	throws RoleNotRegisteredException
	{
		AccessControlList acl = new AccessControlList();
		acl.role("user", "unregistered");
	}
}
