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

package org.luwrain.io.api.lsocial.presentation;

import java.io.*;
import static java.util.Objects.*;
import org.luwrain.io.api.lsocial.*;

public final class DeleteQuery extends Query<DeleteQuery, DeleteResponse>
{
    static public final String
	ADDR = "/v1/presentation/delete/",
	ARG_PR = "pr";

    public DeleteQuery(String endpoint) { super(Type.GET, endpoint, ADDR, DeleteResponse.class); }
    public DeleteQuery pr(String value) { args.put(ARG_PR, requireNonNull(value, "value can't be null")); return this; }
    public DeleteQuery pr(Presentation pr) { return pr(pr.getId()); }

    public DeleteQuery pr(long value)
    {
	if (value < 0)
	    throw new IllegalArgumentException("value can't be negative");
	args.put(ARG_PR, String.valueOf(value));
	return this;
    }
}
