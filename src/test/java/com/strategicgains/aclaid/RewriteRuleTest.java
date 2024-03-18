package com.strategicgains.aclaid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.strategicgains.aclaid.domain.LocalTupleSet;
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
			.add(CARL, VIEWER, DOC_SLIDES);
	}

	@Test
	public void testReadOne()
	throws ParseException
	{
		assertNotNull(tuples.readOne(KIM, OWNER, DOC_ROADMAP));		
		assertNotNull(tuples.readOne(BEN, EDITOR, DOC_ROADMAP));		
		assertNotNull(tuples.readOne(CARL, VIEWER, DOC_SLIDES));		
	}

	@Test
	public void testEmptyThis()
	throws ParseException
	{
		ObjectDefinition document = new ObjectDefinition(DOCUMENT_OBJECT);
		RelationDefinition viewer = new RelationDefinition(VIEWER);
		document.addRelation(viewer);
		viewer.setRewriteRules(new This(viewer));
		assertFalse(viewer.check(tuples, UserSet.parse(BEN), new ObjectId(DOC_ROADMAP)));
//		assertFalse(viewer.check(tuples, UserSet.parse(BEN), OWNER, new ObjectId(DOC_ROADMAP)));
//		assertTrue(viewer.check(tuples, UserSet.parse(BEN), EDITOR, new ObjectId(DOC_ROADMAP)));
	}

	@Test
	public void testThis()
	throws ParseException
	{
		RelationDefinition relation = new RelationDefinition(EDITOR);
		relation.setRewriteRules(new This());
		assertTrue(relation.check(tuples, UserSet.parse(BEN), EDITOR, new ObjectId(DOC_ROADMAP)));
	}

	@Test
	public void testComputedUserSet()
	throws ParseException
	{
		RewriteRule rule = new ComputedUserSet().withRelation(EDITOR);
		assertTrue(rule.check(tuples, UserSet.parse(BEN), VIEWER, new ObjectId(DOC_ROADMAP)));
		assertFalse(rule.check(tuples, UserSet.parse(KIM), VIEWER, new ObjectId(DOC_ROADMAP)));
		assertFalse(rule.check(tuples, UserSet.parse(CARL), VIEWER, new ObjectId(DOC_ROADMAP)));
	}

	@Test
	public void testUnion()
	throws ParseException
	{
		ObjectDefinition document = new ObjectDefinition(DOCUMENT_OBJECT);
		RelationDefinition viewer = new RelationDefinition(VIEWER);
		document.addRelation(viewer);
		RewriteRule rule = new Union(
			Arrays.asList(
				new ComputedUserSet(document).withRelation(OWNER),
				new ComputedUserSet(document).withRelation(EDITOR)));
		viewer.setRewriteRules(rule);
		assertTrue(viewer.check(tuples, UserSet.parse(KIM), new ObjectId(DOC_ROADMAP)));
		assertTrue(viewer.check(tuples, UserSet.parse(BEN), new ObjectId(DOC_ROADMAP)));
		assertFalse(viewer.check(tuples, UserSet.parse(BEN), new ObjectId(DOC_ROADMAP)));
	}

	@Test
	public void testInheritence()
	throws ParseException
	{
		ObjectDefinition document = new ObjectDefinition(DOCUMENT_OBJECT);
		RelationDefinition owner = new RelationDefinition(OWNER);
		RelationDefinition editor = new RelationDefinition(EDITOR);
		document.addRelation(owner);
		document.addRelation(editor);
		Union editorRewrite = new Union()
			.addChild(new This(editor))
			.addChild(new ComputedUserSet(document, OWNER));
		editor.setRewriteRules(editorRewrite);
		RelationDefinition viewer = new RelationDefinition(VIEWER);
		Union viewerRewrite = new Union()
			.addChild(new This(viewer))
			.addChild(new ComputedUserSet(document, EDITOR));
		viewer.setRewriteRules(viewerRewrite);
		
		// kim@owner#doc/roadmap
		// kim@editor#doc/roadmap
		// kim@viewer#doc/roadmap
		assertTrue(check(owner, editor, viewer, new Tuple(KIM, OWNER, DOC_ROADMAP)));
		assertTrue(check(owner, editor, viewer, new Tuple(KIM, EDITOR, DOC_ROADMAP)));
		assertTrue(check(owner, editor, viewer, new Tuple(KIM, VIEWER, DOC_ROADMAP)));

		// ben@editor#doc/roadmap
		// ben@viewer#doc/roadmap
		assertTrue(check(owner, editor, viewer, new Tuple(BEN, EDITOR, DOC_ROADMAP)));
		assertTrue(check(owner, editor, viewer, new Tuple(BEN, VIEWER, DOC_ROADMAP)));

		// carl@viewer#doc/slides (direct tuple read)
		assertTrue(check(owner, editor, viewer, new Tuple(CARL, VIEWER, DOC_SLIDES)));
	}

	@Test
	public void testParentFolder()
	throws ParseException
	{
		RelationDefinition parent = new RelationDefinition(PARENT);
		RelationDefinition owner = new RelationDefinition(OWNER);
		RelationDefinition editor = new RelationDefinition(EDITOR);
		Union editorRewrite = new Union()
			.addChild(new This())
			.addChild(new ComputedUserSet(OWNER))
			.addChild(new TupleToUserSet(PARENT, new ComputedUserSet(Tuple.USERSET_OBJECT, EDITOR)));
		editor.setRewriteRules(editorRewrite);
		RelationDefinition viewer = new RelationDefinition(VIEWER);
		Union viewerRewrite = new Union()
			.addChild(new This())
			.addChild(new ComputedUserSet(EDITOR))
			.addChild(new TupleToUserSet(PARENT, new ComputedUserSet(Tuple.USERSET_OBJECT, VIEWER)));
		viewer.setRewriteRules(viewerRewrite);

		assertTrue(check(Arrays.asList(parent, owner, editor, viewer), new Tuple(KIM, OWNER, FOLDER_PLANNING)));
	}

	private boolean check(RelationDefinition owner, RelationDefinition editor, RelationDefinition viewer, Tuple tuple)
	{
		return check(Arrays.asList(owner, editor, viewer), tuple);
	}

	private boolean check(List<RelationDefinition> relations, Tuple key)
	{
		return relations.stream().anyMatch(r -> r.check(tuples, key.getUserset(), key.getRelation(), key.getObjectId()));
	}
}
