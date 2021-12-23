package com.strategicgains.aclaid.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.strategicgains.aclaid.AccessControlList;
import com.strategicgains.aclaid.domain.Grant;
import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.UserSet;
import com.strategicgains.aclaid.exception.InvalidGrantException;

public class GrantBuilder
{
	private List<NamespaceBuilder> namespaces = new ArrayList<>();
	private List<Grant> grants = new ArrayList<>();
	private Grant current;

	public GrantBuilder forResource(String qrn)
	throws ParseException, InvalidGrantException
	{
		current = new Grant();
		grants.add(current);
		return withResource(qrn);
	}

	public GrantBuilder forUserset(String qrn)
	throws ParseException, InvalidGrantException
	{
		current = new Grant();
		grants.add(current);
		return withUserset(qrn);
	}

	public GrantBuilder withResource(String qrn)
	throws ParseException, InvalidGrantException
	{
		if (current.hasResource())
		{
			current = cloneCurrent();
		}

		current.setResource(Resource.parse(qrn));
		return this;
	}

	public GrantBuilder withRelation(String relation)
	throws InvalidGrantException
	{
		if (current.hasRelation())
		{
			current = cloneCurrent();
		}

		current.setRelation(relation);
		return this;
	}

	public GrantBuilder withUserset(String qrn)
	throws ParseException, InvalidGrantException
	{
		if (current.hasUserset())
		{
			current = cloneCurrent();
		}

		current.setUserset(UserSet.parse(qrn));
		return this;
	}

	private Grant cloneCurrent()
	throws InvalidGrantException
	{
		if (!current.isValid()) throw new InvalidGrantException(current.toString());

		Grant g = new Grant(current);
		grants.add(g);
		return g;
	}

	public AccessControlList build()
	{
		AccessControlList m = new AccessControlList();
		grants.stream().forEach(g -> m.withGrant(g));
		return m;
	}

	public NamespaceBuilder namespace(String name)
	{
		NamespaceBuilder b = new NamespaceBuilder(name);
		namespaces.add(b);
		return b;
	}
}
