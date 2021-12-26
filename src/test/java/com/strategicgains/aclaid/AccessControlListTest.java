package com.strategicgains.aclaid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

import com.strategicgains.aclaid.builder.GrantBuilder;
import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidGrantException;

public class AccessControlListTest
{
	private static final String ADMINS_GROUP = "groups:admins";
	private static final String EVERYONE_GROUP = "groups:everyone";

	private static final String ALL_DOCS = "docs:*";
	private static final String DOC_1234 = "docs:1234";

	private static final String MEMBER_RELATION = "member";
	private static final String VIEWER_RELATION = "viewer";
	private static final String ADMIN_RELATION = "administers";
	private static final String OWNER_RELATION = "owner";

	private static final String ALL_USERS = "user:*";
	private static final String ADMINISTRATORS = ADMINS_GROUP + "#" + MEMBER_RELATION;
	private static final String BETTY = "user:betty";
	private static final String BOB = "user:bob";
	private static final String SALLY = "user:sally";
	private static final String SAM = "user:sam";
	private static final String TODD = "user:todd";
	private static final String JASMINE = "user:jasmine";

	@Test
	public void test()
	throws ParseException, InvalidGrantException
	{
		GrantBuilder gb = new GrantBuilder();

		gb.namespace("doc")
			.relation("owner")
			.relation("editor")
				.union()
					._this()
					.computedUserset("owner")
			.relation("viewer")
				.union()
					._this()
					.computedUserset("editor");

		gb.forResource(DOC_1234)
			.withUserset(TODD)
				.withRelation(OWNER_RELATION);

		gb.forResource(ALL_DOCS)
			.withRelation(ADMIN_RELATION)
				.withUserset(ALL_USERS + "#" + OWNER_RELATION);

		gb.forResource(ALL_DOCS)
			.withRelation(ADMIN_RELATION)
				.withUserset(ADMINISTRATORS);

		gb.forResource(ALL_DOCS)
			.withRelation(VIEWER_RELATION)
				.withUserset(EVERYONE_GROUP + "#" + MEMBER_RELATION);

		gb.forResource(ADMINS_GROUP)
			.withRelation(MEMBER_RELATION)
				.withUserset(SAM)
				.withUserset(BOB)
				.withUserset(SALLY)
				.withUserset(BETTY);

		gb.forResource(EVERYONE_GROUP)
			.withRelation(MEMBER_RELATION)
				.withUserset(ALL_USERS);

		AccessControlList acl = gb.build();
		assertNotNull(acl);
		assertTrue(acl.check(TODD, MEMBER_RELATION, EVERYONE_GROUP));
		assertTrue(acl.check(JASMINE, MEMBER_RELATION, EVERYONE_GROUP));
		assertTrue(acl.check(BETTY, MEMBER_RELATION, EVERYONE_GROUP));
		assertTrue(acl.check(BOB, MEMBER_RELATION, EVERYONE_GROUP));
		assertTrue(acl.check(SALLY, MEMBER_RELATION, EVERYONE_GROUP));
		assertTrue(acl.check(SAM, MEMBER_RELATION, EVERYONE_GROUP));
	
		assertFalse(acl.check(TODD, MEMBER_RELATION, ADMINS_GROUP));
		assertFalse(acl.check(JASMINE, MEMBER_RELATION, ADMINS_GROUP));
		assertTrue(acl.check(BETTY, MEMBER_RELATION, ADMINS_GROUP));
		assertTrue(acl.check(BOB, MEMBER_RELATION, ADMINS_GROUP));
		assertTrue(acl.check(SALLY, MEMBER_RELATION, ADMINS_GROUP));
		assertTrue(acl.check(SAM, MEMBER_RELATION, ADMINS_GROUP));

		assertTrue(acl.check(TODD, OWNER_RELATION, DOC_1234));
		assertTrue(acl.check(TODD, VIEWER_RELATION, DOC_1234));
		assertTrue(acl.check(JASMINE, VIEWER_RELATION, DOC_1234));
		assertFalse(acl.check(JASMINE, OWNER_RELATION, DOC_1234));
		assertTrue(acl.check(BOB, ADMIN_RELATION, "video:12345"));
		assertTrue(acl.check(SALLY, ADMIN_RELATION, "video:12345"));
		assertTrue(acl.check(TODD, ADMIN_RELATION, DOC_1234));
		assertFalse(acl.check(JASMINE, ADMIN_RELATION, DOC_1234));
	}
}
