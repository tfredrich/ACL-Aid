package com.strategicgains.aclaid.domain.rewrite;

import java.util.ArrayList;
import java.util.List;

import com.strategicgains.aclaid.domain.LocalTupleSet;
import com.strategicgains.aclaid.domain.Tuple;
import com.strategicgains.aclaid.domain.TupleSet;

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
public class RewriteExpression
implements RewriteRule
{
	private List<RewriteRule> rules = new ArrayList<>();

	public void add(RewriteRule rule)
	{
		rules.add(rule);
	}

	@Override
	public TupleSet rewrite(TupleSet inputSet, Tuple tupleKey)
	{
		// TODO: Remove this duplicate code (duplicated in Union.rewrite())
		TupleSet rewrites = new LocalTupleSet();
		rules
			.stream()
			.map(r -> r.rewrite(inputSet, tupleKey))
			.forEach(rewrites::addAll);
		return rewrites;
	}
}
