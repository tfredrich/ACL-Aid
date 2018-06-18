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
package com.strategicgains.aclaid.builder;

import java.util.ArrayList;
import java.util.List;

import com.strategicgains.aclaid.Condition;
import com.strategicgains.aclaid.Grant;
import com.strategicgains.aclaid.Permission;
import com.strategicgains.aclaid.Resource;
import com.strategicgains.aclaid.Role;

/**
 * @author toddf
 * @since Feb 25, 2016
 */
public class GrantBuilder
{
	private Grant grant;

	public GrantBuilder role(Role role)
	{
		return role(role.getRoleId());
	}

	public GrantBuilder role(String roleId)
	{
		grant = new Grant(roleId);
		return this;
	}

	public GrantBuilder resource(Resource resource)
	{
		return resource(resource.getResourceId());
	}

	public GrantBuilder resource(String resourceId)
	{
		grant.resourceId(resourceId);
		return this;
	}

	public GrantBuilder permissions(Permission... permissions)
	{
		return permissions(asStrings(permissions));
	}

	public GrantBuilder permissions(String... permissionIds)
	{
		grant.allow(permissionIds);
		return this;
	}

	public GrantBuilder withAssertion(Condition assertion)
	{
		grant.withCondition(assertion);
		return this;
	}

	public Grant build()
	{
		return grant;
	}

	private String[] asStrings(Permission[] permissions)
	{
		List<String> strings = new ArrayList<>(permissions.length);

		for (Permission permission : permissions)
		{
			strings.add(permission.toString());
		}

		return strings.toArray(new String[0]);
	}
}
