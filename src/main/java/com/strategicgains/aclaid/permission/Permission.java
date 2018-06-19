/*
    Copyright 2018, Strategic Gains, Inc.

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
package com.strategicgains.aclaid.permission;

import java.text.ParseException;

/**
 * @author toddf
 * @since Jun 18, 2018
 */
public interface Permission<T>
{
	public String getPermissionId();
	public boolean matches(String permissionId) throws ParseException;
	public boolean matches(T that);
}
