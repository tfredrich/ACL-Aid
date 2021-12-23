package com.strategicgains.aclaid;

import java.util.HashSet;
import java.util.Set;

import com.strategicgains.aclaid.domain.Grant;
import com.strategicgains.aclaid.domain.Resource;
import com.strategicgains.aclaid.domain.UserSet;

public class AccessControlList
{
	private Set<Grant> grants = new HashSet<>();
//	private Map<QualifiedResourceName, PrincipalGrants> grantsByPrincipal = new HashMap<>();
//	private Map<QualifiedResourceName, Grant> grantsByResource = new HashMap<>();
	

	public AccessControlList withGrant(Grant grant)
	{
		grants.add(new Grant(grant));
		return this;
	}

	public AccessControlList withGrant(Resource resource, String relation, UserSet userset)
	{
		grants.add(new Grant(userset, relation, resource));
		return this;
	}

	public boolean isAllowed(UserSet userset, String relation, Resource resource)
	{
		for (Grant g : grants)
		{
			if (g.matches(userset, relation, resource))
			{
				return true;
			}

			if (g.getUserset().hasRelation() && g.applies(resource, relation))
			{
				if (isInGroup(userset, g.getUserset())) return true;
			}
		}

		return false;
	}

	private boolean isInGroup(UserSet userset, Resource resource)
	{
//		QualifiedResourceName group = new QualifiedResourceName(resource);
//		ResourcePath rp = group.getResourcePath();
//		String relation = rp.getFragment();
//		rp.removeFragment();
//		group.setResourcePath(rp);
//
//		for (Grant g : grants)
//		{
//			if (!g.applies(group)) continue;
//
//			if (g.matches(userset, relation, group))
//			{
//				return true;
//			}
//
//			if (g.getUserset().hasFragment() && g.applies(resource))
//			{
//				if (isInGroup(userset, g.getUserset())) return true;
//			}
//		}

		return false;
	}
}
