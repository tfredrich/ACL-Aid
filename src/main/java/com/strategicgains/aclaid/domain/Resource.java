package com.strategicgains.aclaid.domain;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Resource
{
	private static final String REGEX = "^(.*?):(.*?)$";
	private static final Pattern PATTERN = Pattern.compile(REGEX);

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
		if (path == null || path.isEmpty()) throw new ParseException("Resource references cannot be null or empty", 0);

		Matcher m = PATTERN.matcher(path.trim());

		if (!m.matches()) throw new ParseException("Invalid resource reference: " + path, 0);

		Resource resource = new Resource();
		String namespace = m.group(1);

		if (namespace == null || namespace.isBlank()) throw new ParseException("Resource path must contain a namespace", 0);

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

	public boolean matches(Resource resource)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
