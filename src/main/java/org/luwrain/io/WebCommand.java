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
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import org.luwrain.script.*;

public final class WebCommand implements Command
{
    static private final String LOG_COMPONENT = "io";
    static public final String WEB_OPEN_HOOK = "luwrain.web.open";

    @Override public String getName()
    {
	return "web";
    }

    @Override public void onCommand(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	final String query = Popups.simple(luwrain, "Найти или открыть в Интернете", "Интернет:", "");
	if (query == null || query.trim().isEmpty())
	    return;
	final WebSearchResult res = runWebOpenHook(luwrain, query);
	if (res == null || res.getItemCount() == 0)
	{
	    luwrain.message("Ничего не найдено", Luwrain.MessageType.ERROR);
	    return;
	}
	final WebSearchResultPopup popup = new WebSearchResultPopup(luwrain, res.getTitle(), res, Popups.DEFAULT_POPUP_FLAGS);
	luwrain.popup(popup);
	if (popup.wasCancelled())
	    return;
	final WebSearchResult.Item result = popup.result();
	if (result == null)
	    return;
	luwrain.openUrl(result.getClickUrl());
    }

    private WebSearchResult runWebOpenHook(Luwrain luwrain, String query)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notEmpty(query, "query");
	final Object obj;
	try {
	    obj = new org.luwrain.script.hooks.ProviderHook(luwrain).run(WEB_OPEN_HOOK, new Object[]{query});
	}
	catch(RuntimeException e)
	{
	    luwrain.crash(e);
	    return null;
	}
	final Object itemsObj = ScriptUtils.getMember(obj, "items");
	if (itemsObj == null)
	    return null;
	final List items = ScriptUtils.getArray(itemsObj);
	if (items == null)
	    return null;
	final List<WebSearchResult.Item> res = new LinkedList();
	for(Object o: items)
	    if (o != null)
	    {
		final Object titleObj = ScriptUtils.getMember(o, "title");
		final Object snippetObj = ScriptUtils.getMember(o, "snippet");
		final Object displayUrlObj = ScriptUtils.getMember(o, "displayUrl");
		final Object clickUrlObj = ScriptUtils.getMember(o, "clickUrl");
		if (titleObj == null || snippetObj == null ||
		    displayUrlObj == null || clickUrlObj == null)
		    continue;
		final String title = ScriptUtils.getStringValue(titleObj);
		final String snippet = ScriptUtils.getStringValue(snippetObj);
		final String displayUrl = ScriptUtils.getStringValue(displayUrlObj);
		final String clickUrl = ScriptUtils.getStringValue(clickUrlObj);
		if (title == null || snippet == null ||
		    clickUrl == null || displayUrl == null)
		    continue;
		if (title.isEmpty() || clickUrl.isEmpty())
		    continue;
		res.add(new WebSearchResult.Item(title, snippet, displayUrl, clickUrl));
	    }
	final String title;
	final Object titleObj = ScriptUtils.getMember(obj, "title");
	if(titleObj != null)
	{
	    final String value = ScriptUtils.getStringValue(titleObj);
	    title = value != null?value:"";
	} else
	    title = "";
	return new WebSearchResult(title, res.toArray(new WebSearchResult.Item[res.size()]));
    }
}
