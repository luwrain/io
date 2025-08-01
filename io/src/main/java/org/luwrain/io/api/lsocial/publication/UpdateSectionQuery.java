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

public class UpdateSectionQuery extends Query<UpdateSectionQuery, UpdateSectionResponse>
{
    static public final String
	ADDR = "/v1/publication/section/update/",
	ARG_PUBL = "publ", //Required
	ARG_SECT = "sect",
	ARG_TYPE = "type", //Required
	ARG_LABEL = "label", //Optional
	ARG_SRC = "src", //Optional
	ARG_ALT = "alt", //Optional
	ARG_CAP = "capt"; //Optional

    public UpdateSectionQuery(String endpoint)
    {
	super(Type.POST, endpoint, ADDR, UpdateSectionResponse.class);
    }

        public UpdateSectionQuery publ(String value)
    {
	args.put(ARG_PUBL, requireNonNull(value, "value can't be null"));
	return this;
    }

            public UpdateSectionQuery sect(String value)
    {
	args.put(ARG_SECT, requireNonNull(value, "value can't be null"));
	return this;
    }

            public UpdateSectionQuery type(int value)
    {
	if (value < 0)
	    throw new IllegalArgumentException("value can't be negative");
	args.put(ARG_TYPE, String.valueOf(value));
	return this;
    }

    public UpdateSectionQuery source(String value)
    {
	args.put(ARG_SRC, requireNonNull(value, "value can't be null"));
	return this;
    }

        public UpdateSectionQuery alternativeSource(String value)
    {
	args.put(ARG_ALT, requireNonNull(value, "value can't be null"));
	return this;
    }

            public UpdateSectionQuery caption(String value)
    {
	args.put(ARG_CAP, requireNonNull(value, "value can't be null"));
	return this;
    }

    public UpdateSectionQuery label(String value)
    {
	args.put(ARG_LABEL, requireNonNull(value, "value can't be null"));
	return this;
    }
}
