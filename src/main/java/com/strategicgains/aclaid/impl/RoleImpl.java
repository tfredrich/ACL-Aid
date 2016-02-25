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
package com.strategicgains.aclaid.impl;

import com.strategicgains.aclaid.Role;

/**
 * @author toddf
 * @since Feb 24, 2016
 */
public class RoleImpl
implements Role
{
	private String roleId;

	public RoleImpl()
	{
		super();
	}

	public RoleImpl(String roleId)
	{
		this();
		setRoleId(roleId);
	}

	@Override
	public String getRoleId()
	{
		return roleId;
	}

	public void setRoleId(String roleId)
	{
		this.roleId = roleId;
	}
}
