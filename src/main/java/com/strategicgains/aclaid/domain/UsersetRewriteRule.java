package com.strategicgains.aclaid.domain;

import com.strategicgains.aclaid.NamespaceConfiguration;

public interface UsersetRewriteRule
{
	boolean matches(NamespaceConfiguration namespace, UserSet userset, String relation, ResourceName resource);
}
