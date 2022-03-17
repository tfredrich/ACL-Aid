package com.strategicgains.aclaid.domain;

import java.text.ParseException;

/**
 * This is the Zanzibar Tuple containing a Resource, Relation and User[set].
 * 
 * ⟨tuple⟩   ::= ⟨object⟩‘#’⟨relation⟩‘@’⟨user⟩
 * ⟨resource⟩::= ⟨namespace⟩‘:’⟨resource id⟩
 * (relation)::= (relation name)
 * ⟨user⟩    ::= ⟨user id⟩ | ⟨userset⟩
 * (user id) ::= 'user:'(resource id)
 * ⟨userset⟩ ::= ⟨resource⟩‘#’⟨relation⟩
 * 
 * Would like to be able to answer as many "resource has relation on userset" questions as possible from memory.
 * With small footprint and high performance.
 * 
 * @author toddfredrich
 *
 */
public class Tuple
{
	private Resource resource;	// 'video xyz', 'groupB'
	private String relation;	// 'owner', 'viewer', 'member'
	private UserSet userset;	// 'todd', 'groupB#member'

	public Tuple()
	{
		super();
	}

	public Tuple(Resource resource, String relation, UserSet userset)
	{
		this();
		setResource(resource);
		setRelation(relation);
		setUserset(userset);
	}

	public Tuple(String resource, String relation, String userset)
	throws ParseException
	{
		this(Resource.parse(resource), relation, UserSet.parse(userset));
	}

	public Tuple(Tuple tuple)
	{
		this(tuple.getResource(), tuple.getRelation(), tuple.getUserset());
	}

	public boolean hasResource()
	{
		return (resource != null);
	}

	public Resource getResource()
	{
		return new Resource(resource);
	}

	public void setResource(Resource resource)
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

	public boolean matches(UserSet userset, String relation, Resource resource)
	{
		return (this.relation.equals(relation)
			&& this.resource.matches(resource)
			&& this.userset.matches(userset));
	}

	public boolean applies(Resource resource)
	{
		return this.resource.matches(resource);
	}

	public boolean applies(Resource resource, String relation)
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
		return String.format("%s#%s@%s)", resource, relation, userset);
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

		return new Tuple(Resource.parse(obj[0]), rel[0], UserSet.parse(rel[1]));
	}
}
