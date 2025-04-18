package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.Date;

/**
 * This is the Zanzibar Tuple containing an Object, Relation and User[set].
 * 
 * From the Zanzibar paper:
 * ⟨tuple⟩ ::= ⟨object⟩‘#’⟨relation⟩‘@’⟨user⟩
 * ⟨object⟩ ::= ⟨namespace⟩‘:’⟨object id⟩
 * ⟨user⟩ ::= ⟨user id⟩ | ⟨userset⟩
 * ⟨userset⟩ ::= ⟨object⟩‘#’⟨relation⟩
 * 
 * Where ⟨namespace⟩ and ⟨relation⟩ are predefined in client configurations (§2.3),
 * ⟨object id⟩ is a string, and
 * ⟨user id⟩ is an integer.
 * 
 * The primary keys required to identify a relation tuple are ⟨namespace⟩, ⟨object id⟩, ⟨relation⟩, and ⟨user⟩. 
 * One feature worth noting is that a ⟨userset⟩ allows ACLs to refer to groups and thus supports representing nested group membership.
 * 
 * We redefine subtly here to use the following vocabulary instead:
 * ⟨tuple⟩     ::= ⟨object⟩‘#’⟨relation⟩‘@’⟨user⟩
 * ⟨object⟩    ::= ⟨namespace⟩‘:’⟨object id⟩
 * ⟨user⟩      ::= ⟨object⟩ | ⟨userset⟩
 * ⟨userset⟩   ::= ⟨object⟩‘#’⟨relation⟩
 * (object id) ::= (string)
 * 
 * @author Todd Fredrich
 */
public class Tuple
{
	/**
	 * The objectId on the tuple userset.
	 */
	public static final String USERSET_OBJECT = "$TUPLE_USERSET_OBJECT";

	/**
	 * The relation on the tuple userset.
	 */
	public static final String USERSET_RELATION = "$TUPLE_USERSET_RELATION";

	/**
	 * The relation on the tuple (as opposed to the relation on the tuple userset).
	 */
	public static final String RELATION = "$TUPLE_RELATION";

	/**
	 * The resource (or object) on which the UserSet has a relation.
	 * Examples are: 'documents:document/1', 'groups:group/B', 'bat:foobar/345'
	 */
	private ObjectId objectId;

	/**
	 * The relationship being granted, such as 'owner', 'viewer', 'member'
	 */
	private String relation;

	/**
	 * The UserSet being granted the relationship to the resource.
	 * Examples: 'users:user/todd', 'groups:group/B#member'
	 */
	private UserSet userset;

	/**
	 * Optional. If this is a temporal, expiring tuple then this is when the tuple expires or becomes
	 * ineffective and is no longer applicable. After the expiration time, the tuple is available for
	 * garbage collection (or otherwise removal).
	 */
	private Date expiresAt;

	public Tuple()
	{
		super();
	}

	public Tuple(UserSet userset, String relation, ObjectId objectId)
	{
		this();
		setObjectId(objectId);
		setRelation(relation);
		setUserset(userset);
	}

	public Tuple(String userset, String relation, String resource)
	throws ParseException
	{
		this(UserSet.parse(userset), relation, new ObjectId(resource));
	}

	public Tuple(Tuple tuple)
	{
		this(tuple.getUserset(), tuple.getRelation(), tuple.getObjectId());
	}

	public boolean expires()
	{
		return (expiresAt != null);
	}

	public boolean isExpired()
	{
		return (expires() && expiresAt.getTime() < System.currentTimeMillis());
	}

	public Date getExpiresAt()
	{
		return (expires() ? new Date(expiresAt.getTime()) : null);
	}

	public void setExpiresAt(Date expiresAt)
	{
		this.expiresAt = (expiresAt != null ? new Date(expiresAt.getTime()) : null);
	}

	public boolean hasObjectId()
	{
		return (objectId != null);
	}

	public ObjectId getObjectId()
	{
		return objectId;
	}

