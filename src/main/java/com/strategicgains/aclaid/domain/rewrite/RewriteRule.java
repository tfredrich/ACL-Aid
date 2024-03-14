package com.strategicgains.aclaid.domain.rewrite;

import com.strategicgains.aclaid.domain.ResourceName;
import com.strategicgains.aclaid.domain.TupleSet;

/**
 * In Zanzibar, a rewrite rule is a function that takes an objectId and returns a set of users.
 * 
 * Here, we also pass in the relationTuples, which is the set of tuples for use in the rule.
 * 
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
public interface RewriteRule
{
	TupleSet rewrite(TupleSet relationTuples, String parentRelation, ResourceName objectId);
}
