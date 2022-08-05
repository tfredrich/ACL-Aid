package com.strategicgains.aclaid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

import com.strategicgains.aclaid.builder.AccessControlListBuilder;
import com.strategicgains.aclaid.builder.UserSets;
import com.strategicgains.aclaid.exception.RelationNotRegisteredException;

/**
 * A unit test that checks ACL-Aid against the Zanzibar-based
 * examples and concepts explained at: https://zanzibar.academy/
 * 
 * @author tfredrich
 * @see https://zanzibar.academy/
 */
public class ZanzibarAcademyTest
{
	private static final String DOCUMENT_NAMESPACE = "doc";
	private static final String ORGANIZATION_NAMESPACE = "org";
	private static final String FOLDER_NAMESPACE = "folder";

	// Relations
	private static final String EDITOR_RELATION = "editor";
	private static final String MEMBER_RELATION = "member";
	private static final String OWNER_RELATION = "owner";
	private static final String PARENT_RELATION = "parent";
	private static final String VIEWER_RELATION = "viewer";

	// Groups
	private static final String CONTOSO = ORGANIZATION_NAMESPACE + ":contoso";

	// Users
	private static final String KIM = DOCUMENT_NAMESPACE + ":user/kim";
	private static final String BEN = DOCUMENT_NAMESPACE + ":user/ben";
	private static final String CARL = DOCUMENT_NAMESPACE + ":user/carl";
	private static final String DANA = DOCUMENT_NAMESPACE + ":user/dana";

	// Resources
	private static final String DOC_ROADMAP = DOCUMENT_NAMESPACE + ":document/roadmap";
	private static final String DOC_README = DOCUMENT_NAMESPACE + ":document/readme";
	private static final String DOC_SLIDES = DOCUMENT_NAMESPACE + ":document/slides";
	private static final String FOLDER_PLANNING = FOLDER_NAMESPACE + ":folder/planning";
	private static final String FOLDER_ENGINEERING = FOLDER_NAMESPACE + ":folder/engineering";

	/**
	 * name: "doc"
	 * relation { name: "editor" }
	 * relation { name: "viewer" }
	 * relation { name: "owner" }
	 * 
	 * TUPLES:
	 * Kim is owner of the doc:roadmap
	 * Ben is editor of the doc:roadmap
	 * Carl is viewer of the doc:slides
	 * 
	 * Then add:
	 * Kim is editor of the doc:roadmap
	 */
	@Test
	public void test01Basics()
	throws ParseException, RelationNotRegisteredException
	{
		AccessControlListBuilder builder = new AccessControlListBuilder();
		builder
			.namespace(DOCUMENT_NAMESPACE)
				.relation(OWNER_RELATION)
				.relation(EDITOR_RELATION)
				.relation(VIEWER_RELATION)

			.tuple(KIM, OWNER_RELATION, DOC_ROADMAP)
			.tuple(BEN, EDITOR_RELATION, DOC_ROADMAP)
			.tuple(CARL, VIEWER_RELATION, DOC_SLIDES);

		AccessControlList acl = builder.build();

		assertTrue(acl.check(CARL, VIEWER_RELATION, DOC_SLIDES));
		assertTrue(acl.check(KIM, OWNER_RELATION, DOC_ROADMAP));
		assertTrue(acl.check(BEN, EDITOR_RELATION, DOC_ROADMAP));

		assertFalse(acl.check(KIM, EDITOR_RELATION, DOC_ROADMAP));
		// Add Kim as editor of the doc:roadmap
		acl.addTuple(KIM, EDITOR_RELATION, DOC_ROADMAP);
		assertTrue(acl.check(KIM, EDITOR_RELATION, DOC_ROADMAP));
	}

