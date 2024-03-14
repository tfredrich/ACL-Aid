package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.Objects;

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
	public static final UserSet EMPTY = new UserSet();

	private ObjectId userId;
	private String relation;

	public UserSet()
	{
		super();
	}

	public UserSet(ObjectId resource)
	{
		this(resource, null);
	}

	public UserSet(ObjectId userId, String relation)
	{
		this();
		setRelation(relation);
		setUserId(userId);
	}

	public UserSet(UserSet that)
	{
		this();
		setRelation(that.relation);
		setUserId(that.userId);
	}

	public String getNamespace()
	{
		return (hasUserId() ? userId.getNamespace() : null);
	}

	public static UserSet parse(String string)
	throws ParseException
	{
		if (string == null || string.isEmpty()) throw new ParseException("Usersets cannot be null or empty", 0);

		String[] segments = string.trim().split("#", 2);

		if (segments.length > 2) throw new ParseException("Invalid userset: " + string, 0);

		
		UserSet userset = new UserSet();
		userset.setUserId(new ObjectId(segments[0]));

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

	public void setRelation(String relation)
	{
		this.relation = relation;
	}

	protected boolean isUser()
	{
		return !hasRelation();
	}

	protected boolean hasUserId()
	{
		return (userId != null);
	}

	public void setUserId(ObjectId userId)
	{
		this.userId = userId;
	}

	public ObjectId getUserId()
	{
		return userId;
	}

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder(userId.toString());

		if (hasRelation())
		{
			s.append("#");
			s.append(relation);
		}

		return s.toString();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(relation, userId);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSet other = (UserSet) obj;
		return Objects.equals(relation, other.relation) && Objects.equals(userId, other.userId);
	}

	public boolean matches(UserSet that)
	{
		if (this.userId.matches(that.userId))
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
