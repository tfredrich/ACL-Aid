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

/**
 * For a given principal (via a QualifiedResourceName and acceptable wildcards), defines access for a given set of resources.
 * Specifies the permissions allowed or denied for those resources. May also include a condition that must return true in order
 * for access to be granted.
 * 
 * @author toddf
 * @since Nov 2, 2018
 */
public class PolicyStatement
{
	private ResourceName principal;
	private Set<ResourceName> resources = new HashSet<>();
	private Set<String> allowed = new HashSet<>();
	private Set<String> denied = new HashSet<>();
	private Condition condition;

	public PolicyStatement()
	{
		super();
	}

	public Collection<ResourceName> getResources()
	{
		return resources;
	}

	public PolicyStatement setPrincipal(String qrnString)
	throws ParseException
	{
		return setPrincipal(qrnString != null ? ResourceName.parse(qrnString) : null);
	}

	public PolicyStatement setPrincipal(ResourceName principal)
	{
		this.principal = principal;
		return this;
	}

	public ResourceName getPrincipal()
	{
		return principal;
	}

	public boolean hasPrincipal()
	{
		return (principal != null);
	}

	public PolicyStatement setResource(String... resourceQrns)
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

	public PolicyStatement setResources(Collection<ResourceName> resourceQrns)
	{
		this.resources.addAll(resourceQrns);
		return this;
	}

	public PolicyStatement allow(String... relations)
	{
		return allow(Arrays.asList(relations));
	}

	public PolicyStatement allow(Collection<String> relations)
	{
		this.allowed.addAll(relations);
		return this;
	}

	public PolicyStatement deny(String... relations)
	{
		return deny(Arrays.asList(relations));
	}

	public PolicyStatement deny(Collection<String> relations)
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
	public PolicyStatement withCondition(Condition condition)
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
	public boolean appliesToPrincipal(ResourceName principal)
	{
		if (!hasPrincipal()) return false;

		return getPrincipal().matches(principal);
	}

	/**
	 * If a conditional is present, executes the conditional to determine access. Otherwise, simply looks at the permissions in this policy.
	 * 
	 * @param context
	 * @param permission
	 * @return
	 */
	public boolean evaluate(PolicyContext context, String relation)
	{
		if (context != null &&
			!appliesToPrincipal(context.getPrincipal()) &&
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
