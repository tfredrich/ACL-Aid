/*
    Copyright 2016, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package com.strategicgains.aclaid;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.strategicgains.aclaid.impl.AggregateCondition;

/**
 * Defines access for a given role and resource. Specifies the permissions allowed for that role/resource combination.
 * If either roleId or resourceId is null, then it is assumed to be a wildcard. If the permission set is empty, then
 * it is assumed to be a wildcard--allowing any permission (otherwise, no grant would be present).
 * 
 * @author toddf
 * @since Feb 22, 2016
 */
public class Grant
{
	private String roleId;
	private String resourceId;
	private Set<String> allowedPermissions = new HashSet<>();
	private Condition condition;

	public Grant(String roleId)
	{
		super();
		this.roleId = roleId;
	}

	public String resourceId()
	{
		return resourceId;
	}

	public Grant resourceId(String resourceId)
	{
		this.resourceId = resourceId;
		return this;
	}

	public Collection<String> allowedPermissions()
	{
		return Collections.unmodifiableCollection(allowedPermissions);
	}

	public Grant allow(String... permissionIds)
	{
		if (permissionIds != null)
		{
			allow(Arrays.asList(permissionIds));
		}

		return this;
	}

	public Grant allow(Collection<String> permissionIds)
	{
		this.allowedPermissions.addAll(permissionIds);
		return this;
	}

	/**
	 * If more-than one condition is added, the conditions are added to an underlying {@link AggregateCondition}.
	 * 
	 * @param condition
	 * @return
	 */
	public Grant withCondition(Condition condition)
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

	public void merge(Grant grant)
	{
		this.allowedPermissions.addAll(grant.allowedPermissions);

		if (grant.hasCondition())
		{
			withCondition(grant.condition);
		}
	}

	public boolean isAllowed(String permissionId)
	{
		if (allowedPermissions.isEmpty()) return true;

		return allowedPermissions.contains(permissionId);
	}

	public String roleId()
	{
		return roleId;
	}

	public boolean isAllowed(AccessControlList acl, Role role, Resource resource, String permissionId)
	{
		if (hasCondition())
		{
			return condition.evaluate(acl, role, resource, permissionId);
		}
		else
		{
			return isAllowed(permissionId);
		}
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{role: ");
		sb.append(roleId != null ? roleId : "*");
		sb.append(", resource: ");
		sb.append(resourceId != null ? resourceId : "*");
		sb.append(", permissions: ");
		sb.append(allowedPermissions.toString());
		sb.append(", hasCondition: ");
		sb.append(hasCondition());
		sb.append("}");
		return sb.toString();
	}

	private boolean hasCondition()
	{
		return (condition != null);
	}
}
