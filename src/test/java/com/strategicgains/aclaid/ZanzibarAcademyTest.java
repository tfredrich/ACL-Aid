package com.strategicgains.aclaid;

import static com.strategicgains.aclaid.builder.rewrite.Rewrites._this;
import static com.strategicgains.aclaid.builder.rewrite.Rewrites.computedUserSet;
import static com.strategicgains.aclaid.builder.rewrite.Rewrites.union;
import static com.strategicgains.aclaid.builder.rewrite.Rewrites.tupleToUserSet;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

import com.strategicgains.aclaid.builder.AccessControlBuilder;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.exception.InvalidTupleException;

/**
 * A unit test that checks ACL-Aid against the Zanzibar-based
 * examples and concepts explained at: https://zanzibar.academy/
 * 
 * @author tfredrich
 * @see https://zanzibar.academy/
 */
//@Ignore
public class ZanzibarAcademyTest
{
	private static final String NAMESPACE = "ZanzibarAcademyTest:";
	private static final String DOCUMENT_OBJECT = "doc";
	private static final String ORGANIZATION_OBJECT = "org";
	private static final String FOLDER_OBJECT = "folder";
	private static final String USER_OBJECT = "user";

	// Relations
	private static final String EDITOR = "editor";
	private static final String MEMBER = "member";
	private static final String OWNER = "owner";
	private static final String PARENT = "parent";
	private static final String VIEWER = "viewer";

	// Groups
	private static final String CONTOSO = NAMESPACE + ORGANIZATION_OBJECT + "/contoso";

	// Users
	private static final String KIM = NAMESPACE + USER_OBJECT + "/kim";
	private static final String BEN = NAMESPACE + USER_OBJECT + "/ben";
	private static final String CARL = NAMESPACE + USER_OBJECT + "/carl";
	private static final String DANA = NAMESPACE + USER_OBJECT + "/dana";

