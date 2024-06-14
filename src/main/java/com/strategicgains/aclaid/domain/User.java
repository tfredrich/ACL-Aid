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
public class User
{
	public static final String USER_PREFIX = "user";

	private ObjectId objectId;

	public User()
	{
		super();
	}

	public User(ObjectId objectId)
	{
		this();
		setObjectId(objectId);
	}

	public User(User that)
	{
		this();
		setObjectId(that.objectId);
	}

	public String getNamespace()
	{
		return (hasObjectId() ? objectId.getNamespace() : null);
	}

	public static User parse(String string)
	throws ParseException
	{
		if (string == null || string.isEmpty()) throw new ParseException("Users cannot be null or empty", 0);

		String[] segments = string.trim().split("#", 2);

		if (segments.length > 1) throw new ParseException("Invalid user: " + string, 0);

		
		User user = new User();
		user.setObjectId(new ObjectId(segments[0]));

		return user;
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
		return objectId.toString();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(objectId);
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
		User other = (User) obj;
		return Objects.equals(objectId, other.objectId);
	}

	public boolean matches(User that)
	{
		return this.objectId.matches(that.objectId);
	}
}
