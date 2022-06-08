/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.io;

import java.util.concurrent.*;
import java.util.function.Consumer;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import static org.luwrain.script.Hooks.*;
import static org.luwrain.script2.ScriptUtils.*;

public final class WebSearch 
{
    static public final String
	HOOK_WEB_SEARCH = "luwrain.web.search";

    private final Luwrain luwrain;

    public WebSearch(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

    public void searchAsync(String query, Consumer<Object> resultHandler)
    {
	NullCheck.notEmpty(query, "query");
	NullCheck.notNull(resultHandler, "resultHandler");
	luwrain.executeBkg(new FutureTask<>(()->{
		    final Object res;
		    try {
			res = runWebSearchHook(luwrain, query);
		    }
		    catch(Throwable e)
		    {
			luwrain.crash(e);
			return;
		    }
		    luwrain.runUiSafely(()->resultHandler.accept(res));
	}, null));
    }

    public void searchAsync(String query)
    {
	NullCheck.notEmpty(query, "query");
	searchAsync(query, (result)->{
		if (result == null || !(result instanceof WebSearchResult))
		{
		    luwrain.message(luwrain.i18n().getStaticStr("NothingFound"), Luwrain.MessageType.DONE);
		    return;
		}
		final WebSearchResult webSearchResult = (WebSearchResult)result;
		final WebSearchResult.Item item = WebSearchResultPopup.open(luwrain, webSearchResult);
		if (item == null)
		    return;
		onItemClick(item);
	    });
    }

    public void onItemClick(WebSearchResult.Item item)
    {
	NullCheck.notNull(item, "item");
	if (!item.getClickUrl().isEmpty())
	    luwrain.openUrl(item.getClickUrl());
    }

    private Object runWebSearchHook(Luwrain luwrain, String query)
    {
	final Object obj = provider(luwrain, HOOK_WEB_SEARCH, new Object[]{query});
	if (obj == null)
	    return null;
	final WebSearchResult.Item[] items = WebSearchResult.getItemsFromHookObj(getMember(obj, "items"));
	if (items == null)
	    return null;
	final String title = asString(getMember(obj, "title"));
	return new WebSearchResult(title != null?title.trim():"", items);
    }
}
