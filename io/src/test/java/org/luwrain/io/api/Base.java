// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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
