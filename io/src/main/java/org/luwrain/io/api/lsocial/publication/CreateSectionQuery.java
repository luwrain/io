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

public class CreateSectionQuery extends Query<CreateSectionQuery, CreateSectionResponse>
{
    static public final String
	ADDR = "/v1/publication/section/create/",
	ARG_PUBL = "publ", //Required
	ARG_TYPE = "type", //Required
	ARG_POS = "pos", //Optional
	ARG_LABEL = "label", //Optional
	ARG_SRC = "src", //Optional
	ARG_ALT = "alt", //Optional
	ARG_CAP = "capt"; //Optional

    public CreateSectionQuery(String endpoint)
    {
	super(Type.POST, endpoint, ADDR, CreateSectionResponse.class);
    }

        public CreateSectionQuery publ(String value)
    {
	args.put(ARG_PUBL, requireNonNull(value, "value can't be null"));
	return this;
    }

            public CreateSectionQuery position(int value)
    {
	if (value < 0)
	    throw new IllegalArgumentException();
	args.put(ARG_POS, String.valueOf(value));
	return this;
    }


            public CreateSectionQuery type(int value)
    {
	if (value < 0)
	    throw new IllegalArgumentException("value can't be negative");
	args.put(ARG_TYPE, String.valueOf(value));
	return this;
    }

    public CreateSectionQuery source(String value)
    {
	args.put(ARG_SRC, requireNonNull(value, "value can't be null"));
	return this;
    }

        public CreateSectionQuery alternativeSource(String value)
    {
	args.put(ARG_ALT, requireNonNull(value, "value can't be null"));
	return this;
    }

            public CreateSectionQuery caption(String value)
    {
	args.put(ARG_CAP, requireNonNull(value, "value can't be null"));
	return this;
    }

    public CreateSectionQuery label(String value)
    {
	args.put(ARG_LABEL, requireNonNull(value, "value can't be null"));
	return this;
    }

}
