package com.strategicgains.aclaid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.strategicgains.aclaid.exception.ResourceNotRegisteredException;
import com.strategicgains.aclaid.exception.RoleNotRegisteredException;

public class AccessControlListTest
{

	@Test
	public void shouldReflectAccessRules()
	throws Exception
	{
		AccessControlList acl = new AccessControlList();

		// setup the various roles in our system
		acl.addRole("guest");
		acl.addRole("staff", "guest");
		acl.addRole("editor", "staff");
		acl.addRole("admin");

		// add the resources
		acl.addResource("blogPost");

		// add privileges to roles and resource combinations
		acl.allow("guest", "blogPost", "view");
		acl.allow("staff", "blogPost", "edit", "submit", "revise");
		acl.allow("editor", "blogPost", "publish", "archive", "delete");

		// an admin can do anything.
		acl.allow("admin", "blogPost");

		assertTrue(acl.isAllowed("guest", "blogPost", "view"));
		assertTrue(acl.isAllowed("staff", "blogPost", "view"));
		assertFalse(acl.isAllowed("staff", "blogPost", "publish"));
		assertTrue(acl.isAllowed("staff", "blogPost", "revise"));
		assertTrue(acl.isAllowed("editor", "blogPost", "view"));
		assertFalse(acl.isAllowed("editor", "blogPost", "update"));
		assertTrue(acl.isAllowed("admin", "blogPost", "view"));
		assertTrue(acl.isAllowed("admin", "blogPost", "update"));
		assertTrue(acl.isAllowed("admin", "blogPost", "delete"));
	}

	@Test(expected = RoleNotRegisteredException.class)
	public void shouldThrowRoleNotRegistered()
	throws RoleNotRegisteredException
	{
		AccessControlList acl = new AccessControlList();
		acl.addRole("user", "unregistered");
	}

	@Test(expected = ResourceNotRegisteredException.class)
	public void shouldThrowResourceNotRegistered()
	throws ResourceNotRegisteredException, RoleNotRegisteredException
	{
		AccessControlList acl = new AccessControlList();
		acl.addRole("owner");
		acl.allow("owner", "unregistered", "does not matter");
	}
}
