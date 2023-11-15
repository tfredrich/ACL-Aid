package com.strategicgains.aclaid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.rewrite.ComputedUserSet;
import com.strategicgains.aclaid.domain.rewrite.This;
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

	private TupleSet tuples;

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
	public void testInheritence()
	throws ParseException
	{
		RelationDefinition owner = new RelationDefinition(OWNER);
		RelationDefinition editor = new RelationDefinition(EDITOR);
		Union editorRewrite = new Union(editor)
			.child(new This(editor))
			.child(new ComputedUserSet(editor, OWNER));
		editor.setRewriteRules(editorRewrite);
		RelationDefinition viewer = new RelationDefinition(VIEWER);
		Union viewerRewrite = new Union(viewer)
			.child(new This(viewer))
			.child(new ComputedUserSet(viewer, EDITOR));
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
	public void testRewriteTupleSets()
	throws ParseException
	{
		RelationDefinition owner = new RelationDefinition(OWNER);
		RelationDefinition editor = new RelationDefinition(EDITOR);
		Union editorRewrite = new Union(editor)
			.child(new This(editor))
			.child(new ComputedUserSet(editor, OWNER));
		editor.setRewriteRules(editorRewrite);
		RelationDefinition viewer = new RelationDefinition(VIEWER);
		Union viewerRewrite = new Union(viewer)
			.child(new This(viewer))
			.child(new ComputedUserSet(viewer, EDITOR));
		viewer.setRewriteRules(viewerRewrite);

		TupleSet owners = owner.rewrite(tuples, new ResourceName(DOC_ROADMAP));
		assertEquals(1, owners.size());
		TupleSet editors = editor.rewrite(tuples, new ResourceName(DOC_ROADMAP));
		assertEquals(2, editors.size());
		TupleSet viewers = editor.rewrite(tuples, new ResourceName(DOC_ROADMAP));
		assertEquals(2, viewers.size());

//		owners = owner.rewrite(tuples, new ResourceName(DOC_SLIDES));
//		assertEquals(0, owners.size());
		editors = editor.rewrite(tuples, new ResourceName(DOC_SLIDES));
		assertEquals(0, editors.size());
		viewers = viewer.rewrite(tuples, new ResourceName(DOC_SLIDES));
		assertEquals(1, viewers.size());

	}

	@Test
	public void testParentFolder()
	throws ParseException
	{
		RelationDefinition parent = new RelationDefinition(PARENT);
		RelationDefinition owner = new RelationDefinition(OWNER);
		RelationDefinition editor = new RelationDefinition(EDITOR);
		Union editorRewrite = new Union(editor)
			.child(new This(editor))
			.child(new ComputedUserSet(editor, OWNER));
		editor.setRewriteRules(editorRewrite);
		RelationDefinition viewer = new RelationDefinition(VIEWER);
		Union viewerRewrite = new Union(viewer)
			.child(new This(viewer))
			.child(new ComputedUserSet(viewer, EDITOR));
		viewer.setRewriteRules(viewerRewrite);
	}

	private boolean check(RelationDefinition owner, RelationDefinition editor, RelationDefinition viewer, Tuple key)
	{
		TupleSet t = new LocalTupleSet().addAll(tuples);
		TupleSet owners = owner.rewrite(t, key.getResource());
		t.addAll(owners);
		TupleSet editors = editor.rewrite(t, key.getResource());
		t.addAll(owners).addAll(editors);
		TupleSet viewers = viewer.rewrite(t, key.getResource());
		t.addAll(owners).addAll(editors).addAll(viewers);
		return (t.readOne(key.getUserset(), key.getRelation(), key.getResource()) != null);
	}

	private boolean check(RelationDefinition parent, RelationDefinition owner, RelationDefinition editor, RelationDefinition viewer, Tuple key)
	{
		TupleSet t = new LocalTupleSet().addAll(tuples);
		TupleSet parents = parent.rewrite(t, key.getResource());
		t.addAll(parents);
		TupleSet owners = owner.rewrite(t, key.getResource());
		t.addAll(owners);
		TupleSet editors = editor.rewrite(t, key.getResource());
		t.addAll(owners).addAll(editors);
		TupleSet viewers = viewer.rewrite(t, key.getResource());
		t.addAll(owners).addAll(editors).addAll(viewers);
		return (t.readOne(key.getUserset(), key.getRelation(), key.getResource()) != null);
	}
}