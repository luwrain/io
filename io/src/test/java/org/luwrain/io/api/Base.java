/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.io.api;

import org.apache.logging.log4j.*;

public final class Base
{
    static final String
	ENV_ALLOW_API_TESTS = "LWR_ALLOW_API_TESTS",
	ALLOW_API_TESTS = System.getenv(ENV_ALLOW_API_TESTS);

    static private final Logger log = LogManager.getLogger();

    static public boolean allowApiTests()
    {
	final boolean res = ALLOW_API_TESTS != null && ALLOW_API_TESTS.equalsIgnoreCase("true");
	log.info("API tests " + (res?"allowed":"not allowed"));
	return res;
    }
}
