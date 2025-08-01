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

package org.luwrain.io.api.lsocial.publication;

import java.io.*;
import static java.util.Objects.*;
import org.luwrain.io.api.lsocial.*;

public final class GetSectionQuery extends Query<GetSectionQuery, GetSectionResponse>
{
    static public final String
	ADDR = "/v1/publication/section/get/",
	ARG_PUBL = "publ",
	ARG_SECT = "sect";

    public GetSectionQuery(String endpoint)
    {
	super(Type.GET, endpoint, ADDR, GetSectionResponse.class);
    }

    public GetSectionQuery publ(String value)
    {
	args.put(ARG_PUBL, requireNonNull(value, "value can't be null"));
	return this;
    }

        public GetSectionQuery publ(long value)
    {
	if (value < 0)
	    throw new IllegalArgumentException("value can't be negative");
	args.put(ARG_PUBL, String.valueOf(value));
	return this;
    }

                    public GetSectionQuery publ(Publication publ)
    {
	requireNonNull(publ, "publ can't be null");
	return publ(publ.getId());
    }

    public GetSectionQuery sect(int value)
    {
	if (value < 0)
	    throw new IllegalArgumentException("value can't be negative");
	args.put(ARG_SECT, String.valueOf(value));
	return this;
    }
}
