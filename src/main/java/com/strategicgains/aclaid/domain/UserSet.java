package com.strategicgains.aclaid.domain;

import java.text.ParseException;

/**
 * This is the Zanzibar User (which is either a user or a user set) property of a Tuple.
 * It can be a user identifier or a userset, which is a reference to a relation on another
 * resource (e.g. to determine if the user is a member of a group).
 * 
 * ⟨user⟩    ::= ⟨user id⟩ | ⟨userset⟩
 * (user id) ::= 'user:'(resource id)
 * ⟨userset⟩ ::= ⟨resource⟩‘#’⟨relation⟩
 * ⟨resource⟩::= ⟨namespace⟩‘:’⟨resource id⟩
 * 
 * @author toddfredrich
**/
public class UserSet
{
	private ResourceName resource;
	private String relation;

	public UserSet()
	{
		super();
	}

	public UserSet(UserSet that)
	{
		this();
		setRelation(that.relation);
		setResource(that.resource);
	}

	public static UserSet parse(String string)
	throws ParseException
	{
		if (string == null || string.isEmpty()) throw new ParseException("Usersets cannot be null or empty", 0);

		String[] segments = string.trim().split("#", 2);

		if (segments.length > 2) throw new ParseException("Invalid userset: " + string, 0);

		
		UserSet userset = new UserSet();
		userset.setResource(ResourceName.parse(segments[0]));

		if (segments.length == 2)
		{
			userset.setRelation(segments[1].trim());
		}

		return userset;
	}

	public String getRelation()
	{
		return relation;
	}

	public boolean hasRelation()
	{
		return relation != null;
	}

	protected void setRelation(String relation)
	{
		this.relation = relation;
	}

	protected boolean isUser()
	{
		return !hasRelation();
	}

	protected void setResource(ResourceName resource)
	{
		this.resource = resource;
	}

	public ResourceName getResource()
	{
		return resource;
	}

	public String toString()
	{
		StringBuilder s = new StringBuilder(resource.toString());

		if (hasRelation())
		{
			s.append("#");
			s.append(relation);
		}

		return s.toString();
	}

	public boolean matches(UserSet that)
	{
		if (this.resource.matches(that.resource))
		{
			if (hasRelation())
			{
				return this.relation.equals(that.relation);
			}
			else
			{
				return (!that.hasRelation());
			}
		}

		return false;
	}
}
