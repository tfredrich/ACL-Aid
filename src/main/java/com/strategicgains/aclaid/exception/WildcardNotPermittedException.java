/*
    Copyright 2018, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package com.strategicgains.aclaid.exception;

/**
 * @author toddf
 * @since Jun 21, 2018
 */
public class WildcardNotPermittedException
extends Exception
{
	private static final long serialVersionUID = -458600478129079490L;

	public WildcardNotPermittedException()
	{
		super();
	}

	public WildcardNotPermittedException(String message, Throwable cause,
	    boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WildcardNotPermittedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public WildcardNotPermittedException(String message)
	{
		super(message);
	}

	public WildcardNotPermittedException(Throwable cause)
	{
		super(cause);
	}
}
