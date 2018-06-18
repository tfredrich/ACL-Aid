package com.strategicgains.aclaid;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.strategicgains.aclaid.builder.GrantBuilder;
import com.strategicgains.aclaid.domain.OwnableResourceImpl;
import com.strategicgains.aclaid.domain.OwnershipCondition;
import com.strategicgains.aclaid.domain.RoleImpl;

public class ConditionTest
{
	private AccessControlList acl;
	private RoleImpl<String> admin;
	private RoleImpl<String> guest;
	private RoleImpl<String> bob;
	private RoleImpl<String> bill;
	private OwnableResourceImpl<String> bobPost;
	private OwnableResourceImpl<String> billPost;

	@Before
	public void initialize()
	throws Exception
	{
		acl = new AccessControlList()
			.addRole("guest")
			.addRole("owner", "guest")
			.addRole("admin", "owner")
	
			.addResource("blogPost");

		GrantBuilder gb = new GrantBuilder();
		acl.allow(gb
			.role("guest")
			.resource("blogPost")
			.permissions("view")
			.withAssertion(new OwnershipCondition())
			.build());

		acl.allow(gb
			.role("owner")
			.resource("blogPost")
			.permissions("create", "update", "delete")
			.build());

		// "admin" inherits all.

		admin = new RoleImpl<>("admin", "admin");
		guest = new RoleImpl<>("guest", "guest");
		bob = new RoleImpl<>("bob", "guest");
		bill = new RoleImpl<>("bill", "guest");

		bobPost = new OwnableResourceImpl<>("blogPost", "bob");
		billPost = new OwnableResourceImpl<>("blogPost", "bill");
	}

	@Test
	public void testGuest()
	throws Exception
	{
		assertTrue(acl.isAllowed("guest", "blogPost", "view"));
		assertTrue(acl.isAllowed(guest, billPost, "view"));
		assertTrue(acl.isAllowed(guest, bobPost, "view"));

		assertFalse(acl.isAllowed("guest", "blogPost", "create"));
		assertFalse(acl.isAllowed(guest, bobPost, "create"));
		assertFalse(acl.isAllowed(guest, billPost, "create"));

		assertFalse(acl.isAllowed("guest", "blogPost", "update"));
		assertFalse(acl.isAllowed(guest, bobPost, "update"));
		assertFalse(acl.isAllowed(guest, billPost, "update"));

		assertFalse(acl.isAllowed("guest", "blogPost", "delete"));
		assertFalse(acl.isAllowed(guest, bobPost, "delete"));
		assertFalse(acl.isAllowed(guest, billPost, "delete"));
	}

	@Test
	public void testAdmin()
	throws Exception
	{
		assertTrue(acl.isAllowed("admin", "blogPost", "view"));
		assertTrue(acl.isAllowed(admin, billPost, "view"));
		assertTrue(acl.isAllowed(admin, bobPost, "view"));

		assertTrue(acl.isAllowed("admin", "blogPost", "create"));
		assertTrue(acl.isAllowed(admin, bobPost, "create"));
		assertTrue(acl.isAllowed(admin, billPost, "create"));

		assertTrue(acl.isAllowed("admin", "blogPost", "update"));
		assertTrue(acl.isAllowed(admin, bobPost, "update"));
		assertTrue(acl.isAllowed(admin, billPost, "update"));

		assertTrue(acl.isAllowed("admin", "blogPost", "delete"));
		assertTrue(acl.isAllowed(admin, bobPost, "delete"));
		assertTrue(acl.isAllowed(admin, billPost, "delete"));
	}

	@Test
	public void testOwner()
	throws Exception
	{
		assertTrue(acl.isAllowed("owner", "blogPost", "view"));
		assertTrue(acl.isAllowed(bill, billPost, "view"));
		assertTrue(acl.isAllowed(bill, bobPost, "view"));
		assertTrue(acl.isAllowed(bob, billPost, "view"));
		assertTrue(acl.isAllowed(bob, bobPost, "view"));

		assertTrue(acl.isAllowed("owner", "blogPost", "create"));
		assertTrue(acl.isAllowed(bob, bobPost, "create"));
		assertFalse(acl.isAllowed(bob, billPost, "create"));
		assertFalse(acl.isAllowed(bill, bobPost, "create"));
		assertTrue(acl.isAllowed(bill, billPost, "create"));

		assertTrue(acl.isAllowed("owner", "blogPost", "update"));
		assertTrue(acl.isAllowed(bob, bobPost, "update"));
		assertFalse(acl.isAllowed(bob, billPost, "update"));
		assertFalse(acl.isAllowed(bill, bobPost, "update"));
		assertTrue(acl.isAllowed(bill, billPost, "update"));

		assertTrue(acl.isAllowed("owner", "blogPost", "delete"));
		assertTrue(acl.isAllowed(bob, bobPost, "delete"));
		assertFalse(acl.isAllowed(bob, billPost, "delete"));
		assertFalse(acl.isAllowed(bill, bobPost, "delete"));
		assertTrue(acl.isAllowed(bill, billPost, "delete"));
	}
}
