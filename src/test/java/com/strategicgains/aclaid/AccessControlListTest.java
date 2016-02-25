package com.strategicgains.aclaid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.strategicgains.aclaid.builder.GrantBuilder;
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
	@Test
	public void shouldBuildAccessRules()
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

		GrantBuilder gb = new GrantBuilder();

		// add privileges to roles and resource combinations
		acl.allow(gb
			.role("guest")
			.resource("blogPost")
			.permissions("view")
			.build()
		);

		acl.allow(gb
			.role("staff")
			.resource("blogPost")
			.permissions("edit", "submit", "revise")
			.build()
		);

		acl.allow(gb
			.role("editor")
			.resource("blogPost")
			.permissions("publish", "archive", "delete")
			.build()
		);

		// an admin can do anything.
		acl.allow(gb
			.role("admin")
			.resource("blogPost")
			.build()
		);

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

	public class OwnerAssertion
	implements Assertion
	{
		@Override
		public boolean isAllowed(String role, Resource resource, String permission)
		{
			return false;
		}
	}
}