	public void setObjectId(ObjectId objectId)
	{
		this.objectId = objectId;
	}

	public boolean hasUserset()
	{
		return (userset != null);
	}

	public UserSet getUserset()
	{
		return new UserSet(userset);
	}

	public void setUserset(UserSet userset)
	{
		this.userset = userset;
	}

	public boolean hasRelation()
	{
		return (relation != null && !relation.isEmpty());
	}

	public String getRelation()
	{
		return relation;
	}

	public void setRelation(String relation)
	{
		this.relation = relation;
	}

	public boolean matches(Tuple that)
	{
		return matches(that.getUserset(), that.getRelation(), that.getObjectId());
	}

	public boolean matches(UserSet userset, String relation, ObjectId objectId)
	{
		return (this.relation.equals(relation)
			&& this.objectId.matches(objectId)
			&& this.userset.matches(userset));
	}

	public boolean appliesTo(ObjectId objectId)
	{
		return this.objectId.matches(objectId);
	}

	public boolean appliesTo(ObjectId objectId, String relation)
	{
		return (this.relation.equals(relation)
			&& this.objectId.matches(objectId));
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objectId == null) ? 0 : objectId.hashCode());
		result = prime * result + ((relation == null) ? 0 : relation.hashCode());
		result = prime * result + ((userset == null) ? 0 : userset.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;

		if (obj == null) return false;

		if (getClass() != obj.getClass()) return false;

		Tuple other = (Tuple) obj;

		if (objectId == null)
		{
			if (other.objectId != null) return false;
		}
		else if (!objectId.equals(other.objectId)) return false;

		if (relation == null)
		{
			if (other.relation != null) return false;
		}
		else if (!relation.equals(other.relation)) return false;

		if (userset == null)
		{
			if (other.userset != null) return false;
		}
		else if (!userset.equals(other.userset)) return false;

		return true;
	}

	@Override
	public String toString()
	{
		if (expires()) return String.format("(%s@%s#%s|%d)", userset, relation, objectId, expiresAt.getTime());
		return String.format("(%s@%s#%s)", userset, relation, objectId);
	}

	public String toZanzibar()
	{
		return String.format("(%s#%s@%s)", objectId, relation, userset);
	}

	public boolean isValid()
	{
		return hasObjectId() && hasUserset() && hasRelation();
	}

	/*
	 * Parses an ACL-AID Tuple of the form:
	 * User[#relation]@relation#resource
	 * User[set]@relation#resource
	 */
	public static Tuple parse(String tuple)
	throws ParseException
	{
		String[] obj = tuple.split("@");

		if (obj.length < 2) throw new ParseException("Invalid tuple userset@relation: " + tuple, 0);

		String[] rel = obj[1].split("#");

		if (rel.length < 2) throw new ParseException("Invalid tuple relation#resource: " + tuple, 0);

		return new Tuple(UserSet.parse(obj[0]), rel[0], new ObjectId(rel[1]));
	}

	/*
	 * Parses a Zanzibar Tuple of the form:
	 * resource#relation@user[#relation]
	 * resource#relation@user[set]
	 */
	public static Tuple parseZanzibar(String tuple)
	throws ParseException
	{
		String[] obj = tuple.split("#");

		if (obj.length < 2) throw new ParseException("Invalid tuple resource#relation: " + tuple, 0);

		String[] rel = obj[1].split("@");

		if (rel.length < 2) throw new ParseException("Invalid tuple relation@userset: " + tuple, 0);

		return new Tuple(UserSet.parse(rel[1]), rel[0], new ObjectId(obj[0]));
	}

	public String getUsersetRelation()
	{
		return (hasUserset() ? getUserset().getRelation() : null);
	}

	public ObjectId getUsersetResource()
	{
		return (hasUserset() ? getUserset().getObjectId() : null);
	}

	public boolean isDirectRelation()
	{
		return userset.isObject();
	}
}
