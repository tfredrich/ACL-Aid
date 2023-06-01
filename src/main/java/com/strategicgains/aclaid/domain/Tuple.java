package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.Date;

import com.strategicgains.aclaid.exception.InvalidTupleException;

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
 * ⟨tuple⟩   ::= ⟨resource⟩‘#’⟨relation⟩‘@’⟨user⟩
 * (relation)::= (relation name)
 * ⟨user⟩    ::= ⟨user id⟩ | ⟨userset⟩
 * (user id) ::= (resource)
 * ⟨userset⟩ ::= ⟨resource⟩‘#’⟨relation⟩
 * ⟨resource⟩  ::= ⟨namespace⟩‘:’⟨resource id⟩
 * 
 * Where 'object' becomes 'resource' and a user ID is also a namespaced, fully-qualified resource.
 * 
 * Would like to be able to answer as many "resource has relation on userset" questions as possible from memory.
 * With small footprint and high performance.
 * 
 * @author tfredrich
 */
public class Tuple
{
	private ResourceName resource;	// 'documents:document/1', 'videos:video/456', 'bat:foobar/8'
	private String relation;		// 'owner', 'viewer', 'member'
	private UserSet userset;		// 'users:user/todd', 'users:group/B#member'
	private Date expiresAt;

	public Tuple()
	{
		super();
	}

	public Tuple(UserSet userset, String relation, ResourceName resource)
	throws InvalidTupleException
	{
		this();
		setResource(resource);
		setRelation(relation);
		setUserset(userset);
	}

	public Tuple(String userset, String relation, String resource)
	throws ParseException, InvalidTupleException
	{
		this(UserSet.parse(userset), relation, new ResourceName(resource));
	}

	public Tuple(Tuple tuple)
	throws InvalidTupleException
	{
		this(tuple.getUserset(), tuple.getRelation(), tuple.getResource());
	}

	public boolean expires()
	{
		return (expiresAt != null);
	}

	public Date getExpiresAt()
	{
		return (expires() ? new Date(expiresAt.getTime()) : null);
	}

	public void setExpiresAt(Date expiresAt)
	{
		this.expiresAt = (expiresAt != null ? new Date(expiresAt.getTime()) : null);
	}

	public boolean hasResource()
	{
		return (resource != null);
	}

	public ResourceName getResource()
	{
		return resource;
	}

	public void setResource(ResourceName resource)
	throws InvalidTupleException
	{
		if (resource.isWildcard())
		{
			throw new InvalidTupleException("Wildcard resources not permitted in tuples: " + resource.toString());
		}

		this.resource = resource;
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
	throws InvalidTupleException
	{
		if (userset.getResource().isWildcard()) throw new InvalidTupleException("Wildcard usersets not permitted in tuples: " + userset.toString());
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
		return matches(that.getUserset(), that.getRelation(), that.getResource());
	}

	public boolean matches(UserSet userset, String relation, ResourceName object)
	{
		return (this.relation.equals(relation)
			&& this.resource.matches(object)
			&& this.userset.matches(userset));
	}

	public boolean applies(ResourceName resource)
	{
		return this.resource.matches(resource);
	}

	public boolean applies(ResourceName resource, String relation)
	{
		return (this.relation.equals(relation)
			&& this.resource.matches(resource));
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
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

		if (resource == null)
		{
			if (other.resource != null) return false;
		}
		else if (!resource.equals(other.resource)) return false;

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
		if (expires()) return String.format("(%s@%s#%s|%d)", userset, relation, resource, expiresAt.getTime());
		return String.format("(%s@%s#%s)", userset, relation, resource);
	}

	public String toZanzibar()
	{
		return String.format("(%s#%s@%s)", resource, relation, userset);
	}

	public boolean isValid()
	{
		return hasResource() && hasUserset() && hasRelation();
	}

	/*
	 * Parses an ACL-AID Tuple of the form:
	 * User[#relation]@relation#resource
	 * User[set]@relation#resource
	 */
	public static Tuple parse(String tuple)
	throws ParseException, InvalidTupleException
	{
		String[] obj = tuple.split("@");

		if (obj.length < 2) throw new ParseException("Invalid tuple userset@relation: " + tuple, 0);

		String[] rel = obj[1].split("#");

		if (rel.length < 2) throw new ParseException("Invalid tuple relation#resource: " + tuple, 0);

		return new Tuple(UserSet.parse(obj[0]), rel[0], new ResourceName(rel[1]));
	}

	/*
	 * Parses a Zanzibar Tuple of the form:
	 * resource#relation@user[#relation]
	 * resource#relation@user[set]
	 */
	public static Tuple parseZanzibar(String tuple)
	throws ParseException, InvalidTupleException
	{
		String[] obj = tuple.split("#");

		if (obj.length < 2) throw new ParseException("Invalid tuple resource#relation: " + tuple, 0);

		String[] rel = obj[1].split("@");

		if (rel.length < 2) throw new ParseException("Invalid tuple relation@userset: " + tuple, 0);

		return new Tuple(UserSet.parse(rel[1]), rel[0], new ResourceName(obj[0]));
	}
}
