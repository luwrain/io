// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.telegram;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.*;

public final class ProxyAddrTest
{
    @Test void main()
    {
	final var a = new ProxyAddr("tg://proxy?server=8.8.8.8&port=443&secret=ddaa07cbf0cb30c5e38eefbf6838bf2499");
	assertTrue(a.valid);
	assertEquals("8.8.8.8", a.host);
	assertEquals(443, a.port);
	assertEquals("ddaa07cbf0cb30c5e38eefbf6838bf2499", a.secret);
    }
}
