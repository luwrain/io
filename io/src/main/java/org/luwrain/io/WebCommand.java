
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
