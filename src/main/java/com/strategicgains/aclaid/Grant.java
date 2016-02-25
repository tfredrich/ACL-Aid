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
 * @author toddf
 * @since Feb 22, 2016
 */
public class Grant
{
	private String role;
	private Set<String> allowedPermissions = new HashSet<>();
	private Assertion assertion;

	public Grant(Role role)
	{
		this(role.getRoleId());
	}

	public Grant(String role)
	{
		super();
		this.role = role;
	}

	public Grant allow(String... permissions)
	{
		if (permissions != null)
		{
			allow(Arrays.asList(permissions));
		}

		return this;
	}

	public Grant allow(Collection<String> permissions)
	{
		this.allowedPermissions.addAll(permissions);
		return this;
	}

	public Grant allow(String permission)
	{
		if (permission != null)
		{
			this.allowedPermissions.add(permission);
		}

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

	public boolean isAllowed(String permission)
	{
		if (allowedPermissions.isEmpty()) return true;

		return allowedPermissions.contains(permission);
	}

	public String role()
	{
		return role;
	}

	public boolean isAllowed(Resource resource, String permission)
	{
		boolean isAllowed = isAllowed(permission);

		if (isAllowed && hasAssertion())
		{
			return assertion.isAllowed(role, resource, permission);
		}

		return isAllowed;
	}

	private boolean hasAssertion()
	{
		return (assertion != null);
	}
}
