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
import java.util.Objects;

/**
 * <p>A Permission is a simple string which defines an action that can be performed on a resource.
 * Permissions are namespaced to eliminate name clashes across services. The primary property on
 * the Permission is 'action'. The namespace + action (in the form "namespace:action") is used directly
 * in Policy definitions.</p>
 * 
 * <p>To support wildcard specifications in Policy definitions, an asterisk ('*') can be used in the 'action'
 * property to mean 'all actions'. In addition, since most people (e.g. developers) think of permissions in terms
 * of an action on a particular resource type, Permission supports the concept of a resource classify (or type).</p>
 * 
 * @author tfredrich
 * @since 13 Mar 2017
 */
public class PermissionImpl
implements Permission
{
	private static final String DELIMITER = ":";
	private static final String WILDCARD = "*";

	private String namespace;
	private String action;
	private String classifier;

	public PermissionImpl(String namespace, String action, String classifier)
	{
		super();
		this.namespace = namespace;
		this.action = action;
		this.classifier = classifier;
	}

	public static PermissionImpl parse(String permission)
	throws ParseException
	{
		if (permission == null) throw new ParseException("Permission strings cannot be null", 0);

		String[] parts = permission.split(DELIMITER);

		if (parts.length > 3) throw new ParseException("Permission strings have at most three (3) segments", 0);
		if (parts.length < 2) throw new ParseException("Permission strings have at least two (2) segments", 0);

		return (parts.length == 3 ? new PermissionImpl(parts[0], parts[1], parts[2]) : new PermissionImpl(parts[0], parts[1], null));
	}

	@Override
	public String getPermissionId()
	{
		return toString();
	}

	public String getNamespace()
	{
		return namespace;
	}

	public String getAction()
	{
		return action;
	}

	public boolean isActionWildcard()
	{
		return getAction().equals(WILDCARD);
	}

	public boolean isClassifierWildcard()
	{
		return (hasClassifier() && getClassifier().equals(WILDCARD)) || (!hasClassifier() && isActionWildcard());
	}

	public boolean containsWildcard()
	{
		return isActionWildcard() || isClassifierWildcard();
	}

	public boolean isValid(boolean isWildcardValid)
	{
		if (namespace == null || namespace.isEmpty()) return false;
		if (action == null || action.isEmpty()) return false;

		if (!isWildcardValid)
		{
			return !containsWildcard();
		}

		return true;
	}

	public String getClassifier()
	{
		return classifier;
	}

	public boolean hasClassifier()
	{
		return classifier != null;
	}

	@Override
	public int hashCode()
	{
		int code = this.getClass().hashCode();
		if (namespace != null) code *= namespace.hashCode();
		if (action != null) code *= action.hashCode();
		if (hasClassifier()) code *= classifier.hashCode();
		return code;
	}

	@Override
	public boolean equals(Object that)
	{
		if (that == null) return false;
		if (PermissionImpl.class.isAssignableFrom(that.getClass()));

		return equals((PermissionImpl) that);
	}

	public boolean equals(PermissionImpl that)
	{
		if (that == null) return false;

		return (Objects.equals(this.action, that.action)
			&& Objects.equals(this.namespace, that.namespace)
			&& Objects.equals(this.classifier, that.classifier));
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(getNamespace());
		sb.append(DELIMITER);
		sb.append(getAction());
		sb.append(DELIMITER);
		sb.append(getClassifier());
		return sb.toString();
	}

	public boolean matches(String permissionString)
	throws ParseException
	{
		return matches(parse(permissionString));
	}

	@Override
	public boolean matches(Permission permission)
	{
		return matches((PermissionImpl) permission);
	}
	
	public boolean matches(PermissionImpl that)
	{
		if (that == null) return false;
		if (!this.getNamespace().equals(that.getNamespace())) return false;

		boolean actionMatches = (this.getAction().equals(that.getAction())
			|| this.isActionWildcard()
			|| that.isActionWildcard());

		if (!actionMatches) return false;

		boolean resourceTypeMatches = (this.isClassifierWildcard() || that.isClassifierWildcard());

		if (!resourceTypeMatches)
		{
			if (this.hasClassifier())
			{
				resourceTypeMatches = this.getClassifier().equals(that.getClassifier());
			}
			else if (that.hasClassifier())
			{
				resourceTypeMatches = that.getClassifier().equals(this.getClassifier());
			}
			else
			{
				// Ignore resourceType
				resourceTypeMatches = true;
			}
		}

		return resourceTypeMatches;
	}
}