	/**
	 * name: "doc"
	 * relation { name: "owner" }
	 * relation {
	 *   name: "editor"
	 *   userset_rewrite {
	 *     union {
	 *       child { _this {} }
	 *       child { computed_userset { relation: "owner" } }
	 * }}}
	 * relation {
	 *   name: "viewer"
	 *   userset_rewrite {
	 *     union {
	 *       child { _this {} }
	 *       child { computed_userset { relation: "editor" } }
	 * }}}
	 * 
	 * TUPLES:
	 * Kim is owner of the doc:roadmap
	 * Ben is editor of the doc:roadmap
	 * Carl is viewer of the doc:slides
	 */
	@Test
	public void test02EditorsViewer()
	throws ParseException, RelationNotRegisteredException
	{
		AccessControlListBuilder builder = new AccessControlListBuilder();
		builder
			.namespace(DOCUMENT_NAMESPACE)
				.relation(OWNER_RELATION)
				.relation(EDITOR_RELATION)
					.usersetRewrite()
						.union()
							._this()
							.computedUserset(OWNER_RELATION)
				.relation(VIEWER_RELATION)
					.usersetRewrite()
						.union()
							._this()
							.computedUserset(EDITOR_RELATION)

			.tuple(KIM, OWNER_RELATION, DOC_ROADMAP)
			.tuple(BEN, EDITOR_RELATION, DOC_ROADMAP)
			.tuple(CARL, VIEWER_RELATION, DOC_SLIDES);

		AccessControlList acl = builder.build();

		assertTrue(acl.check(CARL, VIEWER_RELATION, DOC_SLIDES));
		assertTrue(acl.check(KIM, EDITOR_RELATION, DOC_ROADMAP));
		assertTrue(acl.check(KIM, OWNER_RELATION, DOC_ROADMAP));
		assertTrue(acl.check(BEN, EDITOR_RELATION, DOC_ROADMAP));
	}

	/**
	 * ### Organization
	 * name: "org"
	 * relation { name: "member" }
	 * 
	 * ### Document
	 * name: "doc"
	 * relation { name: "owner" }
	 * relation {
	 *   name: "editor"
	 *   userset_rewrite {
	 *     union {
	 *       child { _this {} }
	 *       child { computed_userset { relation: "owner" } }
	 * }}}
	 * relation {
	 *   name: "viewer"
	 *   userset_rewrite {
	 *     union {
	 *       child { _this {} }
	 *       child { computed_userset { relation: "editor" } }
	 * }}}
	 * 
	 * TUPLES:
	 * Carl is member of the org:contoso
	 * org:contoso#member is viewer of the doc:slides
	 */
	@Test
	public void test03Groups()
	throws ParseException, RelationNotRegisteredException
	{
		AccessControlListBuilder builder = new AccessControlListBuilder();
		builder
			.namespace(ORGANIZATION_NAMESPACE)
				.relation(MEMBER_RELATION)

			.namespace(DOCUMENT_NAMESPACE)
				.relation(OWNER_RELATION)
				.relation(EDITOR_RELATION)
					.usersetRewrite()
						.union()
							._this()
							.computedUserset(OWNER_RELATION)
				.relation(VIEWER_RELATION)
					.usersetRewrite()
						.union()
							._this()
							.computedUserset(EDITOR_RELATION)

//			.tuple(CARL, EDITOR_RELATION, CONTOSO)
//			.tuple(CONTOSO + "#" + MEMBER_RELATION, VIEWER_RELATION, DOC_SLIDES);
			.tuple(CARL, MEMBER_RELATION, CONTOSO)
			.tuple(CONTOSO + "#" + MEMBER_RELATION, VIEWER_RELATION, DOC_SLIDES);

		AccessControlList acl = builder.build();

		assertTrue(acl.check(CARL, MEMBER_RELATION, CONTOSO));
		assertTrue(acl.check(CARL, VIEWER_RELATION, DOC_SLIDES));
	}

