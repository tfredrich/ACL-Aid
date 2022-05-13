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
	private static final String DOCUMENTS_NAMESPACE = "docs";
	private static final String GROUPS_NAMESPACE = "groups";
	private static final String USERS_NAMESPACE = "users";

	private static final String DOCUMENTS_PREFIX = DOCUMENTS_NAMESPACE + ":";
	private static final String GROUPS_PREFIX = GROUPS_NAMESPACE + ":";
	private static final String USERS_PREFIX = USERS_NAMESPACE + ":";

	private static final String ADMINS_GROUP = GROUPS_PREFIX + "admins";
	private static final String EVERYONE_GROUP = GROUPS_PREFIX + "everyone";

	private static final String ALL_DOCS = DOCUMENTS_PREFIX + "document/*";
	private static final String DOC_1234 = DOCUMENTS_PREFIX + "document/1234";

	private static final String ADMINISTRATOR_RELATION = "administers";
	private static final String EDITOR_RELATION = "editor";
	private static final String MEMBER_RELATION = "member";
	private static final String OWNER_RELATION = "owner";
	private static final String VIEWER_RELATION = "viewer";

	private static final String EVERY_USER = USERS_PREFIX + "user/*";
	private static final String ADMINISTRATORS_USERSET = ADMINS_GROUP + "#" + MEMBER_RELATION;
	private static final String EVERYONE_USERSET = EVERYONE_GROUP + "#" + MEMBER_RELATION;
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
			.namespace(DOCUMENTS_NAMESPACE)
				.relation(OWNER_RELATION)
				.relation(EDITOR_RELATION)
				.relation(VIEWER_RELATION)
				.relation(ADMINISTRATOR_RELATION)
					.union()
						._this()
						.computedUserset(OWNER_RELATION)

				// Directly-specified tuples
				.tuple(DOC_1234, OWNER_RELATION, TODD)
				.tuple(ALL_DOCS, ADMINISTRATOR_RELATION, ADMINISTRATORS_USERSET)
				.tuple(ALL_DOCS, VIEWER_RELATION, EVERYONE_USERSET)

			.namespace(GROUPS_NAMESPACE)
				.relation(MEMBER_RELATION)
				.tuple(EVERYONE_GROUP, MEMBER_RELATION, EVERY_USER)

				// DSL-built multiple tuples
				.tuples()
					.forResource(ADMINS_GROUP)
						.withRelation(MEMBER_RELATION)
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
		assertTrue(acl.check(TODD, MEMBER_RELATION, EVERYONE_GROUP));
		assertTrue(acl.check(JASMINE, MEMBER_RELATION, EVERYONE_GROUP));
		assertTrue(acl.check(BETTY, MEMBER_RELATION, EVERYONE_GROUP));
		assertTrue(acl.check(BOB, MEMBER_RELATION, EVERYONE_GROUP));
		assertTrue(acl.check(SALLY, MEMBER_RELATION, EVERYONE_GROUP));
		assertTrue(acl.check(SAM, MEMBER_RELATION, EVERYONE_GROUP));
	}

	@Test
	public void testAdminSpecifiedGroup()
	throws ParseException
	{
		assertFalse(acl.check(TODD, MEMBER_RELATION, ADMINS_GROUP));
		assertFalse(acl.check(JASMINE, MEMBER_RELATION, ADMINS_GROUP));
		assertTrue(acl.check(BETTY, MEMBER_RELATION, ADMINS_GROUP));
		assertTrue(acl.check(BOB, MEMBER_RELATION, ADMINS_GROUP));
		assertTrue(acl.check(SALLY, MEMBER_RELATION, ADMINS_GROUP));
		assertTrue(acl.check(SAM, MEMBER_RELATION, ADMINS_GROUP));
		assertFalse(acl.check(BOB, ADMINISTRATOR_RELATION, "docs:video/12345"));
		assertFalse(acl.check(SALLY, ADMINISTRATOR_RELATION, "docs:video/12345"));
		assertFalse(acl.check(JASMINE, ADMINISTRATOR_RELATION, DOC_1234));
	}

	@Test
	public void testInheritance()
	throws ParseException
	{
		assertTrue(acl.check(TODD, OWNER_RELATION, DOC_1234));
		assertTrue(acl.check(TODD, VIEWER_RELATION, DOC_1234));
		assertTrue(acl.check(TODD, ADMINISTRATOR_RELATION, DOC_1234));
		assertTrue(acl.check(JASMINE, VIEWER_RELATION, DOC_1234));
		assertFalse(acl.check(JASMINE, OWNER_RELATION, DOC_1234));
	}
}
