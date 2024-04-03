package com.strategicgains.aclaid.domain.rewrite;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.strategicgains.aclaid.domain.ObjectDefinition;
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
	private ObjectDefinition objectDefinition;
	private String relation;
	private String objectToken;

	public ComputedUserSet(ObjectDefinition objectDefintion)
	{
		super();
		setObjectDefinition(objectDefintion);
	}

	public ComputedUserSet(ObjectDefinition objectDefinition, String relation)
	{
		this(objectDefinition);
		setRelation(relation);
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

	protected void setObjectDefinition(ObjectDefinition objectDefintion)
	{
		this.objectDefinition = objectDefintion;
	}

	public ComputedUserSet withRelation(String relation)
	{
		setRelation(relation);
		return this;
	}

	public ComputedUserSet withToken(String objectToken)
	{
		setObjectToken(objectToken);
		return this;
	}

	@Override
	public Set<UserSet> rewrite(TupleSet tuples, ObjectId objectId)
	{
		if (objectId == null) return Collections.emptySet();

		if (objectDefinition.getName().equals(objectId.getType()))
		{
			return new HashSet<>(Arrays.asList(compute(tuples, objectId, relation)));
		}

		return Collections.emptySet();
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
