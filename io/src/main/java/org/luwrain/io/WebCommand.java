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

import java.util.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import org.luwrain.io.websearch.*;

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
	if (query == null || query.trim().isEmpty())
	    return;
	log.trace("A user is asking to search: " + query);
	new WebSearch(luwrain).searchAsync(query);
    }
}
