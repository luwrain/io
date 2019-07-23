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
    static public final String WEB_SEARCH_HOOK = "luwrain.web.search";
    
    @Override public String getName()
    {
	return "web";
    }

    @Override public void onCommand(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	luwrain.message("probna");
    }

    private WebSearchResult runWebSearchHook(Luwrain luwrain, String query)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notEmpty(query, "query");
	final Object obj;
	try {
	    obj = new org.luwrain.script.hooks.ProviderHook(luwrain).run(WEB_SEARCH_HOOK, new Object[]{query});
	}
	catch(RuntimeException e)
	{
	    luwrain.crash(e);
	    return null;
	}
	final List items = ScriptUtils.getArray(obj);
	if (items == null)
	    return null;
	final List<WebSearchResult.Item> res = new LinkedList();
	for(Object o: items)
	    if (o != null)
	{
	    final Object titleObj = ScriptUtils.getMember(o, "title");

	    	    final Object snippetObj = ScriptUtils.getMember(o, "snippet");
		    	    final Object displayUrlObj = ScriptUtils.getMember(o, "displayUrlObj");
			    	    final Object clickUrlObj = ScriptUtils.getMember(o, "clickUrlObj");

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
	return new WebSearchResult(res.toArray(new WebSearchResult.Item[res.size()]));
    }

    
}
