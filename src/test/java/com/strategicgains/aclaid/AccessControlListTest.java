package com.strategicgains.aclaid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

import com.strategicgains.aclaid.builder.AclBuilder;
import com.strategicgains.aclaid.exception.InvalidTupleException;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class AccessControlListTest
{
	private static final String NAMESPACE = "documents";

	private static final String ADMINS_GROUP = "groups:admins";
	private static final String EVERYONE_GROUP = "groups:everyone";

	private static final String ALL_DOCS = "docs:*";
	private static final String DOC_1234 = "docs:1234";

	private static final String ADMIN_RELATION = "administers";
	private static final String EDITOR_RELATION = "editor";
	private static final String MEMBER_RELATION = "member";
	private static final String OWNER_RELATION = "owner";
	private static final String VIEWER_RELATION = "viewer";

	private static final String ALL_USERS = "user:*";
	private static final String ADMINISTRATORS = ADMINS_GROUP + "#" + MEMBER_RELATION;
	private static final String EVERYONE = EVERYONE_GROUP + "#" + MEMBER_RELATION;
	private static final String BETTY = "user:betty";
	private static final String BOB = "user:bob";
	private static final String SALLY = "user:sally";
	private static final String SAM = "user:sam";
	private static final String TODD = "user:todd";
	private static final String JASMINE = "user:jasmine";

	@Test
	public void test()
	throws ParseException, RelationNotRegisteredException, InvalidTupleException
	{
		AclBuilder builder = new AclBuilder(NAMESPACE);

		builder
			.relation(OWNER_RELATION)
			.relation(MEMBER_RELATION)
			.relation(ADMIN_RELATION)
			.relation(EDITOR_RELATION)
				.union()
					._this()
					.computedUserset(OWNER_RELATION)
			.relation(VIEWER_RELATION)
				.union()
					._this()
					.computedUserset(EDITOR_RELATION);

		builder
			.tuple(TODD, OWNER_RELATION, DOC_1234)
			.tuple(ADMINISTRATORS, ADMIN_RELATION, ALL_DOCS)
			.tuple(EVERYONE, VIEWER_RELATION, ALL_DOCS)
			.tuple(ALL_USERS, MEMBER_RELATION, EVERYONE_GROUP)
	
			.forResource(ADMINS_GROUP)
				.withRelation(MEMBER_RELATION)
					.withUserset(SAM)
					.withUserset(BOB)
					.withUserset(SALLY)
					.withUserset(BETTY);

		AccessControlList acl = builder.build();
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
		assertFalse(acl.check(BOB, ADMIN_RELATION, "video:12345"));
		assertFalse(acl.check(SALLY, ADMIN_RELATION, "video:12345"));
		assertFalse(acl.check(JASMINE, ADMIN_RELATION, DOC_1234));
		assertTrue(acl.check(TODD, ADMIN_RELATION, DOC_1234));
	}
}
