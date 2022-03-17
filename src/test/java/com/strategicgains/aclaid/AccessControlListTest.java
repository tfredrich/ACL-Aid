package com.strategicgains.aclaid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

import com.strategicgains.aclaid.builder.AccessControlListBuilder;
import com.strategicgains.aclaid.exception.InvalidTupleException;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

public class AccessControlListTest
{
	private static final String DOCUMENT = "doc";
	private static final String GROUP = "group";
	private static final String USER = "user";

	private static final String ADMINS_GROUP = GROUP + ":admins";
	private static final String EVERYONE_GROUP = GROUP + ":everyone";

	private static final String ALL_DOCS = DOCUMENT + ":*";
	private static final String DOC_1234 = DOCUMENT + ":1234";

	private static final String ADMINISTRATOR = "administers";
	private static final String EDITOR = "editor";
	private static final String MEMBER = "member";
	private static final String OWNER = "owner";
	private static final String VIEWER = "viewer";

	private static final String EVERY_USER = USER + ":*";
	private static final String ADMINISTRATORS = ADMINS_GROUP + "#" + MEMBER;
	private static final String EVERYONE = EVERYONE_GROUP + "#" + MEMBER;
	private static final String BETTY = USER + ":betty";
	private static final String BOB = USER + ":bob";
	private static final String SALLY = USER + ":sally";
	private static final String SAM = USER + ":sam";
	private static final String TODD = USER + ":todd";
	private static final String JASMINE = USER + ":jasmine";

	@Test
	public void test()
	throws ParseException, RelationNotRegisteredException, InvalidTupleException
	{
		AccessControlListBuilder builder = new AccessControlListBuilder();
		builder
			.namespace(DOCUMENT)
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

			.namespace(GROUP)
				.relation(MEMBER)
				.tuple(EVERYONE_GROUP, MEMBER, EVERY_USER)

				// DSL-built multiple tuples
				.forResource(ADMINS_GROUP)
					.withRelation(MEMBER)
						.withUserset(SAM)
						.withUserset(BOB)
						.withUserset(SALLY)
						.withUserset(BETTY)
			;

		AccessControlList acl = builder.build();
		assertNotNull(acl);
		assertTrue(acl.check(TODD, MEMBER, EVERYONE_GROUP));
		assertTrue(acl.check(JASMINE, MEMBER, EVERYONE_GROUP));
		assertTrue(acl.check(BETTY, MEMBER, EVERYONE_GROUP));
		assertTrue(acl.check(BOB, MEMBER, EVERYONE_GROUP));
		assertTrue(acl.check(SALLY, MEMBER, EVERYONE_GROUP));
		assertTrue(acl.check(SAM, MEMBER, EVERYONE_GROUP));
	
		assertFalse(acl.check(TODD, MEMBER, ADMINS_GROUP));
		assertFalse(acl.check(JASMINE, MEMBER, ADMINS_GROUP));
		assertTrue(acl.check(BETTY, MEMBER, ADMINS_GROUP));
		assertTrue(acl.check(BOB, MEMBER, ADMINS_GROUP));
		assertTrue(acl.check(SALLY, MEMBER, ADMINS_GROUP));
		assertTrue(acl.check(SAM, MEMBER, ADMINS_GROUP));

		assertTrue(acl.check(TODD, OWNER, DOC_1234));
		assertTrue(acl.check(TODD, VIEWER, DOC_1234));
//		assertTrue(acl.check(TODD, ADMINISTRATOR, DOC_1234));
		assertTrue(acl.check(JASMINE, VIEWER, DOC_1234));
		assertFalse(acl.check(JASMINE, OWNER, DOC_1234));
		assertFalse(acl.check(BOB, ADMINISTRATOR, "video:12345"));
		assertFalse(acl.check(SALLY, ADMINISTRATOR, "video:12345"));
		assertFalse(acl.check(JASMINE, ADMINISTRATOR, DOC_1234));
		assertTrue(acl.check(TODD, ADMINISTRATOR, DOC_1234));
	}
}
