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

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import static org.luwrain.script.Hooks.*;
import static org.luwrain.script2.ScriptUtils.*;

public final class WebCommand implements Command
{
    static private final String
	HOOK_WEB_OPEN = "luwrain.web.open";

    private final Set<String> history = new HashSet<>();

    @Override public String getName()
    {
	return "web";
    }

    @Override public void onCommand(Luwrain luwrain)
    {
	final String query = Popups.editWithHistory(luwrain, luwrain.i18n().getStaticStr("WebCommandPopupName"), luwrain.i18n().getStaticStr("WebCommandPopupPrefix"), "", history);
	if (query == null || query.trim().isEmpty())
	    return;
	final Object res = runWebOpenHook(luwrain, query);
	if (res == null)
	{
	    new WebSearch(luwrain).searchAsync(query);
	    return;
	}
	if (res instanceof Exception)
	{
	    luwrain.message(luwrain.i18n().getExceptionDescr((Exception)res), Luwrain.MessageType.ERROR);
	    return;
	}
	if (res instanceof Boolean)
	{
	    final Boolean bool = (Boolean)res;
	    if (bool.booleanValue())
		return;
	}
	if (!(res instanceof WebSearchResult))
	    return;
	final WebSearchResult webSearchResult = (WebSearchResult)res;
	if (webSearchResult.getItemCount() == 0)
	{
	    luwrain.message("Поиск в Интернете не дал результатов", Luwrain.MessageType.DONE);
	    return;
	}
	final WebSearchResult.Item item = WebSearchResultPopup.open(luwrain, webSearchResult);
	if (item == null)
	    return;
	luwrain.openUrl(item.getClickUrl());
    }

    private Object runWebOpenHook(Luwrain luwrain, String query)
    {
	final Object obj = provider(luwrain, HOOK_WEB_OPEN, new Object[]{query});
	if (obj == null || isNull(obj))
	    return null;
	if (isBoolean(obj))
	    return new Boolean(asBoolean(obj));
	final Object[] items = asArray(getMember(obj, "items"));
	if (items == null)
	    return null;
	final List<WebSearchResult.Item> res = new ArrayList<>();
	for(Object o: items)
	    if (o != null)
	    {
		final String
		title = asString(getMember(o, "title")),
		snippet = asString(getMember(o, "snippet")),
		displayUrl = asString(getMember(o, "displayUrl")),
		clickUrl = asString(getMember(o, "clickUrl"));
		if (title == null || title.trim().isEmpty())
		    continue;
		res.add(new WebSearchResult.Item(title.trim(),
						 snippet != null?snippet.trim():"",
						 displayUrl != null?displayUrl.trim():"",
						 clickUrl != null?clickUrl.trim():""));
	    }
	final String title = asString(getMember(obj, "title"));
	return new WebSearchResult(title != null?title.trim():"", res.toArray(new WebSearchResult.Item[res.size()]));
    }
}
