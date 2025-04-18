package com.strategicgains.aclaid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import com.strategicgains.aclaid.builder.AccessControlBuilder;
import com.strategicgains.aclaid.exception.InvalidTupleException;

public class AccessControlTest
{
	private static final String NAMESPACE = "T:";
	private static final String DOCUMENT_OBJECT = "doc";
	private static final String FOLDER_OBJECT = "folder";
	private static final String GROUP_OBJECT = "group";
	private static final String USER_OBJECT = "user";

	private static final String ADMINS_GROUP = NAMESPACE + GROUP_OBJECT + "/admins";
	private static final String EVERYONE_GROUP = NAMESPACE + GROUP_OBJECT + "/everyone";

	// This is for testing wildcards in tuple object IDs, which aren't allowed.
	private static final String ALL_DOCS = NAMESPACE + DOCUMENT_OBJECT + "/*";
	private static final String DOC_1234 = NAMESPACE + DOCUMENT_OBJECT + "/1234";
	private static final String DOC_5678 = NAMESPACE + DOCUMENT_OBJECT + "/5678";
	private static final String FOLDER_ENGINEERING = NAMESPACE + FOLDER_OBJECT + "/engineering";
    private static final String FOLDER_PLANNING = NAMESPACE + FOLDER_OBJECT + "/planning";
    private static final String FOLDER_DOCUMENTS = NAMESPACE + FOLDER_OBJECT + "/documents";

	// However, wildcards are allowed in tuple user IDs.
	private static final String EVERY_USER = NAMESPACE + USER_OBJECT + "/*";

	private static final String ADMINISTRATOR_RELATION = "administers";
	private static final String EDITOR_RELATION = "editor";
	private static final String MEMBER_RELATION = "member";
	private static final String OWNER_RELATION = "owner";
	private static final String PARENT_RELATION = "parent";
	private static final String VIEWER_RELATION = "viewer";

	private static final String ADMINISTRATORS_USERSET = ADMINS_GROUP + "#" + MEMBER_RELATION;
	private static final String EVERYONE_USERSET = EVERYONE_GROUP + "#" + MEMBER_RELATION;
	private static final String BETTY = NAMESPACE + USER_OBJECT + "/betty";
	private static final String BOB = NAMESPACE + USER_OBJECT + "/bob";
	private static final String SALLY = NAMESPACE + USER_OBJECT + "/sally";
	private static final String SAM = NAMESPACE + USER_OBJECT + "/sam";
	private static final String TODD = NAMESPACE + USER_OBJECT + "/todd";
	private static final String JASMINE = NAMESPACE + USER_OBJECT + "/jasmine";

	private AccessControl acl;

	@Before
	public void initialize()
	throws ParseException, InvalidTupleException
	{
		AccessControlBuilder builder = new AccessControlBuilder();
		builder
			.object(FOLDER_OBJECT)
				.relation(PARENT_RELATION)
				.relation(OWNER_RELATION)
				.relation(EDITOR_RELATION)
					.childOf(OWNER_RELATION)
				.relation(VIEWER_RELATION)
					.childOf(EDITOR_RELATION)
				.relation(ADMINISTRATOR_RELATION)
					.childOf(OWNER_RELATION)

			.object(DOCUMENT_OBJECT)
				.relation(PARENT_RELATION)
				.relation(OWNER_RELATION)
				.relation(EDITOR_RELATION)
					.childOf(OWNER_RELATION)
					// or "editor" of "parent" of "document"
				.relation(VIEWER_RELATION)
					.childOf(EDITOR_RELATION)
					// or "viewer" of "parent" of "document"
				.relation(ADMINISTRATOR_RELATION)
					.childOf(OWNER_RELATION)

				// Directly-specified tuples
				.tuple(TODD, OWNER_RELATION, DOC_1234)

			.object(GROUP_OBJECT)
				.relation(MEMBER_RELATION)

				// DSL-built multiple tuples
				.tuples()
					.forResource(ADMINS_GROUP)
						.withRelation(MEMBER_RELATION)
							.withUserset(SAM)
							.withUserset(BOB)
							.withUserset(SALLY)
							.withUserset(BETTY)

				// Single tuples
				.tuple(FOLDER_DOCUMENTS, PARENT_RELATION, FOLDER_ENGINEERING)	// /documents/engineering
				.tuple(FOLDER_ENGINEERING, PARENT_RELATION, FOLDER_PLANNING)	// /documents/engineering/planning
				.tuple(FOLDER_PLANNING, PARENT_RELATION, DOC_5678)				// /documents/engineering/planning/5678)
				.tuple(ADMINISTRATORS_USERSET, ADMINISTRATOR_RELATION, DOC_1234)
				.tuple(ADMINISTRATORS_USERSET, ADMINISTRATOR_RELATION, FOLDER_DOCUMENTS)
				.tuple(TODD, OWNER_RELATION, FOLDER_DOCUMENTS)
				.tuple(JASMINE, VIEWER_RELATION, FOLDER_DOCUMENTS)

				// Every user is a member of the everyone group.
				.tuple(EVERY_USER, MEMBER_RELATION, EVERYONE_GROUP)

				// Make the document available to view by the everyone group.
				.tuple(EVERYONE_USERSET, VIEWER_RELATION, DOC_1234)
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
		assertTrue(acl.check(BOB, ADMINISTRATOR_RELATION, DOC_1234));
		assertTrue(acl.check(SALLY, ADMINISTRATOR_RELATION, DOC_1234));
		assertFalse(acl.check(BOB, ADMINISTRATOR_RELATION, NAMESPACE + "video/12345"));
		assertFalse(acl.check(SALLY, ADMINISTRATOR_RELATION, NAMESPACE + "video/12345"));
		assertFalse(acl.check(JASMINE, ADMINISTRATOR_RELATION, DOC_1234));
	}

	@Test
	public void testRelationInheritance()
	throws ParseException
	{
		assertTrue(acl.check(TODD, OWNER_RELATION, DOC_1234));
		assertTrue(acl.check(TODD, VIEWER_RELATION, DOC_1234));
		assertTrue(acl.check(TODD, ADMINISTRATOR_RELATION, DOC_1234));
		assertFalse(acl.check(JASMINE, OWNER_RELATION, DOC_1234));
		assertTrue(acl.check(JASMINE, VIEWER_RELATION, DOC_1234));
	}

	@Test
	public void testFolderInheritance()
	throws ParseException
	{
		assertTrue(acl.check(TODD, OWNER_RELATION, DOC_5678));
		assertTrue(acl.check(TODD, VIEWER_RELATION, DOC_5678));
		assertTrue(acl.check(TODD, ADMINISTRATOR_RELATION, DOC_5678));
		assertFalse(acl.check(JASMINE, OWNER_RELATION, DOC_5678));
		assertTrue(acl.check(JASMINE, VIEWER_RELATION, DOC_5678));
		assertTrue(acl.check(SALLY, ADMINISTRATOR_RELATION, DOC_5678));
	}

	@Test(expected = InvalidTupleException.class)
	public void testInvalidResourceWithWildcard()
	throws ParseException, InvalidTupleException
	{
		// ALL_DOCS contains a wildcard which is not allowed in tuple resources.
		acl.addTuple(EVERYONE_USERSET, ADMINISTRATOR_RELATION, ALL_DOCS);
	}
}
