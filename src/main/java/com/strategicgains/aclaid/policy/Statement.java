/*
    Copyright 2018, Strategic Gains, Inc.
*/
package com.strategicgains.aclaid.policy;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.UserSet;

/**
 * For a given UserSet (via a ResourceName and acceptable wildcards), defines access to a given set of resources (also
 * via ResourceName and possible wildcards). Specifies the relations allowed or denied for those resources. May also
 * include a condition that must return true in order for access to be granted.
 * 
 * @author toddf
 * @since Nov 2, 2018
 */
public class Statement
{
	private UserSet userset;
	private Set<ResourceName> resources = new HashSet<>();

	// Allowed relations
	private Set<String> allowed = new HashSet<>();

	// Denied relations
	private Set<String> denied = new HashSet<>();

	// Arbitrary conditions.
	private Condition condition;

	public Statement()
	{
		super();
	}

	public Collection<ResourceName> getResources()
	{
		return resources;
	}

	public Statement setUserset(String userset)
	throws ParseException
	{
		return setUserset(userset != null ? UserSet.parse(userset) : null);
	}

	public Statement setUserset(UserSet userset)
	{
		this.userset = userset;
		return this;
	}

	public UserSet getUserset()
	{
		return userset;
	}

	public boolean hasUserset()
	{
		return (userset != null);
	}

	public Statement setResource(String... resourceQrns)
	throws ParseException
	{
		if (resourceQrns != null)
		{
			for(String qrn : resourceQrns)
			{
				resources.add(ResourceName.parse(qrn));
			}
		}

		return this;
	}

	public Statement setResources(Collection<ResourceName> resourceQrns)
	{
		this.resources.addAll(resourceQrns);
		return this;
	}

	public Statement allow(String... relations)
	{
		return allow(Arrays.asList(relations));
	}

	public Statement allow(Collection<String> relations)
	{
		this.allowed.addAll(relations);
		return this;
	}

	public Statement deny(String... relations)
	{
		return deny(Arrays.asList(relations));
	}

	public Statement deny(Collection<String> relations)
	{
		this.denied.addAll(relations);
		return this;
	}

	public Collection<String> getAllowed()
	{
		return Collections.unmodifiableCollection(allowed);
	}

	public Collection<String> getDenied()
	{
		return Collections.unmodifiableCollection(denied);
	}

	/**
	 * If more-than one condition is added, the conditions are added to an underlying {@link AggregateCondition}.
	 * If any of the conditions return true, access is granted.
	 * 
	 * @param condition
	 * @return
	 */
	public Statement withCondition(Condition condition)
	{
		if (hasCondition() && this.condition != condition)
		{
			if (AggregateCondition.class.isAssignableFrom(this.condition.getClass()))
			{
				((AggregateCondition) this.condition).add(condition);
			}
			else
			{
				AggregateCondition aa = new AggregateCondition()
					.add(this.condition)
					.add(condition);
				this.condition = aa;
			}
		}
		else
		{
			this.condition = condition;
		}

		return this;
	}

	public boolean appliesToResource(Resource resource)
	{
		return resources.stream().anyMatch(qrn -> qrn.matches(resource.getResourceName()));
	}

	/**
	 * Returns true if this statement is specifically for the given principal. Otherwise returns false.
	 * If the statement does not have a principal associated with it, returns false.
	 * 
	 * @param principal
	 * @return
	 */
	public boolean appliesToUser(UserSet user)
	{
		if (!hasUserset()) return false;

		return getUserset().matches(user);
	}

	/**
	 * If a conditional is present, executes the conditional to determine access. Otherwise, simply looks at the permissions in this policy.
	 * 
	 * @param context
	 * @param relation
	 * @return
	 */
	public boolean evaluate(PolicyContext context, String relation)
	{
		if (context != null &&
			!appliesToUser(context.getUser()) &&
			!appliesToResource(context.getResource()))
		{
			return false;
		}

		if (hasCondition())
		{
			if (condition.test(context, this, relation))
			{
				return isAllowed(relation);
			}

			return false;
		}
		else
		{
			return isAllowed(relation);
		}
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{resources: ");
		sb.append(resources.toString());
		sb.append(", allowed: ");
		sb.append(allowed.toString());
		sb.append(", denied: ");
		sb.append(denied.toString());
		sb.append(", hasCondition: ");
		sb.append(hasCondition());
		sb.append("}");
		return sb.toString();
	}

	private boolean hasCondition()
	{
		return (condition != null);
	}

	/**
	 * Returns true if the permission matches the allowed list and not
	 * the denied list. Does not execute any conditionals, even if present.
	 * 
	 * @param relation Permission to look for in this policy
	 * @return true if the permission is allowed and not denied in the policy.
	 */
	private boolean isAllowed(String relation)
	{
		return allowed.stream().anyMatch(p -> p.equals(relation)) &&
				denied.stream().noneMatch(t -> t.equals(relation));
	}
}
