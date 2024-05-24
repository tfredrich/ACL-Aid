package com.strategicgains.aclaid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.strategicgains.aclaid.domain.InMemoryTupleSet;
import com.strategicgains.aclaid.domain.ObjectDefinition;
import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.domain.rewrite.ComputedUserSet;
import com.strategicgains.aclaid.domain.rewrite.RewriteRule;
import com.strategicgains.aclaid.domain.rewrite.This;
import com.strategicgains.aclaid.domain.rewrite.TupleToUserSet;
import com.strategicgains.aclaid.domain.rewrite.Union;
import com.strategicgains.aclaid.domain.rewrite.expression.UsersetExpression;
import com.strategicgains.aclaid.exception.InvalidTupleException;

public class RewriteRuleTest
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

	private InMemoryTupleSet tuples;

	@Before
	public void initialize()
	throws ParseException, InvalidTupleException
	{
		tuples = new InMemoryTupleSet();		
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
		RewriteRule rule = new This(new RelationDefinition(VIEWER));
		UsersetExpression rewrite = rule.rewrite(new ObjectId(DOC_ROADMAP));
		assertTrue(rewrite.evaluate(tuples).isEmpty());
	}

	@Test
	public void testThis()
	throws ParseException, InvalidTupleException
    {
        InMemoryTupleSet local = new InMemoryTupleSet(tuples);
        local.add(KIM, VIEWER, DOC_ROADMAP);
        RewriteRule rule = new This(new RelationDefinition(VIEWER));
        UsersetExpression rewrite = rule.rewrite(new ObjectId(DOC_ROADMAP));
        Set<UserSet> users = rewrite.evaluate(local);
        assertTrue(users.contains(UserSet.parse(KIM)));
        assertEquals(1, users.size());
    }

	@Test
	public void testComputedUserSet()
	throws ParseException
	{
		RewriteRule rule = new ComputedUserSet(EDITOR);
		UsersetExpression rewrite = rule.rewrite(new ObjectId(DOC_ROADMAP));
		Set<UserSet> users = rewrite.evaluate(tuples);
		assertTrue(users.contains(UserSet.parse(BEN)));
		assertEquals(1, users.size());
	}

	@Test
	public void testTupleToUserSet()
	throws ParseException
	{
		RewriteRule rule = new TupleToUserSet(PARENT,
			new ComputedUserSet(VIEWER)
				.withToken(Tuple.USERSET_OBJECT));
		UsersetExpression rewrite = rule.rewrite(new ObjectId(DOC_ROADMAP));
		Set<UserSet> users = rewrite.evaluate(tuples);
		assertEquals(2, users.size());
		assertTrue(users.contains(UserSet.parse(FOLDER_ENGINEERING + "#" + VIEWER)));
		assertTrue(users.contains(UserSet.parse(FOLDER_PLANNING + "#" + VIEWER)));
	}

	@Test
	public void testUnion()
	throws ParseException
	{
		ObjectDefinition docs = new ObjectDefinition(DOCUMENT_OBJECT);
		RelationDefinition viewer = new RelationDefinition(VIEWER);
		docs.addRelation(viewer);
		RewriteRule rule = new Union(
			Arrays.asList(
				new This(viewer),
				new ComputedUserSet(OWNER),
				new ComputedUserSet(EDITOR)
			)
		);
		UsersetExpression rewrite = rule.rewrite(new ObjectId(DOC_ROADMAP));
		Set<UserSet> users = rewrite.evaluate(tuples);
		assertEquals(2, users.size());
		assertTrue(users.contains(UserSet.parse(KIM)));
		assertTrue(users.contains(UserSet.parse(BEN)));
	}
}
