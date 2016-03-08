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
import java.util.List;

/**
 * An assertion that contains multiple assertions.
 * 
 * @author toddf
 * @since Mar 8, 2016
 */
public class AssertionAggregate
implements Assertion
{
	private List<Assertion> assertions = new ArrayList<>();

	/**
	 * Makes the assertion, calling the aggregated assertions, returning after the first one that returns true.
	 * Otherwise returns false.
	 */
	public boolean isAllowed(AccessControlList acl, Role role, Resource resource, String permissionId)
	{
		for (Assertion assertion : assertions)
		{
			if (assertion.isAllowed(acl, role, resource, permissionId)) return true;
		}

		return false;
	}

	/**
	 * Add an assertion to this aggregate. Will not add the same assertion (by reference) more-than once.
	 * 
	 * @param assertion
	 * @return this instance for method chaining.
	 */
	public AssertionAggregate add(Assertion assertion)
	{
		if (!assertions.contains(assertion))
		{
			assertions.add(assertion);
		}

		return this;
	}
}
