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
		setObjectId(objectId);
		setRelation(relation);
	}

	public UserSet(UserSet that)
	{
		setObjectId(that.getObjectId());
		setRelation(that.relation);
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

	public boolean isObject()
	{
		return (hasObjectId() && !hasRelation());
	}

	public boolean isGroup() {
		return (hasObjectId() && hasRelation());
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

	public String getNamespace()
	{
		return (hasObjectId() ? objectId.getNamespace() : null);
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
		StringBuilder s = new StringBuilder();

		if (hasObjectId()) {
			s.append(objectId);
		}

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
		if (hasRelation()) {
			return Objects.hash(relation, getObjectId());
		}
		else return Objects.hash(getObjectId());
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
		return (Objects.equals(relation, other.relation)
			&& Objects.equals(objectId, other.objectId));
	}

	public boolean matches(ObjectId objectId, String relation)
	{
		if (this.hasObjectId() && this.objectId.matches(objectId))
		{
			if (hasRelation())
			{
				return this.relation.equals(relation);
			}
			else
			{
				return (relation == null);
			}
		}

		return false;
	}

	public boolean matches(UserSet that)
	{
		return matches(that.objectId, that.relation);
	}
}
