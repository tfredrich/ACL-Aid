package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the Zanzibar User (which is either a user or a user set).
 * 
 * ⟨user⟩    ::= ⟨user id⟩ | ⟨userset⟩
 * (user id) ::= 'user:'(object_id)
 * ⟨userset⟩ ::= ⟨object⟩‘#’⟨relation⟩ 
 * ⟨object⟩  ::= ⟨namespace⟩‘:’⟨object id⟩
**/
public class UserSet
extends Resource
{
	private static final String REGEX = "^(.*?):(.*?)(?:#(.*?)){0,1}$";
	private static final Pattern PATTERN = Pattern.compile(REGEX);

	private String relation;

	public UserSet()
	{
		super();
	}

	public UserSet(UserSet that)
	{
		super(that);
		setRelation(that.relation);
	}

	public static UserSet parse(String string)
	throws ParseException
	{
		if (string == null || string.isEmpty()) throw new ParseException("Usersets cannot be null or empty", 0);

		Matcher m = PATTERN.matcher(string.trim());

		if (!m.matches()) throw new ParseException("Invalid userset: " + string, 0);

		UserSet userset = new UserSet();
		String namespace = m.group(1);

		if (namespace == null || namespace.isBlank()) throw new ParseException("Usersets must contain a namespace", 0);

		userset.setNamespace(namespace.trim());
		String identifier = m.group(2);

		if (identifier != null && !identifier.isBlank()) userset.setIdentifier(identifier.trim());

		String relation = m.group(3);

		if (relation != null && !relation.isBlank()) userset.setRelation(relation.trim());

		return userset;
	}

	public Resource asResourceReference()
	{
		return new Resource(this);
	}

	protected void setRelation(String relation)
	{
		this.relation = relation;
	}

	public String getRelation()
	{
		return relation;
	}

	public boolean hasRelation()
	{
		return relation != null;
	}

	public String toString()
	{
		StringBuilder s = new StringBuilder(super.toString());

		if (hasRelation())
		{
			s.append("#");
			s.append(relation);
		}

		return s.toString();
	}
}
