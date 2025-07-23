/*
 * Copyright 2024 Michael Pozhidaev <msp@luwrain.org>
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

package org.luwrain.io.api.lsocial.publication;

import java.io.*;
import static java.util.Objects.*;
import org.luwrain.io.api.lsocial.*;

public final class ListQuery extends Query<ListQuery, ListResponse>
{
    static public final String
	ADDR = "/v1/publication/list/",
	ARG_OFFSET = "offset",
	ARG_LIMIT = "limit";

    public ListQuery(String endpoint)
    {
	super(Type.GET, endpoint, ADDR, ListResponse.class);
    }

    public ListQuery offset(int value)
    {
	args.put(ARG_OFFSET, String.valueOf(value));
	return this;
    }

    public ListQuery limit(String value)
    {
	args.put(ARG_LIMIT, String.valueOf(value));
	return this;
    }
}