	// Resources
	private static final String DOC_ROADMAP = NAMESPACE + DOCUMENT_OBJECT + "/roadmap";
	private static final String DOC_README = NAMESPACE + DOCUMENT_OBJECT + "/readme";
	private static final String DOC_SLIDES = NAMESPACE + DOCUMENT_OBJECT + "/slides";
	private static final String FOLDER_PLANNING = NAMESPACE + FOLDER_OBJECT + "/planning";
	private static final String FOLDER_ENGINEERING = NAMESPACE + FOLDER_OBJECT + "/engineering";

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
	 * 
	 * @throws ParseException 
	 * @throws RelationNotRegisteredException 
	 * @throws InvalidTupleException 
	 */
	@Test
	public void test01Basics()
	throws ParseException, InvalidTupleException
	{
		AccessControlBuilder builder = new AccessControlBuilder();
		builder
			.object(DOCUMENT_OBJECT)
				.relation(EDITOR)
				.relation(VIEWER)
				.relation(OWNER)

			.tuple(KIM, OWNER, DOC_ROADMAP)
			.tuple(BEN, EDITOR, DOC_ROADMAP)
			.tuple(CARL, VIEWER, DOC_SLIDES);

		AccessControl acl = builder.build();

		assertTrue(acl.check(CARL, VIEWER, DOC_SLIDES));
		assertTrue(acl.check(KIM, OWNER, DOC_ROADMAP));
		assertTrue(acl.check(BEN, EDITOR, DOC_ROADMAP));

		assertFalse(acl.check(KIM, EDITOR, DOC_ROADMAP));

		// Add Kim as editor of the doc:roadmap
		acl.addTuple(KIM, EDITOR, DOC_ROADMAP);
		assertTrue(acl.check(KIM, EDITOR, DOC_ROADMAP));
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
	public void test02Inheritence()
	throws ParseException, InvalidTupleException
	{
		AccessControlBuilder builder = new AccessControlBuilder();
		builder
			.object(DOCUMENT_OBJECT)
				.relation(OWNER)
				.relation(EDITOR)
					.rewrite(
						union(
							_this(),
							computedUserSet(OWNER)
						)
					)
				.relation(VIEWER)
					.rewrite(
						union(
							_this(),
							computedUserSet(EDITOR)
						)
					)

			.tuple(KIM, OWNER, DOC_ROADMAP)
			.tuple(BEN, EDITOR, DOC_ROADMAP)
			.tuple(CARL, VIEWER, DOC_SLIDES);

		AccessControl acl = builder.build();

		assertTrue(acl.check(KIM, OWNER, DOC_ROADMAP));
		assertTrue(acl.check(KIM, EDITOR, DOC_ROADMAP));
		assertTrue(acl.check(BEN, EDITOR, DOC_ROADMAP));
		assertTrue(acl.check(BEN, VIEWER, DOC_ROADMAP));
		assertTrue(acl.check(CARL, VIEWER, DOC_SLIDES));
		assertTrue(acl.check(KIM, VIEWER, DOC_ROADMAP));
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
	throws ParseException, InvalidTupleException
	{
		AccessControlBuilder builder = new AccessControlBuilder();
		builder
			.object(ORGANIZATION_OBJECT)
				.relation(MEMBER)

			.object(DOCUMENT_OBJECT)
				.relation(OWNER)
				.relation(EDITOR)
					.rewrite(
						union(
							_this(),
							computedUserSet(OWNER)
						)
					)
				.relation(VIEWER)
					.rewrite(
						union(
							_this(),
							computedUserSet(EDITOR)
						)
					)
			.object(DOC_README)
			.tuple(CARL, MEMBER, CONTOSO)
			.tuple(CONTOSO + "#" + MEMBER, VIEWER, DOC_SLIDES);

		AccessControl acl = builder.build();

		assertTrue(acl.check(CARL, MEMBER, CONTOSO));
		assertTrue(acl.check(CARL, VIEWER, DOC_SLIDES));
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
	 *           computed_userset {
	 *             object: $TUPLE_USERSET_OBJECT  # parent folder
	 *             relation: "viewer" }}}
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
	throws ParseException, InvalidTupleException
	{
		AccessControlBuilder builder = new AccessControlBuilder();
		builder
			.object(ORGANIZATION_OBJECT)
				.relation(MEMBER)
			.object(FOLDER_OBJECT)
				.relation(PARENT)
				.relation(OWNER)
				.relation(EDITOR)
				.rewrite(
					union(
						_this(),
						computedUserSet(OWNER)
					)
				)
				.relation(VIEWER)
				.rewrite(
					union(
						_this(),
						computedUserSet(EDITOR)
					)
				)
			.object(DOCUMENT_OBJECT)
				.relation(PARENT)
				.relation(OWNER)
				.relation(EDITOR)
					.rewrite(
						union(
							_this(),
							computedUserSet(OWNER),
							tupleToUserSet(
								PARENT,
								computedUserSet(EDITOR)
									.resource(Tuple.USERSET_OBJECT)
							)
						)
					)
				.relation(VIEWER)
					.rewrite(
						union(
							_this(),
							computedUserSet(EDITOR),
							tupleToUserSet(
								PARENT,
								computedUserSet(VIEWER)
									.resource(Tuple.USERSET_OBJECT)
							)
						)
					)

					// +viewer on parent [folder] provides viewer on document
//					.ownedBy(VIEWER_RELATION, PARENT_RELATION)

			.tuple(FOLDER_PLANNING, PARENT, DOC_README)
			.tuple(KIM, VIEWER, FOLDER_PLANNING);

		AccessControl acl = builder.build();

		assertTrue(acl.check(FOLDER_PLANNING, PARENT, DOC_README));
		assertTrue(acl.check(KIM, VIEWER, FOLDER_PLANNING));
		assertTrue(acl.check(KIM, VIEWER, DOC_README));

		assertFalse(acl.check(DANA, MEMBER, CONTOSO));
		assertFalse(acl.check(DANA, EDITOR, FOLDER_ENGINEERING));
		assertFalse(acl.check(DANA, EDITOR, FOLDER_PLANNING));
		assertFalse(acl.check(DANA, EDITOR, DOC_README));
		assertFalse(acl.check(DANA, VIEWER, DOC_README));
		assertFalse(acl.check(CONTOSO + "#" + MEMBER, EDITOR, FOLDER_ENGINEERING));
		assertFalse(acl.check(FOLDER_ENGINEERING, PARENT, FOLDER_PLANNING));

		// Add Dana to org:contoso, org:contoso#member editor of folder:engineering, folder:engineering parent of folder:planning.
		acl.addTuple(DANA, MEMBER, CONTOSO)
			.addTuple(CONTOSO + "#" + MEMBER, EDITOR, FOLDER_ENGINEERING)
			.addTuple(FOLDER_ENGINEERING, PARENT, FOLDER_PLANNING);

		assertTrue(acl.check(DANA, MEMBER, CONTOSO));
		assertTrue(acl.check(DANA, EDITOR, FOLDER_ENGINEERING));
		assertTrue(acl.check(DANA, EDITOR, FOLDER_PLANNING));
		assertTrue(acl.check(DANA, EDITOR, DOC_README));
		assertTrue(acl.check(DANA, VIEWER, DOC_README));
	}
}
