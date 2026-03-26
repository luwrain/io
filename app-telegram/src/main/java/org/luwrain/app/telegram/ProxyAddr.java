// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.telegram;

import java.util.*;

final class ProxyAddr
{
    static private final String
	PREFIX = "tg://proxy?",
	HOST = "server=",
	PORT = "port=",
	SECRET = "secret=";

    final String source, host, secret;
    final boolean valid;
    int port;

    ProxyAddr(String source)
    {
	this.source = source;
	if (!source.startsWith(PREFIX))
	{
	    valid = false;
	    host = null;
	    port = 0;
	    secret = null;
	    return;
	}
	final var params = source.substring(PREFIX.length()).split("&", -1);
	String h = null, p = null, s = null;
	for(var pp: params)
	{
	    if (pp.startsWith(HOST))
		h = pp.substring(HOST.length());
	    if (pp.startsWith(PORT))
		p = pp.substring(PORT.length());
	    if (pp.startsWith(SECRET))
		s = pp.substring(SECRET.length());
	}
	if (h == null || p == null || s == null)
	{
	    valid = false;
	    host = null;
	    port = 0;
	    secret = null;
	    return;
	}
	final int pp;
	try {
	    pp = Integer.parseInt(p);
	}
	catch(NumberFormatException ex)
	{
	    valid = false;
	    host = null;
	    port = 0;
	    secret = null;
	    return;
	}
	valid = true;
	host = h;
	port = pp;
	secret = s;
    }
    
    
}
