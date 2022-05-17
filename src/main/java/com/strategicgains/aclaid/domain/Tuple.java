package com.strategicgains.aclaid.domain;

import java.text.ParseException;

/**
 * This is the Zanzibar Tuple containing a Resource, Relation and User[set].
 * 
 * ⟨tuple⟩   ::= ⟨object⟩‘#’⟨relation⟩‘@’⟨user⟩
 * (relation)::= (relation name)
 * ⟨user⟩    ::= ⟨user id⟩ | ⟨userset⟩
 * (user id) ::= (resource)
 * ⟨userset⟩ ::= ⟨resource⟩‘#’⟨relation⟩
 * ⟨resource⟩::= ⟨namespace⟩‘:’⟨resource id⟩
 * 
 * Would like to be able to answer as many "resource has relation on userset" questions as possible from memory.
 * With small footprint and high performance.
 * 
 * @author tfredrich
 *
 */
public class Tuple
{
	private ResourceName resource;	// 'documents:document/1', 'videos:video/456', 'bat:foobar/8'
	private String relation;		// 'owner', 'viewer', 'member'
	private UserSet userset;		// 'users:user/todd', 'users:group/B#member'

	public Tuple()
	{
		super();
	}

	public Tuple(ResourceName resource, String relation, UserSet userset)
	{
		this();
		setResource(resource);
		setRelation(relation);
		setUserset(userset);
	}

	public Tuple(String resource, String relation, String userset)
	throws ParseException
	{
		this(ResourceName.parse(resource), relation, UserSet.parse(userset));
	}

	public Tuple(Tuple tuple)
	{
		this(tuple.getResource(), tuple.getRelation(), tuple.getUserset());
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
	{
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
		return matches(that.getUserset(), that.getRelation(), that.getResource());
	}

	public boolean matches(UserSet userset, String relation, ResourceName resource)
	{
		return (this.relation.equals(relation)
			&& this.resource.matches(resource)
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
		return String.format("(%s#%s@%s)", resource, relation, userset);
	}

	public boolean isValid()
	{
		return hasResource() && hasUserset() && hasRelation();
	}

	public static Tuple parse(String tuple)
	throws ParseException
	{
		String[] obj = tuple.split("#");

		if (obj.length < 2) throw new ParseException("Invalid tuple resource#relation: " + tuple, 0);

		String[] rel = obj[1].split("@");

		if (rel.length < 2) throw new ParseException("Invalid tuple relation@userset: " + tuple, 0);

		return new Tuple(ResourceName.parse(obj[0]), rel[0], UserSet.parse(rel[1]));
	}
}
