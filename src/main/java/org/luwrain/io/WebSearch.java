/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>

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

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import org.luwrain.script.*;

public class WebSearch 
{
    static private final String LOG_COMPONENT = "io";
    static public final String WEB_SEARCH_HOOK = "luwrain.web.search";

    protected final Luwrain luwrain;

    public WebSearch(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

    public void searchAsync(String query, Consumer resultHandler)
    {
	NullCheck.notEmpty(query, "qeury");
	NullCheck.notNull(resultHandler, "resultHandler");
	luwrain.executeBkg(new FutureTask(()->{
		    final Object res = runWebSearchHook(luwrain, query);
		    luwrain.runUiSafely(()->resultHandler.accept(res));
	}, null));
    }

    public void searchAsync(String query)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notEmpty(query, "query");
	searchAsync(query, (result)->{
		if (result != null && (result instanceof Exception))
		{
		    luwrain.message(luwrain.i18n().getExceptionDescr((Exception)result), Luwrain.MessageType.ERROR);
		    return;
		}
		if (result == null || !(result instanceof WebSearchResult))
		{
		    onNothingFound();
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

    public void onNothingFound()
    {
	luwrain.message("Поиск в Интернете не дал результата", Luwrain.MessageType.DONE);
    }

    private Object runWebSearchHook(Luwrain luwrain, String query)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notEmpty(query, "query");
	final Object obj;
	try {
	    obj = new org.luwrain.script.hooks.ProviderHook(luwrain).run(WEB_SEARCH_HOOK, new Object[]{query});
	}
	catch(RuntimeException e)
	{
	    return e;
	}
	if (obj == null)
	    return null;
	final Object itemsObj = ScriptUtils.getMember(obj, "items");
	if (itemsObj == null)
	    return null;
	final WebSearchResult.Item[] items = WebSearchResult.getItemsFromHookObject(itemsObj);
	if (items == null)
	    return null;
	final String title;
	final Object titleObj = ScriptUtils.getMember(obj, "title");
	if(titleObj != null)
	{
	    final String value = ScriptUtils.getStringValue(titleObj);
	    title = value != null?value:"";
	} else
	    title = "";
	return new WebSearchResult(title, items);
    }
}
