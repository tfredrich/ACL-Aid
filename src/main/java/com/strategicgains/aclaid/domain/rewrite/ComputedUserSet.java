package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.ObjectId;
import com.strategicgains.aclaid.domain.RelationDefinition;
import com.strategicgains.aclaid.domain.rewrite.expression.ComputedUserSetExpression;
import com.strategicgains.aclaid.domain.rewrite.expression.ThisExpression;
import com.strategicgains.aclaid.domain.rewrite.expression.UnionPredicate;
import com.strategicgains.aclaid.domain.rewrite.expression.UsersetExpression;

/**
 * From the Zanzibar document:
 * Computes, for the input object, a new userset. For example, this allows the userset expression
 * for a viewer relation to refer to the editor userset on the same object, thus offering an ACL
 * inheritance capability between relations.
 *
 * @author Todd Fredrich
 * @see RewriteRule
 */
public class ComputedUserSet
implements RewriteRuleLeaf
{
	private RelationDefinition relation;
	private String objectToken;

	public ComputedUserSet(RelationDefinition relation)
	{
		super();
		setRelation(relation);
	}

	public ComputedUserSet(RelationDefinition relation, String objectToken)
	{
		this(relation);
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

	protected void setRelation(RelationDefinition relation)
	{
		this.relation = relation;
	}

	public ComputedUserSet withRelation(RelationDefinition relation)
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
	public UsersetExpression rewrite(ObjectId objectId)
	{
		if (hasObjectToken() && getObjectToken().startsWith("$"))
		{
			System.out.println(hasObjectToken() + " " + getObjectToken() + " " + relation.getName());
			return new ComputedUserSetExpression(objectId, relation.getName(), getObjectToken());
		}
		
		return new UnionPredicate()
			.addChild(new ThisExpression(objectId, relation.getName()))
			.addChild(relation.rewrite(objectId));
	}
}
