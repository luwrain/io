// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.websearch;

import java.util.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import org.luwrain.io.websearch.*;
import org.luwrain.core.annotations.*;

@Cmd(
     name = "web",
     title = { "en=Web", "ru=Веб" }
     )
public final class WebCommand implements Command
{
    static private final Logger log = LogManager.getLogger();
    private final Set<String> history = new HashSet<>();

    @Override public String getName()
    {
	return "web";
    }

    @Override public void onCommand(Luwrain luwrain)
    {
	final String query = Popups.editWithHistory(luwrain, luwrain.getString("static:WebCommandPopupName"), luwrain.getString("static:WebCommandPopupPrefix"), "", history);
	if (query == null || query.isBlank())
	    return;
	final var lower = query.trim().toLowerCase();
	if (lower.startsWith("http://") || lower.startsWith("https://"))
	{
	    luwrain.launchApp("reader", new String[]{ query.trim() });
	    return;
	}
	log.trace("User is asking to search: " + query);
	new WebSearch(luwrain).searchAsync(query);
    }
}
