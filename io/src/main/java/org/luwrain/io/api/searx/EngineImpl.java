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

package org.luwrain.io.api.searx;

import java.io.*;
import com.google.auto.service.*;

import org.luwrain.io.websearch.*;

@AutoService(Engine.class)
public final class EngineImpl implements org.luwrain.io.websearch.Engine
{
    @Override public Response search(Query query) throws IOException
    {
	final var s = new Searx("http://localhost:8888");
	final var resp = s.request(query.getText());
	final var res = new Response();
	res.setQuery(query);
	res.setEntries(resp.results.stream()
	.map(r -> {
		final var e = new Entry();
		e.setTitle(r.title);
		e.setClickUrl(r.url);
		e.setDisplayUrl(r.url);
		e.setSnippet(r.content);
		return e;
	    }).toList());
	return res;
    }
}
