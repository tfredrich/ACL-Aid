package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.RelationDefinition;

/**
union(
	intersection(
		relation(inputObj, "owner"),
		relation(inputObj, "editor")
	),
	relation(inputObj, "viewer")
)

Outputs:
       +--union--+
      /           \
     /             \
    /               \
intersection      viewer
  /       \
owner    editor

union {
   child { _this {} }
   child { computed_userset { relation: "owner" } }
}

Outputs:
     union
    /     \
_this     computed_userset
                   \
               relation: owner

union {
  child { _this {} }
  child { computed_userset { relation: "editor" } }
  child { tuple_to_userset {
    tupleset { relation: "parent" }
    computed_userset {
      object: $TUPLE_USERSET_OBJECT  # parent folder
      relation: "viewer"
    }
 }}
}

Outputs:
     +---------------+----------union---------------+
    /               /                                \
_this     computed_userset           +-----------tuple_to_userset---------+
                   /                /                                      \
          relation: editor     tupleset                             computed_userset
                                   /                                /               \
                            relation: parent     object: $TUPLE_USERSET_OBJECT     relation: viewer
*/
public abstract class AbstractRewriteRule
implements RewriteRule
{
	private RelationDefinition parent;

	private AbstractRewriteRule()
	{
		// Prevents empty instances.
		super();
	}

	protected AbstractRewriteRule(RelationDefinition parent)
	{
		this();
		setParent(parent);
	}

	protected RelationDefinition getParent()
	{
		return parent;
	}

	protected void setParent(RelationDefinition parent)
	{
		this.parent = parent;
	}
}
