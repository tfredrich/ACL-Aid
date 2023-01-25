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

	private static final String ADMINS_GROUP = GROUPS_NAMESPACE + ":admins";
	private static final String EVERYONE_GROUP = GROUPS_NAMESPACE + ":everyone";

	private static final String ALL_DOCS = DOCUMENTS_NAMESPACE + ":document/*";
	private static final String DOC_1234 = DOCUMENTS_NAMESPACE + ":document/1234";

	private static final String ADMINISTRATOR_RELATION = "administers";
	private static final String EDITOR_RELATION = "editor";
	private static final String MEMBER_RELATION = "member";
	private static final String OWNER_RELATION = "owner";
	private static final String VIEWER_RELATION = "viewer";

	private static final String EVERY_USER = USERS_NAMESPACE + ":user/*";
	private static final String ADMINISTRATORS_USERSET = ADMINS_GROUP + "#" + MEMBER_RELATION;
	private static final String EVERYONE_USERSET = EVERYONE_GROUP + "#" + MEMBER_RELATION;
	private static final String BETTY = USERS_NAMESPACE + ":user/betty";
	private static final String BOB = USERS_NAMESPACE + ":user/bob";
	private static final String SALLY = USERS_NAMESPACE + ":user/sally";
	private static final String SAM = USERS_NAMESPACE + ":user/sam";
	private static final String TODD = USERS_NAMESPACE + ":user/todd";
	private static final String JASMINE = USERS_NAMESPACE + ":user/jasmine";

	private AccessControl acl;

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
					.usersetRewrite()
						.union()
							._this()
							.computedUserset(OWNER_RELATION)

				// Directly-specified tuples
				.tuple(TODD, OWNER_RELATION, DOC_1234)
				.tuple(ADMINISTRATORS_USERSET, ADMINISTRATOR_RELATION, ALL_DOCS)
				.tuple(EVERYONE_USERSET, VIEWER_RELATION, ALL_DOCS)

			.namespace(GROUPS_NAMESPACE)
				.relation(MEMBER_RELATION)
				.tuple(EVERY_USER, MEMBER_RELATION, EVERYONE_GROUP)

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
