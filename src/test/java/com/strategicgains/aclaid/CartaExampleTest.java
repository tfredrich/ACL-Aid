package com.strategicgains.aclaid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

import com.strategicgains.aclaid.domain.SimpleTupleStore;
import com.strategicgains.aclaid.exception.InvalidTupleException;

public class CartaExampleTest {
	private static final String NAMESPACE = "CartaExample:";
	private static final String CLASS_OBJECT = "class";
	private static final String EMPLOYEE_OBJECT = "employee";
	private static final String GRADE_OBJECT = "grade";

	private static final String TEACHER_RELATION = "teacher";
	private static final String EDIT_RELATION = "edit";
	private static final String VIEW_RELATION = "view";

	// Users
	private static final String EMP_1 = NAMESPACE + EMPLOYEE_OBJECT + "/kim";
	private static final String EMP_2 = NAMESPACE + EMPLOYEE_OBJECT + "/bob";

	// Resources
	private static final String CLASS_A = NAMESPACE + CLASS_OBJECT + "/a";
	private static final String CLASS_B = NAMESPACE + CLASS_OBJECT + "/b";
	private static final String GRADE_S = NAMESPACE + GRADE_OBJECT + "/s";
	private static final String GRADE_T = NAMESPACE + GRADE_OBJECT + "/t";
	private static final String GRADE_X = NAMESPACE + GRADE_OBJECT + "/x";
	private static final String GRADE_Y = NAMESPACE + GRADE_OBJECT + "/y";

	/**
	 * Using the Carta example, we have the following relationships:
	 * 
	 *                                    *---> grade:x#edit +---> grade:x#view
	 *                                   /
	 * employee:kim --> class:a#teacher *
	 *                                   \
	 *                                    *---> grade:y#edit +---> grade:y#view
	 *
	 * And for negative testing, we add the following relationships:
	 * 
	 *                                    *---> grade:s#edit
	 *                                   /
	 * employee:bob --> class:b#teacher *
	 *                                   \
	 *                                    *---> grade:t#edit
	 *                                    
	 * @throws ParseException
	 * @throws InvalidTupleException
	 * see: https://medium.com/building-carta/user-authorization-in-less-than-10-milliseconds-f20d277fec47
	 */
	@Test
	public void test() throws ParseException, InvalidTupleException {
		SimpleTupleStore tuples = new SimpleTupleStore()
			.add(EMP_1, TEACHER_RELATION, CLASS_A)
			.add(EMP_2, TEACHER_RELATION, CLASS_B)
			.add(CLASS_A + "#" + TEACHER_RELATION, EDIT_RELATION, GRADE_X)
			.add(CLASS_A + "#" + TEACHER_RELATION, EDIT_RELATION, GRADE_Y)
			.add(CLASS_B + "#" + TEACHER_RELATION, EDIT_RELATION, GRADE_S)
			.add(CLASS_B + "#" + TEACHER_RELATION, EDIT_RELATION, GRADE_T)
			.add(GRADE_X + "#" + EDIT_RELATION, VIEW_RELATION, GRADE_X)
			.add(GRADE_Y + "#" + EDIT_RELATION, VIEW_RELATION, GRADE_Y);

		assertTrue(tuples.check(EMP_1, TEACHER_RELATION, CLASS_A));
		assertFalse(tuples.check(EMP_1, TEACHER_RELATION, CLASS_B));
		assertTrue(tuples.check(EMP_1, EDIT_RELATION, GRADE_X));
		assertTrue(tuples.check(EMP_1, VIEW_RELATION, GRADE_X));
		assertTrue(tuples.check(EMP_1, VIEW_RELATION, GRADE_Y));
		assertTrue(tuples.check(EMP_1, EDIT_RELATION, GRADE_Y));
		assertFalse(tuples.check(EMP_1, EDIT_RELATION, GRADE_S));
		assertFalse(tuples.check(EMP_1, EDIT_RELATION, GRADE_T));

		assertTrue(tuples.check(EMP_2, TEACHER_RELATION, CLASS_B));
		assertFalse(tuples.check(EMP_2, TEACHER_RELATION, CLASS_A));
		assertTrue(tuples.check(EMP_2, EDIT_RELATION, GRADE_S));
		assertTrue(tuples.check(EMP_2, EDIT_RELATION, GRADE_T));
		assertFalse(tuples.check(EMP_2, EDIT_RELATION, GRADE_Y));
	}
}
