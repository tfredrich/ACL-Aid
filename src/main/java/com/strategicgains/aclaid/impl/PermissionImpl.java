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

import com.strategicgains.aclaid.Permission;

/**
 * @author toddf
 * @since Feb 24, 2016
 */
public class PermissionImpl
implements Permission
{
	private String permissionId;

	public PermissionImpl()
	{
		super();
	}

	public PermissionImpl(String permissionId)
	{
		this();
		setPermissionId(permissionId);
	}

	@Override
	public String getPermissionId()
	{
		return permissionId;
	}

	public void setPermissionId(String permissionId)
	{
		this.permissionId = permissionId;
	}
}
