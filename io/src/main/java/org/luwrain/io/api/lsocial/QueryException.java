/*
 * Copyright 2024-2025 Michael Pozhidaev <msp@luwrain.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.luwrain.io.api.lsocial;

public class QueryException extends java.io.IOException
{
    public final int httpCode;
    public final Response resp;

    public QueryException(Exception e)
    {
	super(e.getMessage(), e);
	this.httpCode = -1;
	this.resp = null;
    }

    public QueryException(String message)
    {
	super(message);
		this.httpCode = -1;
	this.resp = null;
    }

    public QueryException(int httpCode, Response resp)
    {
	super("HTTP code: " + String.valueOf(httpCode + (resp != null?", Error code: " + resp.getErrorCode():"")));
	this .httpCode = httpCode;
	this.resp = resp;
    }
}
