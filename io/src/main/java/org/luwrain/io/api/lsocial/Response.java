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

import lombok.*;

@Data
public class Response
{
    public enum Status { OK, ERROR };

    static public final String
		ACCOUNT_BLOCKED = "ACCOUNT_BLOCKED",
	ILLEGAL_QUERY = "ILLEGAL_QUERY",
	INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR",
		LIMIT_EXCEEDED = "LIMIT_EXCEEDED",
	NOT_FOUND = "NOT_FOUND",
	NO_ACCOUNT = "NO_ACCOUNT",
	NO_PUBLICATION = "NO_PUBLICATION",
	NO_PRESENTATION = "NO_PRESENTRATION",
    PERMISSION_DENIED = "PERMISSION_DENIED";




    private Status status;
    private String errorCode, errorMessage;

    public Response()
    {
	this.status = Status.OK;
	this.errorCode = null;
	this.errorMessage = null;
    }

    public Response(String errorCode, String errorMessage)
    {
	if (errorCode == null || errorCode.trim().isEmpty())
	    throw new IllegalArgumentException("errorCode can't be empty");
	this.status = Status.ERROR;
	this.errorCode = errorCode;
	this.errorMessage = errorMessage;
    }
}
