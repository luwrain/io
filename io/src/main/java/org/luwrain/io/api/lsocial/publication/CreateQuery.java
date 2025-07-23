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

public class CreateQuery extends Query<CreateQuery, CreateResponse>
{
    static public final String
	ADDR = "/v1/publication/create/",
	ARG_NAME = "name",
	ARG_TYPE = "type";

    public CreateQuery(String endpoint)
    {
	super(Type.GET, endpoint, ADDR, CreateResponse.class);
    }

    public CreateQuery name(String value)
    {
	args.put(ARG_NAME, requireNonNull(value, "value can't be null"));
	return this;
    }

        public CreateQuery type(int value)
    {
	args.put(ARG_TYPE, String.valueOf(value));
	return this;
    }
}
