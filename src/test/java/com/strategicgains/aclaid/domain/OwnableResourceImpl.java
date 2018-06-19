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
package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.resource.OwnableResource;

/**
 * @author toddf
 * @since Feb 24, 2016
 */
public class OwnableResourceImpl<T>
extends ResourceImpl
implements OwnableResource<T>
{
	private T owner;

	public OwnableResourceImpl()
	{
		super();
	}

	public OwnableResourceImpl(String resourceId)
	{
		super(resourceId);
	}

	public OwnableResourceImpl(T owner)
	{
		this();
		setOwner(owner);
	}

	public OwnableResourceImpl(String resourceId, T owner)
	{
		super(resourceId);
		setOwner(owner);
	}

	@Override
	public T getOwner()
	{
		return owner;
	}

	public void setOwner(T owner)
	{
		this.owner = owner;
	}
}
