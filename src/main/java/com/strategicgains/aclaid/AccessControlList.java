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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.strategicgains.aclaid.exception.RoleNotRegisteredException;

/**
 * @author toddf
 * @since Feb 22, 2016
 */
public class AccessControlList
{
	private Set<String> roles = new HashSet<>();
	private Set<String> resources = new HashSet<>();
	private Map<String, List<String>> parents = new HashMap<>();
	private Map<GrantKey, Grant> grants = new HashMap<>();

	public AccessControlList role(Role role)
	{
		return role(role.getRoleId());
	}

	public AccessControlList role(String name)
	{
		roles.add(name);
		return this;
	}

	/**
	 * Register a role with inheritance from the parent roles.
	 * 
	 * @param name
	 * @param parents
	 * @return
	 * @throws RoleNotRegisteredException if one or more of the parent roles are not registered already.
	 */
	public AccessControlList role(String name, String... parents)
	throws RoleNotRegisteredException
	{
		assertRolesRegistered(parents);
		this.parents.put(name, Arrays.asList(parents));
		role(name);
		return this;
	}

	public AccessControlList resource(Resource resource)
	{
		return resource(resource.getResourceId());
	}

	public AccessControlList resource(String name)
	{
		resources.add(name);
		return this;
	}

	public AccessControlList allow(String role, String resource, String... permissions)
	{
		GrantKey grantKey = new GrantKey(role, resource);
		Grant g = grants.get(grantKey);

		if (g != null)
		{
			g.allow(permissions);
		}
		else
		{
			grants.put(grantKey, new Grant(role).allow(permissions));		
		}

		return this;
	}

	public boolean isAllowed(Role role, Resource resource, String permission)
	{
		return false;
	}

	public boolean isAllowed(String role, String resource, String permission)
	{
		Grant g = grants.get(new GrantKey(role, resource));

		if (g == null) return false;

		return g.isAllowed(permission);
	}

	private void assertRolesRegistered(String... roles)
	throws RoleNotRegisteredException
	{
		for(String role : roles)
		{
			assertRoleRegistered(role);
		}
	}

	private void assertRoleRegistered(String role)
	throws RoleNotRegisteredException
	{
		if (!roles.contains(role))
		{
			throw new RoleNotRegisteredException(role);
		}
	}

	private class GrantKey
	{
		private String role;
		private String resource;

		public GrantKey(String role, String resource)
		{
			super();
			this.role = role;
			this.resource = resource;
		}

		private String getGrantKey(String role, String resource)
		{
			return String.format("%s||%s", role, resource);
		}

		public String toString()
		{
			if (role != null && resource != null)
			{
				return getGrantKey(role, resource);
			}

			if (role != null && resource == null)
			{
				return getGrantKey(role, "*");
			}

			if (role == null && resource != null)
			{
				return getGrantKey("*", resource);
			}

			return getGrantKey("*", "*");
		}

		@Override
		public boolean equals(Object that)
		{
			if (that == null) return false;

			return (this.toString().equals(that.toString()));
		}

		@Override
		public int hashCode()
		{
			return toString().hashCode();
		}
	}
}
