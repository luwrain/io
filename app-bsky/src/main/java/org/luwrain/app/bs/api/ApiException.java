// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bs.api;

public final class ApiException extends Exception
{
    private final int statusCode;

    public ApiException(int statusCode, String message)
    {
	super(message);
	this.statusCode = statusCode;
    }

    public ApiException(String message)
    {
	this(-1, message);
    }

    public ApiException(String message, Throwable cause)
    {
	super(message, cause);
	this.statusCode = -1;
    }

    public int getStatusCode()
    {
	return statusCode;
    }

    @Override public String toString()
    {
	if (statusCode >= 0)
	    return "ApiException[status=" + statusCode + ", message=" + getMessage() + "]";
	return "ApiException[message=" + getMessage() + "]";
    }
}
