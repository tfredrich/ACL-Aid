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
import java.util.Map.Entry;
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

	public AccessControlList addRole(Role role, Role... parents)
	throws RoleNotRegisteredException
	{
		if (parents != null)
		{
			return addRole(role.getRoleId(), asStrings(parents));
		}

		return addRole(role);
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
		return allow(new Grant(role).resourceId(resource).allow(permissions));
	}

	public AccessControlList allow(Grant grant)
	throws RoleNotRegisteredException, ResourceNotRegisteredException
	{
		assertRoleRegistered(grant.roleId());
		assertResourceRegistered(grant.resourceId());

		GrantKey grantKey = new GrantKey(grant.roleId(), grant.resourceId());
		Grant g = grants.get(grantKey);

		if (g != null)
		{
			g.merge(grant);
		}
		else
		{
			List<String> p = parents.get(grant.roleId());

			if (p != null)
			{
				for (String parent : p)
				{
					g = _getGrant(parent, grant.resourceId());

					if (g != null) grant.merge(g);
				}
			}

			grants.put(grantKey, grant);		
		}

		recomputeChildGrants(grant);
		return this;
	}

	private void recomputeChildGrants(Grant parentGrant)
	throws RoleNotRegisteredException, ResourceNotRegisteredException
	{

		for (Entry<String, List<String>> entry : parents.entrySet())
		{
			if (entry.getValue().contains(parentGrant.roleId()))
			{
				Grant grant = _getGrant(entry.getKey(), parentGrant.resourceId());

				if (grant != null)
				{
					grant.merge(parentGrant);
				}
				else
				{
					allow(entry.getKey(), parentGrant.resourceId(), parentGrant.allowedPermissions().toArray(new String[0]));
				}
			}
		}
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[roles: ");
		sb.append(roles.toString());
		sb.append(", resources: {");
		sb.append(resources.toString());
		sb.append("}, grants: {");
		grants.values().toString();
		sb.append("}]");
		return sb.toString();
	}

	public boolean isAllowed(Role role, Resource resource, String permissionId)
	{
		String roleId = (role == null ? null : role.getRoleId());
		String resourceId = (resource == null ? null : resource.getResourceId());
		Grant g = getGrant(roleId, resourceId);
		
		if (g == null) return false;

		if (g.isAllowed(this, role, resource, permissionId))
		{
			return true;
		}
		else
		{
			return g.isAllowed(permissionId);
		}
	}

	public boolean isAllowed(String roleId, String permissionId)
	{
		return isAllowed(roleId, null, permissionId);
	}

	public boolean isAllowed(String roleId, String resourceId, String permissionId)
	{
		Grant g = getGrant(roleId, resourceId);

		if (g != null)
		{
			return g.isAllowed(permissionId);
		}

		return false;
	}

	private Grant getGrant(String roleId, String resourceId)
	{
		Grant g = _getGrant(roleId, resourceId);

		if (g != null) return g;

		return _getGrant(roleId, null);
	}

	private Grant _getGrant(String roleId, String resourceId)
	{
		return grants.get(new GrantKey(roleId, resourceId));
	}

	private void assertRolesRegistered(String... roleIds)
	throws RoleNotRegisteredException
	{
		for(String role : roleIds)
		{
			assertRoleRegistered(role);
		}
	}

	private void assertRoleRegistered(String roleId)
	throws RoleNotRegisteredException
	{
		if (!roles.contains(roleId))
		{
			throw new RoleNotRegisteredException(roleId);
		}
	}

	private void assertResourceRegistered(String resourceId)
	throws ResourceNotRegisteredException
	{
		if (resourceId == null) return;

		if (!resources.contains(resourceId))
		{
			throw new ResourceNotRegisteredException(resourceId);
		}
	}

	private String[] asStrings(Role[] parents)
	{
		List<String> s = new ArrayList<String>(parents.length);

		for(Role parent : parents)
		{
			s.add(parent.getRoleId());
		}

		return s.toArray(new String[0]);
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
