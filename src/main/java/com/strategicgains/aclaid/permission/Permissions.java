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
		return match(PermissionImpl.parse(permissionStringA), PermissionImpl.parse(permissionStringB));
	}

	public static boolean match(String permissionString, PermissionImpl permission)
	throws ParseException
	{
		return match(PermissionImpl.parse(permissionString), permission);
	}

	public static boolean match(PermissionImpl permissionA, PermissionImpl permissionB)
	{
		if (permissionA == null && permissionB == null) return true;

		return permissionA.matches(permissionB);
	}
}
