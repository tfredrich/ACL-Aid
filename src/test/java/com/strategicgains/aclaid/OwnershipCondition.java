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

import com.strategicgains.aclaid.impl.RoleImpl;

/**
 * @author toddf
 * @since Mar 13, 2016
 */
public class OwnershipCondition
implements Condition
{
	@SuppressWarnings("unchecked")
	@Override
	public boolean isMet(AccessControlList acl, Role role, Resource resource, String permissionId)
	{
		if ("guest".equals(role.getRoleId()) && RoleImpl.class.isAssignableFrom(role.getClass()))
		{
			RoleImpl<String> principal = (RoleImpl<String>) role;

			if (principal != null && OwnableResource.class.isAssignableFrom(resource.getClass()))
			{
				if (principal.getId().equals(((OwnableResource<String>) resource).getOwner()))
				{
					return acl.isAllowed("owner", resource.getResourceId(), permissionId);
				}
			}
		}

		return acl.isAllowed(role.getRoleId(), resource.getResourceId(), permissionId);
	}
}
