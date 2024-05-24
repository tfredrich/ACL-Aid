package com.strategicgains.aclaid.domain.rewrite.expression;

import java.util.Set;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.domain.rewrite.RewriteRule;

/**
 * Computes, for the input object, a new userset. For example, this allows the userset expression
 * for a viewer relation to refer to the editor userset on the same object, thus offering an ACL
 * inheritance capability between relations.
 *
 * @author Todd Fredrich
 * @see RewriteRule
 */
public class ComputedUserSetExpression
implements UsersetExpression
{
	private ObjectId objectId;
	private String relation;
	private String objectToken;

	public ComputedUserSetExpression(ObjectId objectId, String relation, String objectToken)
	{
		super();
		setObjectId(objectId);
		setRelation(relation);
		setObjectToken(objectToken);
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
		return (relation != null);
	}

	protected void setRelation(String relation)
	{
		this.relation = relation;
	}

	protected void setObjectId(ObjectId objectId)
	{
		this.objectId = objectId;
	}

	@Override
	public Set<UserSet> evaluate(TupleSet tuples)
	{
		TupleSet filtered = tuples.readAll(relation, objectId);
		UserSet userset = compute(filtered, objectId, relation);
		return tuples.expandUserSets(userset.getRelation(), userset.getObjectId());
	}

	private UserSet compute(TupleSet tuples, ObjectId objectId, String relation)
	{
		UserSet userset = new UserSet(objectId, relation);
		
		if (hasObjectToken() && getObjectToken().startsWith("$"))
		{
			switch(getObjectToken())
			{
				case Tuple.USERSET_OBJECT:
					System.out.println(Tuple.USERSET_OBJECT + " of " + userset + " / " + objectId);
//					tuples.stream().findFirst().ifPresent(t -> userset.setObjectId(t.getUsersetResource()));
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

		return userset;
	}
}
