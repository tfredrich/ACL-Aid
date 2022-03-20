package com.strategicgains.aclaid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import com.strategicgains.aclaid.builder.AccessControlListBuilder;
import com.strategicgains.aclaid.exception.InvalidTupleException;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class AccessControlListTest
{
	private static final String DOCUMENTS = "docs";
	private static final String GROUPS = "groups";
	private static final String USERS = "users";

	private static final String DOCUMENTS_PREFIX = DOCUMENTS + ":";
	private static final String GROUPS_PREFIX = GROUPS + ":";
	private static final String USERS_PREFIX = USERS + ":";

	private static final String ADMINS_GROUP = GROUPS_PREFIX + "admins";
	private static final String EVERYONE_GROUP = GROUPS_PREFIX + "everyone";

	private static final String ALL_DOCS = DOCUMENTS_PREFIX + "document/*";
	private static final String DOC_1234 = DOCUMENTS_PREFIX + "document/1234";

	private static final String ADMINISTRATOR = "administers";
	private static final String EDITOR = "editor";
	private static final String MEMBER = "member";
	private static final String OWNER = "owner";
	private static final String VIEWER = "viewer";

	private static final String EVERY_USER = USERS_PREFIX + "user/*";
	private static final String ADMINISTRATORS = ADMINS_GROUP + "#" + MEMBER;
	private static final String EVERYONE = EVERYONE_GROUP + "#" + MEMBER;
	private static final String BETTY = USERS_PREFIX + "user/betty";
	private static final String BOB = USERS_PREFIX + "user/bob";
	private static final String SALLY = USERS_PREFIX + "user/sally";
	private static final String SAM = USERS_PREFIX + "user/sam";
	private static final String TODD = USERS_PREFIX + "user/todd";
	private static final String JASMINE = USERS_PREFIX + "user/jasmine";


	private AccessControlList acl;

	@Before
	public void initialize()
	throws ParseException, InvalidTupleException, RelationNotRegisteredException
	{
		AccessControlListBuilder builder = new AccessControlListBuilder();
		builder
			.namespace(DOCUMENTS)
				.relation(OWNER)
				.relation(ADMINISTRATOR)
				.relation(EDITOR)
					.union()
						._this()
						.computedUserset(OWNER)
				.relation(VIEWER)
					.union()
						._this()
						.computedUserset(EDITOR)

				// Directly-specified tuples
				.tuple(DOC_1234, OWNER, TODD)
				.tuple(ALL_DOCS, ADMINISTRATOR, ADMINISTRATORS)
				.tuple(ALL_DOCS, VIEWER, EVERYONE)

			.namespace(GROUPS)
				.relation(MEMBER)
				.tuple(EVERYONE_GROUP, MEMBER, EVERY_USER)

				// DSL-built multiple tuples
				.tuple()
					.forResource(ADMINS_GROUP)
						.withRelation(MEMBER)
							.withUserset(SAM)
							.withUserset(BOB)
							.withUserset(SALLY)
							.withUserset(BETTY)
			;

		this.acl = builder.build();
	}

	@Test
	public void testEveryoneWildcardGroup()
	throws ParseException
	{
		assertTrue(acl.check(TODD, MEMBER, EVERYONE_GROUP));
		assertTrue(acl.check(JASMINE, MEMBER, EVERYONE_GROUP));
		assertTrue(acl.check(BETTY, MEMBER, EVERYONE_GROUP));
		assertTrue(acl.check(BOB, MEMBER, EVERYONE_GROUP));
		assertTrue(acl.check(SALLY, MEMBER, EVERYONE_GROUP));
		assertTrue(acl.check(SAM, MEMBER, EVERYONE_GROUP));
	}

	@Test
	public void testAdminSpecifiedGroup()
	throws ParseException
	{
		assertFalse(acl.check(TODD, MEMBER, ADMINS_GROUP));
		assertFalse(acl.check(JASMINE, MEMBER, ADMINS_GROUP));
		assertTrue(acl.check(BETTY, MEMBER, ADMINS_GROUP));
		assertTrue(acl.check(BOB, MEMBER, ADMINS_GROUP));
		assertTrue(acl.check(SALLY, MEMBER, ADMINS_GROUP));
		assertTrue(acl.check(SAM, MEMBER, ADMINS_GROUP));
		assertFalse(acl.check(BOB, ADMINISTRATOR, "docs:video/12345"));
		assertFalse(acl.check(SALLY, ADMINISTRATOR, "docs:video/12345"));
		assertFalse(acl.check(JASMINE, ADMINISTRATOR, DOC_1234));
	}

	@Test
	public void testInheritance()
	throws ParseException
	{
		assertTrue(acl.check(TODD, OWNER, DOC_1234));
		assertTrue(acl.check(TODD, VIEWER, DOC_1234));
//		assertTrue(acl.check(TODD, ADMINISTRATOR, DOC_1234));
		assertTrue(acl.check(JASMINE, VIEWER, DOC_1234));
		assertFalse(acl.check(JASMINE, OWNER, DOC_1234));
	}
}
