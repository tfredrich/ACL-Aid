package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.Objects;

/**
 * This is the user ID portion of the Zanzibar UserSet property of a Tuple.
 * 
 * ⟨user⟩      ::= ⟨object⟩ | ⟨userset⟩
 * ⟨object⟩    ::= ⟨namespace⟩‘:’⟨object id⟩
 * (namespace) ::= (string)
 * (object id) ::= (string)
 *  
 * @author Todd Fredrich
 * see: {@link UserSet}
 **/
public class Actor
{
	private ObjectId objectId;

	public Actor()
	{
		super();
	}

	public Actor(ObjectId objectId)
	{
		this();
		setObjectId(objectId);
	}

	public Actor(Actor that)
	{
		this();
		setObjectId(that.objectId);
	}

	public String getNamespace()
	{
		return (hasObjectId() ? objectId.getNamespace() : null);
	}

	public static Actor parse(String string)
	throws ParseException
	{
		if (string == null || string.isEmpty()) throw new ParseException("Actors cannot be null or empty", 0);

		String[] segments = string.trim().split("#", 2);

		if (segments.length > 1) throw new ParseException("Invalid actor: " + string, 0);

		
		Actor user = new Actor();
		user.setObjectId(new ObjectId(segments[0]));

		return user;
	}

	protected boolean isActor()
	{
		return true;
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
		Actor other = (Actor) obj;
		return Objects.equals(objectId, other.objectId);
	}

	public boolean matches(Actor that)
	{
		return this.objectId.matches(that.objectId);
	}
}
