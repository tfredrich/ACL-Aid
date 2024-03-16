package com.strategicgains.aclaid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.ObjectDefinition;
import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.domain.rewrite.expression.ComputedUserSetExpression;
import com.strategicgains.aclaid.domain.rewrite.expression.RewriteExpression;
import com.strategicgains.aclaid.domain.rewrite.expression.ThisExpression;
import com.strategicgains.aclaid.domain.rewrite.expression.UnionExpression;
import com.strategicgains.aclaid.exception.InvalidTupleException;

public class RewriteExpressionTest
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

	// Resources
	private static final String DOC_ROADMAP = NAMESPACE + DOCUMENT_OBJECT + "/roadmap";
	private static final String DOC_README = NAMESPACE + DOCUMENT_OBJECT + "/readme";
	private static final String DOC_SLIDES = NAMESPACE + DOCUMENT_OBJECT + "/slides";
	private static final String FOLDER_PLANNING = NAMESPACE + FOLDER_OBJECT + "/planning";
	private static final String FOLDER_ENGINEERING = NAMESPACE + FOLDER_OBJECT + "/engineering";

	private LocalTupleSet tuples;

	@Before
	public void initialize()
	throws ParseException, InvalidTupleException
	{
		tuples = new LocalTupleSet();		
		tuples
			.add(KIM, OWNER, DOC_ROADMAP)
			.add(BEN, EDITOR, DOC_ROADMAP)
			.add(CARL, VIEWER, DOC_SLIDES)

			.add(CARL, MEMBER, CONTOSO)
			.add(CONTOSO + "#" + MEMBER, VIEWER, FOLDER_PLANNING)
			
			.add(FOLDER_PLANNING, PARENT, FOLDER_ENGINEERING)
			.add(FOLDER_ENGINEERING, PARENT, DOC_ROADMAP);
;
	}

	@Test
	public void testEmptyThis()
	throws ParseException
	{
		RewriteExpression rule = new ThisExpression(new RelationDefinition(VIEWER));
		Set<UserSet> rewrite = rule.rewrite(tuples, new ObjectId(DOC_ROADMAP));
		assertTrue(rewrite.isEmpty());
	}

	@Test
	public void testThis()
	throws ParseException, InvalidTupleException
    {
        LocalTupleSet local = new LocalTupleSet(tuples);
        local.add(KIM, VIEWER, DOC_ROADMAP);
        RewriteExpression rule = new ThisExpression(new RelationDefinition(VIEWER));
        Set<UserSet> rewrite = rule.rewrite(local, new ObjectId(DOC_ROADMAP));
        assertTrue(rewrite.contains(UserSet.parse(KIM)));
    }

	@Test
	public void testComputedUserSet()
	throws ParseException
	{
		RewriteExpression rule = new ComputedUserSetExpression(new ObjectDefinition(DOCUMENT_OBJECT)).withRelation(EDITOR);
		Set<UserSet> rewrite = rule.rewrite(tuples, new ObjectId(DOC_ROADMAP));
		assertTrue(rewrite.contains(UserSet.parse(DOC_ROADMAP + "#" + EDITOR)));
		assertEquals(1, rewrite.size());
	}

	@Test
	public void testTupleToUserSet()
	throws ParseException
	{
		RewriteExpression rule = new ComputedUserSetExpression(new ObjectDefinition(DOCUMENT_OBJECT))
				.withRelation(OWNER);
		Set<UserSet> rewrite = rule.rewrite(tuples, new ObjectId(DOC_ROADMAP));
		assertTrue(rewrite.contains(UserSet.parse(DOC_ROADMAP + "#" + OWNER)));
		assertEquals(1, rewrite.size());
	}

	@Test
	public void testUnion()
	throws ParseException
	{
		ObjectDefinition docs = new ObjectDefinition(DOCUMENT_OBJECT);
		RelationDefinition viewer = new RelationDefinition(VIEWER);
		docs.addRelation(viewer);
		RewriteExpression rule = new UnionExpression(
			Arrays.asList(
				new ThisExpression(viewer),
				new ComputedUserSetExpression(docs).withRelation(OWNER),
				new ComputedUserSetExpression(docs).withRelation(EDITOR)
			)
		);
		Set<UserSet> rewrite = rule.rewrite(tuples, new ObjectId(DOC_ROADMAP));
		assertTrue(rewrite.contains(UserSet.parse(DOC_ROADMAP + "#" + EDITOR)));
		assertTrue(rewrite.contains(UserSet.parse(DOC_ROADMAP + "#" + OWNER)));
		assertEquals(2, rewrite.size());
	}
}
