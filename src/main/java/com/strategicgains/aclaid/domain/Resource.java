package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the Zanzibar Resource property of a Tuple.
 * 
 * ⟨resource⟩::= ⟨namespace⟩‘:’⟨resource id⟩
 * 
 * @author toddfredrich
 *
 */
public class Resource
{
	private static final String REGEX = "^(.*?):(.*?)$";
	private static final Pattern PATTERN = Pattern.compile(REGEX);
	protected static final String WILDCARD = "*";

	private String namespace;
	private String identifier;

	public Resource()
	{
		super();
	}

	public Resource(Resource that)
	{
		this();
		setIdentifier(that.identifier);
		setNamespace(that.namespace);
	}

	public static Resource parse(String path)
	throws ParseException
	{
		if (path == null || path.isEmpty()) throw new ParseException("Resource cannot be null or empty", 0);

		Matcher m = PATTERN.matcher(path.trim());

		if (!m.matches()) throw new ParseException("Invalid resource: " + path, 0);

		Resource resource = new Resource();
		String namespace = m.group(1);

		if (namespace == null || namespace.isBlank()) throw new ParseException("Resource must contain a namespace", 0);

		resource.setNamespace(namespace.trim());
		String identifier = m.group(2);

		if (identifier != null && !identifier.isBlank()) resource.setIdentifier(identifier.trim());

		return resource;
	}

	protected void setIdentifier(String id)
	{
		this.identifier = id;
	}

	protected void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}

	public boolean matches(Resource that)
	{
		return ((this.namespace.equals(that.namespace) || this.namespace.equals(WILDCARD))
			&& (this.identifier.equals(that.identifier) || this.identifier.equals(WILDCARD)));
	}

	public String toString()
	{
		return String.format("%s:%s", namespace, identifier);
	}
}
