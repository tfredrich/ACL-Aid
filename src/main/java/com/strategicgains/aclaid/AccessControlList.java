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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.strategicgains.aclaid.exception.ResourceNotRegisteredException;
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

	public AccessControlList addRole(Role role)
	{
		return addRole(role.getRoleId());
	}

	public AccessControlList addRole(String name)
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
	public AccessControlList addRole(String name, String... parents)
	throws RoleNotRegisteredException
	{
		if (parents != null)
		{
			assertRolesRegistered(parents);
			this.parents.put(name, computeInheritance(parents));
		}

		addRole(name);
		return this;
	}

	private List<String> computeInheritance(String[] parents)
	{
		List<String> inheritance = new ArrayList<>(parents.length);

		for (String parent : parents)
		{
			if (!inheritance.contains(parent))
			{
				inheritance.add(parent);
				List<String> grandParents = this.parents.get(parent);

				if (grandParents != null)
				{
					List<String> gps = computeInheritance(grandParents.toArray(new String[0]));
					addGrandParents(inheritance, gps);
				}
			}
		}

		return inheritance;
	}

	private void addGrandParents(List<String> inheritance, List<String> grandParents)
	{
		for (String gp : grandParents)
		{
			if (!inheritance.contains(gp))
			{
				inheritance.add(gp);
			}
		}
	}

	public AccessControlList addResource(Resource resource)
	{
		return addResource(resource.getResourceId());
	}

	public AccessControlList addResource(String name)
	{
		resources.add(name);
		return this;
	}

	public AccessControlList allow(String role, String resource, String... permissions)
	throws RoleNotRegisteredException, ResourceNotRegisteredException
	{
		assertRoleRegistered(role);
		assertResourceRegistered(resource);

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
		return isAllowed(role.getRoleId(), resource.getResourceId(), permission);
	}

	public boolean isAllowed(String role, String resource, String permission)
	{
		boolean isAllowed = _isAllowed(role, resource, permission);

		if (isAllowed) return true;

		List<String> p = parents.get(role);

		if (p != null)
		{
			for (String parent : p)
			{
				isAllowed = _isAllowed(parent, resource, permission);

				if (isAllowed) return true;
			}
		}

		return false;
	}

	private boolean _isAllowed(String role, String resource, String permission)
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

	private void assertResourceRegistered(String resource)
	throws ResourceNotRegisteredException
	{
		if (!resources.contains(resource))
		{
			throw new ResourceNotRegisteredException(resource);
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

		@Override
		public String toString()
		{
			String oRole = (role == null ? "*" : role);
			String oRes = (resource == null ? "*" : resource);
			return oRole + "||" + oRes;
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
