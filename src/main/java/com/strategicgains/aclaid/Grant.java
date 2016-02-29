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
import java.util.HashSet;
import java.util.Set;

/**
 * Defines access for a given role and resource. Specifies the permissions allowed for that role/resource combination.
 * If either roleId or resourceId is null, then it is assumed to be a wildcard. If the permission set is empty, then
 * it is assumed to be a wildcard--allowing any permission.
 * 
 * @author toddf
 * @since Feb 22, 2016
 */
public class Grant
{
	private String roleId;
	private String resourceId;
	private Set<String> allowedPermissions = new HashSet<>();
	private Assertion assertion;

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

	public Grant withAssertion(Assertion assertion)
	{
		this.assertion = assertion;
		return this;
	}

	public void mergePermissions(Grant grant)
	{
		this.allowedPermissions.addAll(grant.allowedPermissions);
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

	public boolean isAllowed(Resource resource, String permissionId)
	{
		boolean isAllowed = isAllowed(permissionId);

		if (isAllowed && hasAssertion())
		{
			return assertion.isAllowed(roleId, resource, permissionId);
		}

		return isAllowed;
	}

	private boolean hasAssertion()
	{
		return (assertion != null);
	}
}
