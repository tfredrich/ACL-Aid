package com.strategicgains.aclaid;

import java.text.ParseException;
import java.util.Objects;

import com.strategicgains.aclaid.Permission;

/**
 * <p>A Permission is a simple string which defines an action that can be performed on a resource.
 * Permissions are namespaced to eliminate name clashes across services. The primary property on
 * the Permission is 'action'. The namespace + action (in the form "namespace:action") is used directly
 * in Policy definitions.</p>
 * 
 * <p>To support wildcard specifications in Policy definitions, an asterisk ('*') can be used in the 'action'
 * property to mean 'all actions'. In addition, since most people (e.g. developers) think of permissions in terms
 * of an action on a particular resource type, Permission supports the concept of a resource type.</p>
 * 
 * @author toddf
 * @since 13 Mar 2017
 */
public class Permission
{
	public static final String DELIMITER = ":";
	public static final String WILDCARD = "*";

	private String namespace;
	private String action;
	private String classifier;

	public Permission(String namespace, String action, String classifier)
	{
		super();
		this.namespace = namespace;
		this.action = action;
		this.classifier = classifier;
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
		if (Permission.class.isAssignableFrom(that.getClass()));

		return equals((Permission) that);
	}

	public boolean equals(Permission that)
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

	public String format()
	{
		StringBuilder sb = new StringBuilder(getNamespace());
		sb.append(DELIMITER);

		if (action != null)
		{
			sb.append(getAction());
		}
		else
		{
			sb.append(WILDCARD);
			return sb.toString();
		}

		sb.append(DELIMITER);

		if (isClassifierWildcard())
		{
			sb.append(WILDCARD);
		}
		else
		{
			sb.append(getClassifier());
		}

		return sb.toString();
	}

	public static Permission parse(String permission)
	throws ParseException
	{
		if (permission == null) throw new ParseException("Permission strings cannot be null", 0);

		String[] parts = permission.split(DELIMITER);

		if (parts.length > 3) throw new ParseException("Permission strings have at most three (3) segments", 0);
		if (parts.length < 2) throw new ParseException("Permission strings have at least two (2) segments", 0);

		return (parts.length == 3 ? new Permission(parts[0], parts[1], parts[2]) : new Permission(parts[0], parts[1], null));
	}

	public static boolean match(String permissionStringA, String permissionStringB)
	throws ParseException
	{
		return match(parse(permissionStringA), parse(permissionStringB));
	}

	public static boolean match(String permissionString, Permission permission)
	throws ParseException
	{
		return match(parse(permissionString), permission);
	}

	public static boolean match(Permission permissionA, Permission permissionB)
	{
		if (permissionA == null && permissionB == null) return true;
		if (permissionA == null || permissionB == null) return false;
		if (!permissionA.getNamespace().equals(permissionB.getNamespace())) return false;

		boolean actionMatches = (permissionA.getAction().equals(permissionB.getAction())
			|| permissionA.isActionWildcard()
			|| permissionB.isActionWildcard());

		if (!actionMatches) return false;

		boolean resourceTypeMatches = (permissionA.isClassifierWildcard() || permissionB.isClassifierWildcard());

		if (!resourceTypeMatches) {
			if (permissionA.hasClassifier()) {
				resourceTypeMatches = permissionA.getClassifier().equals(permissionB.getClassifier());
			}
			else if (permissionB.hasClassifier()) {
				resourceTypeMatches = permissionB.getClassifier().equals(permissionA.getClassifier());
			}
			else {	// Ignore resourceType
				resourceTypeMatches = true;
			}
		}

		return resourceTypeMatches;
	}
}
