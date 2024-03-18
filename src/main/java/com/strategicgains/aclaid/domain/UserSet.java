package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.Objects;

/**
 * This is the Zanzibar User (which is either a user or a user set) property of a Tuple.
 * It can be a user identifier or a userset, which is a reference to a relation on another
 * resource (e.g. to determine if the user is a member of a group).
 * 
 * ⟨user⟩      ::= ⟨user id⟩ | ⟨userset⟩
 * (user id)   ::= 'user:'(object id)
 * ⟨userset⟩   ::= ⟨object⟩‘#’⟨relation⟩
 * ⟨object⟩    ::= ⟨namespace⟩‘:’⟨object id⟩
 * (namespace) ::= (string)
 * (object id) ::= (string)
 * 
 * Where ⟨namespace⟩ and ⟨relation⟩ are predefined in client configurations.
 * 
 * In other words, a UserSet instance can be a user identifier or a object-relation pair.
 * 
 * For example:
 *  'user:123' is a user identifier.
 *  'groups:group/admin#owner' is a userset.
 *  
 * @author Todd Fredrich
 *
 **/
public class UserSet
{
	public static final UserSet EMPTY = new UserSet();
	public static final String USER_PREFIX = "user";

	private ObjectId objectId;
	private String relation;

	public UserSet()
	{
		super();
	}

	public UserSet(ObjectId userId)
	{
		this(userId, null);
	}

	public UserSet(ObjectId objectId, String relation)
	{
		this();
		setRelation(relation);
		setObjectId(objectId);
	}

	public UserSet(UserSet that)
	{
		this();
		setRelation(that.relation);
		setObjectId(that.objectId);
	}

	public String getNamespace()
	{
		return (hasObjectId() ? objectId.getNamespace() : null);
	}

	public static UserSet parse(String string)
	throws ParseException
	{
		if (string == null || string.isEmpty()) throw new ParseException("Usersets cannot be null or empty", 0);

		String[] segments = string.trim().split("#", 2);

		if (segments.length > 2) throw new ParseException("Invalid userset: " + string, 0);

		
		UserSet userset = new UserSet();
		userset.setObjectId(new ObjectId(segments[0]));

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
		return (hasObjectId() && USER_PREFIX.equals(objectId.getType()));
	}

	protected boolean hasObjectId()
	{
		return (objectId != null);
	}

	public void setObjectId(ObjectId objectId)
	{
		this.objectId = objectId;
	}

	public ObjectId getObjectId()
	{
		return objectId;
	}

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder(objectId.toString());

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
		return Objects.hash(relation, objectId);
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
		return Objects.equals(relation, other.relation) && Objects.equals(objectId, other.objectId);
	}

	public boolean matches(UserSet that)
	{
		if (this.objectId.matches(that.objectId))
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
