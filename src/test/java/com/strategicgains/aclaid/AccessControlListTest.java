package com.strategicgains.aclaid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.strategicgains.aclaid.builder.GrantBuilder;
import com.strategicgains.aclaid.domain.BlogPost;
import com.strategicgains.aclaid.domain.ResourceImpl;
import com.strategicgains.aclaid.domain.RoleImpl;
import com.strategicgains.aclaid.domain.User;
import com.strategicgains.aclaid.exception.ResourceNotRegisteredException;
import com.strategicgains.aclaid.exception.RoleNotRegisteredException;

public class AccessControlListTest
{
	@Test
	public void shouldHonorInheritence()
	throws Exception
	{
		AccessControlList acl = new AccessControlList();		

		// setup the various roles in our system
		acl.addRole("guest");
		acl.addRole("admin", "guest");
		acl.addRole("system", "admin");

		// add the resources
		acl.addResource("blogPost");

		acl.allow("guest", "blogPost", "view");
		acl.allow("admin", "blogPost", "create", "update", "delete");
		// purposefully not allowing extra system permissions to ensure inheritance works in that case.

		assertTrue(acl.isAllowed("guest", "blogPost", "view"));

		assertTrue(acl.isAllowed("admin", "blogPost", "create"));
		assertTrue(acl.isAllowed("admin", "blogPost", "view"));
		assertTrue(acl.isAllowed("admin", "blogPost", "update"));
		assertTrue(acl.isAllowed("admin", "blogPost", "delete"));

		assertTrue(acl.isAllowed("system", "blogPost", "create"));
		assertTrue(acl.isAllowed("system", "blogPost", "view"));
		assertTrue(acl.isAllowed("system", "blogPost", "update"));
		assertTrue(acl.isAllowed("system", "blogPost", "delete"));

		RoleImpl<String> admin = new RoleImpl<>("admin", "admin");
		RoleImpl<String> system = new RoleImpl<>("system", "system");
		ResourceImpl blogPost = new ResourceImpl("blogPost");

		assertTrue(acl.isAllowed(admin, blogPost, "create"));
		assertTrue(acl.isAllowed(admin, blogPost, "view"));
		assertTrue(acl.isAllowed(admin, blogPost, "update"));
		assertTrue(acl.isAllowed(admin, blogPost, "delete"));

		assertTrue(acl.isAllowed(system, blogPost, "create"));
		assertTrue(acl.isAllowed(system, blogPost, "view"));
		assertTrue(acl.isAllowed(system, blogPost, "update"));
		assertTrue(acl.isAllowed(system, blogPost, "delete"));
	}

	@Test
	public void shouldReflectResourceAccessRules()
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
	public void shouldReflectWildcardAccessRules()
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
		acl.allow("guest", null, "view_post");
		acl.allow("staff", null, "edit_post", "submit_post", "revise_post");
		acl.allow("editor", null, "publish_post", "archive_post", "delete_post");

		// an admin can do anything.
		acl.allow("admin", null);

		assertTrue(acl.isAllowed("guest", null, "view_post"));
		assertTrue(acl.isAllowed("staff", null, "view_post"));
		assertFalse(acl.isAllowed("staff", null, "publish_post"));
		assertTrue(acl.isAllowed("staff", null, "revise_post"));
		assertTrue(acl.isAllowed("editor", null, "view_post"));
		assertFalse(acl.isAllowed("editor", null, "update_post"));
		assertTrue(acl.isAllowed("admin", null, "view_post"));
		assertTrue(acl.isAllowed("admin", null, "update_post"));
		assertTrue(acl.isAllowed("admin", null, "delete_post"));

