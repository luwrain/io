
package org.luwrain.app.wiki;

import java.net.*;
import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.queries.*;
import org.luwrain.controls.*;
import org.luwrain.popups.*;
import org.luwrain.app.base.*;
import static org.luwrain.core.DefaultEventResponse.*;

final class MainLayout extends LayoutBase
{
    private final App app;
final ConsoleArea area;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	this.area = new ConsoleArea(consoleParams((params)->{
		    //			params.model = base.getModel();
		    params.appearance = new Appearance();
		    params.name = app.getStrings().appName();
	params.inputPos = ConsoleArea.InputPos.TOP;
		})) ;
    	area.setConsoleClickHandler((area,index,obj)->{
		if (obj == null || !(obj instanceof Page))
		    return false;
		final Page page = (Page)obj;
		try {
		    final String url = "https://" + URLEncoder.encode(page.lang) + ".wikipedia.org/wiki/" + URLEncoder.encode(page.title, "UTF-8").replaceAll("\\+", "%20");//Completely unclear why wikipedia doesn't recognize '+' sign
		    app.getLuwrain().launchApp("reader", new String[]{url});
		}
		catch (UnsupportedEncodingException e)
		{
		    app.crash(e);
		}
		return true;
	    });
	area.setConsoleInputHandler((area,text)->{
		NullCheck.notNull(text, "text");
		if (text.trim().isEmpty() || app.isBusy())
		    return ConsoleArea.InputHandler.Result.REJECTED;
		//base.search("", text.trim(), area);
		return ConsoleArea.InputHandler.Result.OK;
	    });
	area.setInputPrefix(app.getStrings().appName() + ">");
	setAreaLayout(area, actions());
    }

final class Appearance implements ConsoleArea.Appearance
    {
	    @Override public void announceItem(Object item)
	    {
		NullCheck.notNull(item, "item");
		app.setEventResponse(listItem(item.toString()));
			    }
	    @Override public String getTextAppearance(Object item)
	    {
		NullCheck.notNull(item, "item");
		return item.toString();
	    }
}
}
