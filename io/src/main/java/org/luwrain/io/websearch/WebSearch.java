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

package org.luwrain.io.websearch;

import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;

import static java.util.Objects.*;

public final class WebSearch 
{
    static private final Logger log = LogManager.getLogger();
    
    final Luwrain luwrain;
    
    public WebSearch(Luwrain luwrain)
    {
	this.luwrain = requireNonNull(luwrain, "luwrain can't be null");
    }

    public void searchAsync(String query, Consumer<Response> resultHandler)
    {
	final var engines = luwrain.createInstances(Engine.class);
	if (engines == null || engines.isEmpty())
	{
	    log.error("No search engines available");
	    return;
	}
	final var eng = engines.get(0);
	final var q = new Query();
	q.setText(query);
	luwrain.executeBkg(()->{
		log.trace("Using search engine " + eng.getClass().getName());
		try {
		    final Response res = eng.search(q);
		    luwrain.runUiSafely(()->resultHandler.accept(res));
		}
		catch(IOException ex)
		{
		    log.error("Web search failed using the engine " + eng.getClass().getName(), ex);
		    luwrain.crash(ex);
		}
	    });
    }

    public void searchAsync(String query)
    {
	searchAsync(query, res -> {
		if (res == null || res.getEntries() == null || res.getEntries().isEmpty())
		{
		    luwrain.message(luwrain.getString("static:NothingFound"), Luwrain.MessageType.DONE);
		    return;
		}
		log.trace("Obtained " + res.getEntries().size() + " search results");
		final Entry entry = WebSearchPopup.open(luwrain, res);
		if (entry == null)
		    return;
		onEntryClick(entry);
	    });
    }

    public void onEntryClick(Entry entry)
    {
	if (!entry.getClickUrl().isEmpty())
	    luwrain.openUrl(entry.getClickUrl());
    }
}
