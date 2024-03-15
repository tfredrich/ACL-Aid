package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

/**
 * Computes, for the input object, a new userset. For example, this allows the userset expression
 * for a viewer relation to refer to the editor userset on the same object, thus offering an ACL
 * inheritance capability between relations.
 *
 * @author Todd Fredrich
 * @see RewriteRule
 */
public class ComputedUserSet
implements RewriteRule
{
	private String containedRelation;
	private String objectToken;

	public ComputedUserSet()
	{
		super();
	}

	public ComputedUserSet(String relation)
	{
		this();
		setContainedRelation(relation);
	}

	public ComputedUserSet(String objectToken, String relation)
	{
		this();
		setObjectToken(objectToken);
		setContainedRelation(relation);
	}

	protected String getObjectToken()
	{
		return objectToken;
	}

	public boolean hasObjectToken()
	{
		return (objectToken != null);
	}

	protected void setObjectToken(String objectToken)
	{
		this.objectToken = objectToken;
	}

	public boolean hasRelation()
	{
		return (containedRelation != null);
	}

	protected void setContainedRelation(String relation)
	{
		this.containedRelation = relation;
	}

	public RewriteRule withRelation(String relation)
	{
		setContainedRelation(relation);
		return this;
	}

	@Override
	public boolean check(TupleSet relationTuples, UserSet user, String relation, ObjectId objectId)
	{
		return compute(relationTuples, relation, objectId)
			.matches(user, relation, objectId);
	}

	private Tuple compute(TupleSet input, String relation, ObjectId objectId)
	{
		UserSet userset = new UserSet(objectId, containedRelation);
		
		if (hasObjectToken() && getObjectToken().startsWith("$"))
		{
			switch(getObjectToken())
			{
				case Tuple.USERSET_OBJECT:
					System.out.println(Tuple.USERSET_OBJECT + " of " + userset + " / " + objectId);
					input.stream().findFirst().ifPresent(t -> userset.setObjectId(t.getUsersetResource()));
					break;
				case Tuple.USERSET_RELATION:
					System.out.println(Tuple.USERSET_RELATION + " of " + userset);
					break;
				case Tuple.RELATION:
					System.out.println(Tuple.RELATION + " of " + userset);
					break;
				default:
					System.out.println(getObjectToken() + " of " + userset);
			}
		}

//		assertEquals(DOC_ROADMAP, t.getObjectId().toString());
//		assertEquals(VIEWER, t.getRelation());
//		assertEquals(UserSet.parse(DOC_ROADMAP + "#" + OWNER), t.getUserset());

		return new Tuple(userset, relation, objectId);
	}

//	private UserSet compute(String relation, ObjectId objectId)
//	{
//		UserSet userset = new UserSet(objectId, containedRelation);
//		
//		if (hasObjectToken() && getObjectToken().startsWith("$"))
//		{
//			switch(getObjectToken())
//			{
//				case Tuple.USERSET_OBJECT:
//					System.out.println(Tuple.USERSET_OBJECT + " of " + userset + " / " + objectId);
//					input.stream().findFirst().ifPresent(t -> userset.setObjectId(t.getUsersetResource()));
//					break;
//				case Tuple.USERSET_RELATION:
//					System.out.println(Tuple.USERSET_RELATION + " of " + userset);
//					break;
//				case Tuple.RELATION:
//					System.out.println(Tuple.RELATION + " of " + userset);
//					break;
//				default:
//					System.out.println(getObjectToken() + " of " + userset);
//			}
//		}
//
////		assertEquals(DOC_ROADMAP, t.getObjectId().toString());
////		assertEquals(VIEWER, t.getRelation());
////		assertEquals(UserSet.parse(DOC_ROADMAP + "#" + OWNER), t.getUserset());
//
//		return new Tuple(userset, relation, objectId);
//	}
}
