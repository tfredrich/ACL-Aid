package com.strategicgains.aclaid.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import com.strategicgains.aclaid.exception.InvalidTupleException;

public class LocalTupleSetTest
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

	private LocalTupleSet ts;

	@Before
	public void initialize()
	throws ParseException, InvalidTupleException
	{
		ts = new LocalTupleSet()
			.add(KIM, OWNER_RELATION, DOC_ROADMAP)
			.add(BEN, EDITOR_RELATION, DOC_ROADMAP)
			.add(KIM, EDITOR_RELATION, DOC_ROADMAP)
			.add(CARL, MEMBER_RELATION, CONTOSO)
			.add(DANA, MEMBER_RELATION, CONTOSO)
			.add(CONTOSO + "#" + MEMBER_RELATION, VIEWER_RELATION, DOC_SLIDES)
			.add(CONTOSO + "#" + MEMBER_RELATION, EDITOR_RELATION, FOLDER_ENGINEERING)
			.add(FOLDER_ENGINEERING, PARENT_RELATION, FOLDER_PLANNING)
			.add(FOLDER_PLANNING, PARENT_RELATION, DOC_README);

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
		assertEquals(DOC_ROADMAP, tuple.getResource().toString());
		assertEquals(OWNER_RELATION, tuple.getRelation());
		assertEquals(KIM, tuple.getUserset().toString());
	}

	@Test
	public void testReadOneKimEditorOfRoadmap()
	throws ParseException
	{
		Tuple tuple = ts.readOne(KIM, EDITOR_RELATION, DOC_ROADMAP);
		assertNotNull(tuple);
		assertEquals(DOC_ROADMAP, tuple.getResource().toString());
		assertEquals(EDITOR_RELATION, tuple.getRelation());
		assertEquals(KIM, tuple.getUserset().toString());
	}

	@Test
	public void testReadEditorsForRoadmap()
	throws ParseException
	{
		LocalTupleSet tupleSet = ts.read(EDITOR_RELATION, new ResourceName(DOC_ROADMAP));
		assertNotNull(tupleSet);
		assertEquals(2, tupleSet.size());
	}

	@Test
	public void testReadOneCarlViewerOfSlides()
	throws ParseException
	{
		Tuple tuple = ts.readOne(CARL, VIEWER_RELATION, DOC_SLIDES);
		assertNotNull(tuple);
		assertEquals(DOC_SLIDES, tuple.getResource().toString());
		assertEquals(VIEWER_RELATION, tuple.getRelation());
		assertEquals(CARL, tuple.getUserset().toString());
	}

	@Test
	public void testReadOneDanaEditorOfEngineeringFolder()
	throws ParseException
	{
		Tuple tuple = ts.readOne(DANA, EDITOR_RELATION, FOLDER_ENGINEERING);
		assertNotNull(tuple);
		assertEquals(FOLDER_ENGINEERING, tuple.getResource().toString());
		assertEquals(EDITOR_RELATION, tuple.getRelation());
		assertEquals(DANA, tuple.getUserset().toString());
	}
}
