package com.strategicgains.aclaid.domain.rewrite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public abstract class AggregateExpression
implements Expression
{
	private List<Expression> operands = new ArrayList<>();

	protected AggregateExpression()
	{
		super();
	}

	protected AggregateExpression(Expression... operands)
	{
		this();
		setOperands(operands);
	}

	public AggregateExpression add(Expression operand)
	{
		this.operands.add(operand);
		return this;
	}

	private void setOperands(Expression[] operands)
	{
		if (operands == null || operands.length == 0)
		{
			this.operands.clear();
			return;
		}

		this.operands = new ArrayList<>(Arrays.asList(operands));
	}

	protected Stream<Expression> operands()
	{
		return operands.stream();
	}
}
