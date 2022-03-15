package com.strategicgains.aclaid.exception;

public class RelationNotRegisteredException
extends Exception
{
	private static final long serialVersionUID = 1172776356613955205L;

	public RelationNotRegisteredException()
	{
	}

	public RelationNotRegisteredException(String message)
	{
		super(message);
	}

	public RelationNotRegisteredException(Throwable cause)
	{
		super(cause);
	}

	public RelationNotRegisteredException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public RelationNotRegisteredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