	/**
	 * name: "folder"
	 * relation { name: "parent" }
	 * relation { name: "owner" }
	 * relation {
	 *   name: "editor"
	 *   userset_rewrite {
	 *     union {
	 *       child { _this {} }
	 *       child { computed_userset { relation: "owner" } }
	 * }}}
	 * relation {
	 *   name: "viewer"
	 *   userset_rewrite {
	 *     union {
	 *       child { _this {} }
	 *       child { computed_userset { relation: "editor" } }
	 * }}}
	 * 
	 * name: "doc"
	 * relation { name: "parent" }
	 * relation { name: "owner" }
	 * relation {
	 *   name: "editor"
	 *   userset_rewrite {
	 *     union {
	 *       child { _this {} }
	 *       child { computed_userset { relation: "owner" } }
	 *       child { tuple_to_userset {
	 *         tupleset { relation: "parent" }
	 *         computed_userset {
	 *           object: $TUPLE_USERSET_OBJECT  # parent folder
	 *           relation: "editor" }}}
	 * }}}
	 * relation {
	 *   name: "viewer"
	 *   userset_rewrite {
	 *     union {
	 *       child { _this {} }
	 *       child { computed_userset { relation: "editor" } }
	 *       child { tuple_to_userset {
	 *         tupleset { relation: "parent" }
	 *         computed_userset {
	 *           object: $TUPLE_USERSET_OBJECT  # parent folder
	 *           relation: "viewer" }}}
	 * }}}
	 * 
	 * name: "org"
	 * relation { name: "member" }
	 * 
	 * TUPLES:
	 * folder:planning is parent of the doc:readme
	 * Kim is viewer of the folder:planning
	 * 
	 * Then add:
	 * Dana is member of org:contoso
	 * org:contoso#member editor of folder:engineering
	 * folder:engineering parent of folder:planning
	 */
	@Test
	public void test04Folders()
	throws ParseException, RelationNotRegisteredException
	{
		AccessControlListBuilder builder = new AccessControlListBuilder();
		builder
			.namespace(FOLDER_NAMESPACE)
				.relation(PARENT_RELATION)
				.relation(OWNER_RELATION)
				.relation(EDITOR_RELATION)
					.usersetRewrite()
						.union()
							._this()
							.computedUserset(OWNER_RELATION)
							.tupleToUserSet()
								.tupleSet(PARENT_RELATION)
								.computedUserset(UserSets.TUPLE_USERSET_OBJECT, EDITOR_RELATION)
				.relation(VIEWER_RELATION)
					.usersetRewrite()
						.union()
							._this()
							.computedUserset(EDITOR_RELATION)
		
			.namespace(ORGANIZATION_NAMESPACE)
				.relation(MEMBER_RELATION)
		
			.namespace(DOCUMENT_NAMESPACE)
				.relation(PARENT_RELATION)
				.relation(OWNER_RELATION)
				.relation(EDITOR_RELATION)
				.relation(VIEWER_RELATION)

			.tuple(FOLDER_PLANNING, PARENT_RELATION, DOC_README)
			.tuple(KIM, VIEWER_RELATION, FOLDER_PLANNING);

		AccessControlList acl = builder.build();

		assertTrue(acl.check(FOLDER_PLANNING, PARENT_RELATION, DOC_README));
		assertTrue(acl.check(KIM, VIEWER_RELATION, FOLDER_PLANNING));
		assertTrue(acl.check(KIM, VIEWER_RELATION, DOC_README));

		assertFalse(acl.check(DANA, MEMBER_RELATION, CONTOSO));
		assertFalse(acl.check(DANA, EDITOR_RELATION, FOLDER_ENGINEERING));
		assertFalse(acl.check(DANA, EDITOR_RELATION, FOLDER_PLANNING));
		assertFalse(acl.check(DANA, EDITOR_RELATION, DOC_README));
		assertFalse(acl.check(DANA, VIEWER_RELATION, DOC_README));
		assertFalse(acl.check(CONTOSO + "#" + MEMBER_RELATION, EDITOR_RELATION, FOLDER_ENGINEERING));
		assertFalse(acl.check(FOLDER_ENGINEERING, PARENT_RELATION, FOLDER_PLANNING));

		// Add Dana to org:contoso, org:contoso#member editor of folder:engineering, folder:engineering parent of folder:planning.
		acl.addTuple(DANA, MEMBER_RELATION, CONTOSO)
			.addTuple(CONTOSO + "#" + MEMBER_RELATION, EDITOR_RELATION, FOLDER_ENGINEERING)
			.addTuple(FOLDER_ENGINEERING, PARENT_RELATION, FOLDER_PLANNING);

		assertTrue(acl.check(DANA, MEMBER_RELATION, CONTOSO));
		assertTrue(acl.check(DANA, EDITOR_RELATION, FOLDER_ENGINEERING));
		assertTrue(acl.check(DANA, EDITOR_RELATION, FOLDER_PLANNING));
		assertTrue(acl.check(DANA, EDITOR_RELATION, DOC_README));
		assertTrue(acl.check(DANA, VIEWER_RELATION, DOC_README));
	}
}