		assertTrue(acl.isAllowed("guest", "*", "view_post"));
		assertTrue(acl.isAllowed("staff", "*", "view_post"));
		assertFalse(acl.isAllowed("staff", "*", "publish_post"));
		assertTrue(acl.isAllowed("staff", "*", "revise_post"));
		assertTrue(acl.isAllowed("editor", "*", "view_post"));
		assertFalse(acl.isAllowed("editor", "*", "update_post"));
		assertTrue(acl.isAllowed("admin", "*", "view_post"));
		assertTrue(acl.isAllowed("admin", "*", "update_post"));
		assertTrue(acl.isAllowed("admin", "*", "delete_post"));

		assertTrue(acl.isAllowed("guest", "blogPost", "view_post"));
		assertTrue(acl.isAllowed("staff", "blogPost", "view_post"));
		assertFalse(acl.isAllowed("staff", "blogPost", "publish_post"));
		assertTrue(acl.isAllowed("staff", "blogPost", "revise_post"));
		assertTrue(acl.isAllowed("editor", "blogPost", "view_post"));
		assertFalse(acl.isAllowed("editor", "blogPost", "update_post"));
		assertTrue(acl.isAllowed("admin", "blogPost", "view_post"));
		assertTrue(acl.isAllowed("admin", "blogPost", "update_post"));
		assertTrue(acl.isAllowed("admin", "blogPost", "delete_post"));
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

	@Test
	public void shouldBuildAccessRulesWithAssertion()
	throws Exception
	{
		AccessControlList acl = new AccessControlList();

		// setup the various roles in our system
		acl.addRole("guest");
		acl.addRole("owner", "guest");

		// add the resources
		acl.addResource("blogPost");

		GrantBuilder gb = new GrantBuilder();

		// add privileges to roles and resource combinations
		acl.allow(gb
			.role("guest")
			.resource("blogPost")
			.permissions("view")
			.withAssertion(new OwnerAssertion())
			.build()
		);

		acl.allow(gb
			.role("owner")
			.resource("blogPost")
			.permissions("edit", "revise", "delete")
			.build()
		);

		assertTrue(acl.isAllowed("guest", "blogPost", "view"));

		User bill = new User("bill");
		User bob = new User("bob");
		BlogPost post = new BlogPost();
		post.setOwner(bill.getId());
		assertTrue(acl.isAllowed(bill, post, "view"));
		assertTrue(acl.isAllowed(bill, post, "edit"));
		assertTrue(acl.isAllowed(bill, post, "revise"));
		assertTrue(acl.isAllowed(bill, post, "delete"));
		assertTrue(acl.isAllowed(bob, post, "view"));
		assertFalse(acl.isAllowed(bob, post, "edit"));
		assertFalse(acl.isAllowed(bob, post, "revise"));
		assertFalse(acl.isAllowed(bob, post, "delete"));
	}

