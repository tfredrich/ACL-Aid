package com.strategicgains.aclaid;

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
		return match(Permission.parse(permissionStringA), Permission.parse(permissionStringB));
	}

	public static boolean match(String permissionString, Permission permission)
	throws ParseException
	{
		return match(Permission.parse(permissionString), permission);
	}

	public static boolean match(Permission permissionA, Permission permissionB)
	{
		if (permissionA == null && permissionB == null) return true;

		return permissionA.matches(permissionB);
	}
}
