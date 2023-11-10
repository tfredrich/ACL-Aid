package com.strategicgains.aclaid.builder.rewrite;

import com.strategicgains.aclaid.builder.AbstractChildBuildable;
import com.strategicgains.aclaid.builder.Buildable;

public abstract class AbstractRewriteRuleBuilder<P extends Buildable>
extends AbstractChildBuildable<P>
implements RewriteRuleBuilder
{
	protected AbstractRewriteRuleBuilder()
	{
		super(null);
	}
}
