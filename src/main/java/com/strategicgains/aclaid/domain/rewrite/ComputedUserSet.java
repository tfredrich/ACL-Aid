package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.LocalTupleSet;
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
	private String relation;
	private String resourceToken;

	public ComputedUserSet()
	{
		super();
	}

	public ComputedUserSet(String relation)
	{
		this();
		setRelation(relation);
	}

	public ComputedUserSet(String resource, String relation)
	{
		this();
		setResource(resource);
		setRelation(relation);
	}

	protected String getResource()
	{
		return resourceToken;
	}

	public boolean hasResource()
	{
		return (resourceToken != null);
	}

	protected void setResource(String resource)
	{
		this.resourceToken = resource;
	}

	public boolean hasRelation()
	{
		return (relation != null);
	}

	protected void setRelation(String relation)
	{
		this.relation = relation;
	}

	public RewriteRule withRelation(String relation)
	{
		setRelation(relation);
		return this;
	}

	@Override
	public TupleSet expand(TupleSet input, String parentRelation, ObjectId objectId)
	{
		UserSet rewrite = new UserSet(objectId, relation);
		
		if (hasResource() && getResource().startsWith("$"))
		{
			switch(getResource())
			{
				case Tuple.USERSET_OBJECT:
					System.out.println(Tuple.USERSET_OBJECT + " of " + rewrite + " / " + objectId);
					input.stream().findFirst().ifPresent(t -> rewrite.setUserId(t.getUsersetResource()));
					break;
				case Tuple.USERSET_RELATION:
					System.out.println(Tuple.USERSET_RELATION + " of " + rewrite);
					break;
				case Tuple.RELATION:
					System.out.println(Tuple.RELATION + " of " + rewrite);
					break;
				default:
					System.out.println(getResource() + " of " + rewrite);
			}
		}

		return new LocalTupleSet()
			.add(new Tuple(rewrite, parentRelation, objectId));
	}

	@Override
	public boolean check(TupleSet relationTuples, UserSet user, String relation, ObjectId objectId)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
