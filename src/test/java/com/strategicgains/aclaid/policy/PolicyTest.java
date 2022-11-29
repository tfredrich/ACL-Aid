package com.strategicgains.aclaid.policy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.UUID;

import org.junit.Test;

import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.UserSet;

public class PolicyTest
{
	private static final UUID ADMIN_ID = UUID.randomUUID();

	@Test
	public void testPermissionOnlyMatching()
	throws ParseException
	{
		String one = "test:do:one";
		String two = "test:do:two";
		String three = "test:do:three";
		String four = "test:do:four";

		Policy p = new Policy();
		p.statement()
			.setResource("test:blogPost/*", "test:user/*")
			.allow(one, two);
		p.statement()
			.setResource("test:foo/*")
			.allow(one, two, three, four)
			.deny(three);

		Resource x = new TransientResource("test", "blogPost", UUID.randomUUID());
		PolicyContext c = new PolicyContext(null, x);
		assertTrue(p.evaluate(c, one));
		assertTrue(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		Resource y = new TransientResource("test", "blogPost", UUID.randomUUID());
		c = new PolicyContext(null, y);
		assertTrue(p.evaluate(c, one));
		assertTrue(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		Resource z = new TransientResource("test", "foo", UUID.randomUUID());
		c = new PolicyContext(null, z);
		assertTrue(p.evaluate(c, one));
		assertTrue(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertTrue(p.evaluate(c, four));
	}
	
	@Test
	public void testMatchEnforcingTenancy()
	throws ParseException
	{
		UserSet p1 = new UserSet(new ResourceName("test", "user", UUID.randomUUID().toString()));
		UserSet admin = new UserSet(new ResourceName("test", "user", ADMIN_ID.toString()));
		Policy p = new Policy();
		Condition tenancy = new TenancyCondition(admin.getResource());
		String one = "test:do:one";
		String two = "test:do:two";
		String three = "test:do:three";
		String four = "test:do:four";

		p.statement()
			.setResource("test:blogPost/*", "test:user/*")
			.allow(one, two)
			.withCondition(tenancy);
		p.statement()
			.setResource("test:foo/*")
			.allow(one, two, three, four)
			.deny("test:do:three")
			.withCondition(tenancy);
		p.statement()
			.setUserset(String.format("iam:user/%s", ADMIN_ID))
			.setResource("test:foo/*")
			.allow(one, two, three, four)
			.deny("test:do:three")
			.withCondition(tenancy);

		Resource x = new TransientResource("test", "blogPost", UUID.randomUUID());
		PolicyContext c = new PolicyContext(p1, x);
		assertTrue(p.evaluate(c, one));
		assertTrue(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		x = new TransientResource("test", "blogPost", UUID.randomUUID());
		c = new PolicyContext(admin, x);
		assertFalse(p.evaluate(c, one));
		assertFalse(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		x = new TransientResource("test", "blogPost", UUID.randomUUID());
		c = new PolicyContext(p1, x);
		assertFalse(p.evaluate(c, one));
		assertFalse(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		x = new TransientResource("test", "blogPost", UUID.randomUUID());
		c = new PolicyContext(admin, x);
		assertFalse(p.evaluate(c, one));
		assertFalse(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		x = new TransientResource("test", "blogPost", UUID.randomUUID());
		c = new PolicyContext(p1, x);
		assertFalse(p.evaluate(c, one));
		assertFalse(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		x = new TransientResource("test", "blogPost", UUID.randomUUID());
		c = new PolicyContext(admin, x);
		assertTrue(p.evaluate(c, one));
		assertTrue(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		Resource y = new TransientResource("test", "user", UUID.randomUUID());
		c = new PolicyContext(p1, y);
		assertTrue(p.evaluate(c, one));
		assertTrue(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		y = new TransientResource("test", "user", UUID.randomUUID());
		c = new PolicyContext(admin, y);
		assertFalse(p.evaluate(c, one));
		assertFalse(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		y = new TransientResource("test", "user", UUID.randomUUID());
		c = new PolicyContext(p1, y);
		assertFalse(p.evaluate(c, one));
		assertFalse(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		y = new TransientResource("test", "user", UUID.randomUUID());
		c = new PolicyContext(admin, y);
		assertFalse(p.evaluate(c, one));
		assertFalse(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		y = new TransientResource("test", "user", UUID.randomUUID());
		c = new PolicyContext(p1, y);
		assertFalse(p.evaluate(c, one));
		assertFalse(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		y = new TransientResource("test", "user", UUID.randomUUID());
		c = new PolicyContext(admin, y);
		assertTrue(p.evaluate(c, one));
		assertTrue(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		Resource z = new TransientResource("test", "foo", UUID.randomUUID());
		c = new PolicyContext(p1, z);
		assertTrue(p.evaluate(c, one));
		assertTrue(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertTrue(p.evaluate(c, four));

		z = new TransientResource("test", "foo", UUID.randomUUID());
		c = new PolicyContext(p1, z);
		assertFalse(p.evaluate(c, one));
		assertFalse(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertFalse(p.evaluate(c, four));

		// This principal has a cross-organization policy statement.
		z = new TransientResource("test", "foo", UUID.randomUUID());
		c = new PolicyContext(admin, z);
		assertTrue(p.evaluate(c, one));
		assertTrue(p.evaluate(c, two));
		assertFalse(p.evaluate(c, three));
		assertTrue(p.evaluate(c, four));
	}
}
