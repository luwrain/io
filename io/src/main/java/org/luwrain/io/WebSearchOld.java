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

package org.luwrain.io;

import java.util.concurrent.*;
import java.util.function.Consumer;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import static org.luwrain.script.Hooks.*;
import static org.luwrain.script.ScriptUtils.*;

public final class WebSearchOld 
{
    static public final String
	HOOK_WEB_SEARCH = "luwrain.web.search";

    private final Luwrain luwrain;

    public WebSearchOld(Luwrain luwrain)
    {
	this.luwrain = luwrain;
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
