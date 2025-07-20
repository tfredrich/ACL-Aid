package com.strategicgains.aclaid.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.strategicgains.aclaid.exception.InvalidTupleException;

public class SimpleTupleStoreTest
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
	private static final String CONTOSO_MEMBER = CONTOSO + "#" + MEMBER_RELATION;

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

	private SimpleTupleStore ts;

	@Before
	public void initialize()
	throws ParseException, InvalidTupleException
	{
		ts = new SimpleTupleStore()
			.add(KIM, OWNER_RELATION, DOC_ROADMAP)
			.add(BEN, EDITOR_RELATION, DOC_ROADMAP)
			.add(KIM, EDITOR_RELATION, DOC_ROADMAP)
			.add(CARL, MEMBER_RELATION, CONTOSO)
			.add(DANA, MEMBER_RELATION, CONTOSO)
			.add(CONTOSO_MEMBER, VIEWER_RELATION, DOC_SLIDES)
			.add(CONTOSO_MEMBER, EDITOR_RELATION, FOLDER_ENGINEERING)
			.add(FOLDER_PLANNING, PARENT_RELATION, FOLDER_ENGINEERING)
			.add(FOLDER_ENGINEERING, PARENT_RELATION, DOC_README);

	}

	@Test
	public void testReadOneKimViewerOfRoadmap()
	throws ParseException
	{
		Tuple tuple = ts.readOne(KIM, VIEWER_RELATION, DOC_ROADMAP);
		assertNull(tuple);
	}

	@Test
	public void testReadOneKimOwnerOfRoadmap()
	throws ParseException
	{
		Tuple tuple = ts.readOne(KIM, OWNER_RELATION, DOC_ROADMAP);
		assertNotNull(tuple);
		assertEquals(DOC_ROADMAP, tuple.getObjectId().toString());
		assertEquals(OWNER_RELATION, tuple.getRelation());
		assertEquals(KIM, tuple.getUserset().toString());
	}

	@Test
	public void testReadOneKimEditorOfRoadmap()
	throws ParseException
	{
		Tuple tuple = ts.readOne(KIM, EDITOR_RELATION, DOC_ROADMAP);
		assertNotNull(tuple);
		assertEquals(DOC_ROADMAP, tuple.getObjectId().toString());
		assertEquals(EDITOR_RELATION, tuple.getRelation());
		assertEquals(KIM, tuple.getUserset().toString());
	}

	@Test
	public void testReadEditorsForRoadmap()
	throws ParseException
	{
		Set<Tuple> tuples = ts.readAll(new ObjectId(DOC_ROADMAP), EDITOR_RELATION);
		assertNotNull(tuples);
		assertEquals(2, tuples.size());
//		assertTrue(tuples.contains(UserSet.parse(KIM)));
//		assertTrue(tuples.contains(UserSet.parse(BEN)));
	}

	@Test
	public void testReadViewersSlides()
	throws ParseException
	{
		Set<Tuple> tuples = ts.readAll(new ObjectId(DOC_SLIDES), VIEWER_RELATION);
		assertNotNull(tuples);
		assertEquals(1, tuples.size());
//		assertTrue(tuples.contains(UserSet.parse(CONTOSO_MEMBER)));
	}

//	@Test
//	public void testExpandViewersSlides()
//	throws ParseException
//	{
//		Set<UserSet> usersets = ts.expandUserSets(VIEWER_RELATION, new ObjectId(DOC_SLIDES));
//		assertNotNull(usersets);
//		assertEquals(3, usersets.size());
//		assertTrue(usersets.contains(UserSet.parse(CARL)));
//		assertTrue(usersets.contains(UserSet.parse(DANA)));
//		assertTrue(usersets.contains(UserSet.parse(CONTOSO_MEMBER)));
//	}

	@Test
	public void testReadOneCarlViewerOfSlides()
	throws ParseException
	{
		Tuple tuple = ts.readOne(CARL, VIEWER_RELATION, DOC_SLIDES);
		assertNotNull(tuple);
		assertEquals(DOC_SLIDES, tuple.getObjectId().toString());
		assertEquals(VIEWER_RELATION, tuple.getRelation());
		assertEquals(CARL, tuple.getUserset().toString());
	}

	@Test
	public void testReadOneDanaEditorOfEngineeringFolder()
	throws ParseException
	{
		Tuple tuple = ts.readOne(DANA, EDITOR_RELATION, FOLDER_ENGINEERING);
		assertNotNull(tuple);
		assertEquals(FOLDER_ENGINEERING, tuple.getObjectId().toString());
		assertEquals(EDITOR_RELATION, tuple.getRelation());
		assertEquals(DANA, tuple.getUserset().toString());
	}

//	@Test
//	public void testExpandDocReadmeParent()
//	throws ParseException
//	{
//		Set<UserSet> usersets = ts.expandUserSets(PARENT_RELATION, new ObjectId(DOC_README));
//		assertNotNull(usersets);
//		assertEquals(2, usersets.size());
//		assertTrue(usersets.contains(UserSet.parse(FOLDER_ENGINEERING)));
//		assertTrue(usersets.contains(UserSet.parse(FOLDER_PLANNING)));
//	}

//	@Test
//	public void testExpandDocSlidesViewer()
//	throws ParseException
//	{
//		Set<UserSet> usersets = ts.expandUserSets(VIEWER_RELATION, new ObjectId(DOC_SLIDES));
//		assertNotNull(usersets);
//		assertEquals(3, usersets.size());
//		assertTrue(usersets.contains(UserSet.parse(CONTOSO_MEMBER)));
//		assertTrue(usersets.contains(UserSet.parse(CARL)));
//		assertTrue(usersets.contains(UserSet.parse(DANA)));
//	}	
}
