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

public final class GetQuery extends Query<GetQuery, GetResponse>
{
    static public final String
	ADDR = "/v1/publication/get/",
	ARG_PUBL = "publ",
	ARG_MODE = "mode";

    static public final int
	MODE_HEADER = 0,
	MODE_FULL = 10;

    public GetQuery(String endpoint)
    {
	super(Type.GET, endpoint, ADDR, GetResponse.class);
    }

    public GetQuery publ(String value)
    {
	args.put(ARG_PUBL, requireNonNull(value, "value can't be null"));
	return this;
    }

    public GetQuery mode(int mode)
    {
	if (mode < 1)
	    throw new IllegalArgumentException("mode can't be negative");
	args.put(ARG_MODE, String.valueOf(mode));
	return this;
    }
}
