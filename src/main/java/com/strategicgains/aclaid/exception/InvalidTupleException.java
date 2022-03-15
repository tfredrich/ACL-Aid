package com.strategicgains.aclaid.exception;

public class InvalidTupleException
extends Exception
{
	private static final long serialVersionUID = 5407547613742588683L;

	public InvalidTupleException() {
		super();
	}

	public InvalidTupleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidTupleException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvalidTupleException(String message)
	{
		super(message);
	}

	public InvalidTupleException(Throwable cause)
	{
		super(cause);
	}
}
