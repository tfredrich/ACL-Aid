package com.strategicgains.aclaid.permission;

import java.text.ParseException;

/**
 * <p>This class contains utility methods to parse and match permissions.</p>
 * 
 * @author tfredrich
 * @since 17 Mar 2017
 */
public abstract class Permissions
{
	public static boolean match(String permissionStringA, String permissionStringB)
	throws ParseException
	{
		PermissionImpl a = (permissionStringA != null ? PermissionImpl.parse(permissionStringA) : null);
		PermissionImpl b = (permissionStringB != null ? PermissionImpl.parse(permissionStringB) : null);
		return match(a, b);
	}

	public static boolean match(String permissionString, Permission permission)
	throws ParseException
	{
		PermissionImpl a = (permissionString != null ? PermissionImpl.parse(permissionString) : null);
		return match(a, permission);
	}

	public static boolean match(Permission permissionA, Permission permissionB)
	{
		if (permissionA == null && permissionB == null) return true;

		return permissionA.matches(permissionB);
	}
}