	@Test
	public void shouldBuildWildcardAccessRules()
	throws Exception
	{
		AccessControlList acl = new AccessControlList();

		// setup the various roles in our system
		acl.addRole("guest");
		acl.addRole("staff", "guest");
		acl.addRole("editor", "staff");
		acl.addRole("admin");

		GrantBuilder gb = new GrantBuilder();

		// add privileges to roles and resource combinations
		acl.allow(gb
			.role("guest")
			.permissions("view")
			.build()
		);

		acl.allow(gb
			.role("staff")
			.permissions("edit", "submit", "revise")
			.build()
		);

		acl.allow(gb
			.role("editor")
			.permissions("publish", "archive", "delete")
			.build()
		);

		// an admin can do anything.
		acl.allow(gb
			.role("admin")
			.build()
		);

		assertTrue(acl.isAllowed("guest", null, "view"));
		assertTrue(acl.isAllowed("staff", null, "view"));
		assertFalse(acl.isAllowed("staff", null, "publish"));
		assertTrue(acl.isAllowed("staff", null, "revise"));
		assertTrue(acl.isAllowed("editor", null, "view"));
		assertFalse(acl.isAllowed("editor", null, "update"));
		assertTrue(acl.isAllowed("admin", null, "view"));
		assertTrue(acl.isAllowed("admin", null, "update"));
		assertTrue(acl.isAllowed("admin", null, "delete"));

		assertTrue(acl.isAllowed("guest", "*", "view"));
		assertTrue(acl.isAllowed("staff", "*", "view"));
		assertFalse(acl.isAllowed("staff", "*", "publish"));
		assertTrue(acl.isAllowed("staff", "*", "revise"));
		assertTrue(acl.isAllowed("editor", "*", "view"));
		assertFalse(acl.isAllowed("editor", "*", "update"));
		assertTrue(acl.isAllowed("admin", "*", "view"));
		assertTrue(acl.isAllowed("admin", "*", "update"));
		assertTrue(acl.isAllowed("admin", "*", "delete"));

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
	public void shouldBuildWildcardAccessRulesWithAssertion()
	throws Exception
	{
		AccessControlList acl = new AccessControlList();

		// setup the various roles in our system
		acl.addRole("guest");
		acl.addRole("owner", "guest");

		GrantBuilder gb = new GrantBuilder();

		// add privileges to roles and resource combinations
		acl.allow(gb
			.role("guest")
			.permissions("view")
			.withAssertion(new OwnerAssertion())
			.build()
		);

		acl.allow(gb
			.role("owner")
			.permissions("edit", "revise", "delete")
			.build()
		);

		assertTrue(acl.isAllowed("guest", null, "view"));
		assertFalse(acl.isAllowed("guest", null, "delete"));
		assertTrue(acl.isAllowed("owner", null, "view"));
		assertTrue(acl.isAllowed("owner", null, "edit"));
		assertTrue(acl.isAllowed("owner", null, "revise"));
		assertTrue(acl.isAllowed("owner", null, "delete"));

		assertTrue(acl.isAllowed("guest", "*", "view"));
		assertFalse(acl.isAllowed("guest", "*", "delete"));
		assertTrue(acl.isAllowed("owner", "*", "view"));
		assertTrue(acl.isAllowed("owner", "*", "edit"));
		assertTrue(acl.isAllowed("owner", "*", "revise"));
		assertTrue(acl.isAllowed("owner", "*", "delete"));

		assertTrue(acl.isAllowed("guest", "blogPost", "view"));
		assertFalse(acl.isAllowed("guest", "blogPost", "delete"));
		assertTrue(acl.isAllowed("owner", "blogPost", "view"));
		assertTrue(acl.isAllowed("owner", "blogPost", "edit"));
		assertTrue(acl.isAllowed("owner", "blogPost", "revise"));
		assertTrue(acl.isAllowed("owner", "blogPost", "delete"));

		User bill = new User("bill");
		User bob = new User("bob");
		BlogPost post = new BlogPost();
		post.setOwner(bill.getId());
		assertTrue(acl.isAllowed(bill, post, "view"));
		assertTrue(acl.isAllowed(bill, post, "edit"));
		assertTrue(acl.isAllowed(bill, post, "revise"));
		assertTrue(acl.isAllowed(bill, post, "delete"));
		assertTrue(acl.isAllowed(bob, post, "view"));
		assertFalse(acl.isAllowed(bob, post, "edit"));
		assertFalse(acl.isAllowed(bob, post, "revise"));
		assertFalse(acl.isAllowed(bob, post, "delete"));
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
	implements Condition
	{
		@Override
		public boolean isMet(AccessControlList acl, Role role, Resource resource, String permissionId)
		{
			User user = null;
			BlogPost post = null;

			if ("guest".equals(role.getRoleId()))
			{
				user = (User) role;
			}

			if ("blogPost".equals(resource.getResourceId()))
			{
				post = (BlogPost) resource;
			}

			if (user != null && post != null)
			{
				if (isOwner(user, post))
				{
					return acl.isAllowed("owner", post.getResourceId(), permissionId);
				}
			}

			return false;
		}

		private boolean isOwner(User user, BlogPost post)
		{
			return (user.getId().equals(post.getOwner()));
		}
	}
}
