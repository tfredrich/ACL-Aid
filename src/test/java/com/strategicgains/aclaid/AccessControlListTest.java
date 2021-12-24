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

		AccessControlList gm = gb.build();
		assertNotNull(gm);
		assertTrue(gm.isAllowed(UserSet.parse(TODD), MEMBER_RELATION, Resource.parse(EVERYONE_GROUP)));
		assertTrue(gm.isAllowed(UserSet.parse(JASMINE), MEMBER_RELATION, Resource.parse(EVERYONE_GROUP)));
		assertTrue(gm.isAllowed(UserSet.parse(BETTY), MEMBER_RELATION, Resource.parse(EVERYONE_GROUP)));
		assertTrue(gm.isAllowed(UserSet.parse(BOB), MEMBER_RELATION, Resource.parse(EVERYONE_GROUP)));
		assertTrue(gm.isAllowed(UserSet.parse(SALLY), MEMBER_RELATION, Resource.parse(EVERYONE_GROUP)));
		assertTrue(gm.isAllowed(UserSet.parse(SAM), MEMBER_RELATION, Resource.parse(EVERYONE_GROUP)));
	
		assertFalse(gm.isAllowed(UserSet.parse(TODD), MEMBER_RELATION, Resource.parse(ADMINS_GROUP)));
		assertFalse(gm.isAllowed(UserSet.parse(JASMINE), MEMBER_RELATION, Resource.parse(ADMINS_GROUP)));
		assertTrue(gm.isAllowed(UserSet.parse(BETTY), MEMBER_RELATION, Resource.parse(ADMINS_GROUP)));
		assertTrue(gm.isAllowed(UserSet.parse(BOB), MEMBER_RELATION, Resource.parse(ADMINS_GROUP)));
		assertTrue(gm.isAllowed(UserSet.parse(SALLY), MEMBER_RELATION, Resource.parse(ADMINS_GROUP)));
		assertTrue(gm.isAllowed(UserSet.parse(SAM), MEMBER_RELATION, Resource.parse(ADMINS_GROUP)));

		assertTrue(gm.isAllowed(UserSet.parse(TODD), OWNER_RELATION, Resource.parse(DOC_1234)));
		assertTrue(gm.isAllowed(UserSet.parse(TODD), VIEWER_RELATION, Resource.parse(DOC_1234)));
		assertTrue(gm.isAllowed(UserSet.parse(JASMINE), VIEWER_RELATION, Resource.parse(DOC_1234)));
		assertFalse(gm.isAllowed(UserSet.parse(JASMINE), OWNER_RELATION, Resource.parse(DOC_1234)));
		assertTrue(gm.isAllowed(UserSet.parse(BOB), ADMIN_RELATION, Resource.parse("qrn:docs:::video/12345")));
		assertTrue(gm.isAllowed(UserSet.parse(SALLY), ADMIN_RELATION, Resource.parse("qrn:docs:::video/12345")));
		assertTrue(gm.isAllowed(UserSet.parse(TODD), ADMIN_RELATION, Resource.parse(DOC_1234)));
		assertFalse(gm.isAllowed(UserSet.parse(JASMINE), ADMIN_RELATION, Resource.parse(DOC_1234)));
	}
}
