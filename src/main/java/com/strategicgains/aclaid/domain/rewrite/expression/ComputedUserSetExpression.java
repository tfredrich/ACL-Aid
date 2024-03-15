package com.strategicgains.aclaid.domain.rewrite.expression;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.strategicgains.aclaid.domain.ObjectDefinition;
import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.TupleSet;
import com.strategicgains.aclaid.domain.UserSet;

/**
 * Computes, for the input object, a new userset. For example, this allows the userset expression
 * for a viewer relation to refer to the editor userset on the same object, thus offering an ACL
 * inheritance capability between relations.
 *
 * @author Todd Fredrich
 * @see RewriteExpression
 */
public class ComputedUserSetExpression
implements RewriteExpression
{
	private ObjectDefinition objectDefintion;
	private String relation;

	public ComputedUserSetExpression(ObjectDefinition objectDefintion)
	{
		super();
		setObjectDefintion(objectDefintion);
	}

	public ComputedUserSetExpression(ObjectDefinition objectDefintion, String relation)
	{
		this(objectDefintion);
		setRelation(relation);
	}

	public boolean hasRelation()
	{
		return (relation != null);
	}

	protected void setRelation(String relation)
	{
		this.relation = relation;
	}

	protected void setObjectDefintion(ObjectDefinition objectDefintion)
	{
		this.objectDefintion = objectDefintion;
	}

	public RewriteExpression withRelation(String relation)
	{
		setRelation(relation);
		return this;
	}

	@Override
	public Set<UserSet> rewrite(TupleSet tuples, ObjectId inputObj)
	{
		if (inputObj == null) return Collections.emptySet();

		if (objectDefintion.getName().equals(inputObj.getType()))
		{
			return new HashSet<>(Arrays.asList(new UserSet(inputObj, relation)));
		}

		return Collections.emptySet();
	}
}
