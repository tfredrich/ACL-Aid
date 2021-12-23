package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public boolean matches(UserSet resource)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public static UserSet parse(String path)
	throws ParseException
	{
		if (path == null || path.isEmpty()) throw new ParseException("Usersets cannot be null or empty", 0);

		Matcher m = PATTERN.matcher(path.trim());

		if (!m.matches()) throw new ParseException("Invalid userset: " + path, 0);

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

	public boolean hasRelation()
	{
		return relation != null;
	}
}
