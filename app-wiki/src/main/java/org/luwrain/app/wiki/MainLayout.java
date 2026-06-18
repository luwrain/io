// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.wiki;

import java.net.*;
import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;
import org.luwrain.io.api.mediawiki.*;

import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.controls.ConsoleUtils.*;

final class MainLayout extends LayoutBase implements  ConsoleArea.ClickHandler<Page>
{
    private final App app;
    final ConsoleArea area;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	this.area = new ConsoleArea<Page>(consoleParams((params)->{
		    params.model = new ListModel<Page>(app.pages);
		    params.appearance = new Appearance();
		    params.name = app.getStrings().appName();
		    params.clickHandler = this;
		    params.inputPos = ConsoleArea.InputPos.TOP;
		}));
	final Actions actions = actions(
					action("servers", app.getStrings().actionServers(), new InputEvent(InputEvent.Special.F5), this::actServers)
					);
	area.setConsoleInputHandler((area,text)->{
		NullCheck.notNull(text, "text");
		if (text.trim().isEmpty())
		    return ConsoleArea.InputHandler.Result.REJECTED;
		return app.search(text.trim())?ConsoleArea.InputHandler.Result.OK:ConsoleArea.InputHandler.Result.REJECTED;
	    });
	area.setInputPrefix(app.getStrings().appName() + ">");
	setAreaLayout(area, actions);
    }

    @Override public boolean onConsoleClick(ConsoleArea area, int index, Page obj)
    {
	if (obj == null || !(obj instanceof Page))
	    return false;
	final Page page = (Page)obj;
	Server serv = null;
	for(Server s: app.servers)
	    if (page.baseUrl.equals(s.searchUrl))
	    {
		serv = s;
		break;
	    }
	if (serv == null || serv.pagesUrl == null || serv.pagesUrl.trim().isEmpty())
	    return false;
	try {
	    String url = serv.pagesUrl;
	    if (!url.endsWith("/"))
		url += "/";
	    url += URLEncoder.encode(page.title, "UTF-8").replaceAll("\\+", "%20");//Completely unclear why wikipedia doesn't recognize '+' sign
	    app.getLuwrain().launchApp("reader", new String[]{url});
	}
	catch (UnsupportedEncodingException e)
	{
	    app.crash(e);
	}
	return true;
    }

    private boolean actServers()
    {
	final ServersLayout serversLayout = new ServersLayout(app, ()->{
		app.setAreaLayout(this);
		app.getLuwrain().announceActiveArea();
		return true;
	    });
	app.setAreaLayout(serversLayout);
	app.getLuwrain().announceActiveArea();
	return true;
    }

    final class Appearance implements ConsoleArea.Appearance<Page>
    {
	@Override public void announceItem(Page page)
	{
	    NullCheck.notNull(page, "page");
	    app.setEventResponse(listItem(app.getLuwrain().getSpeakableText(page.toString(), Luwrain.SpeakableTextType.NATURAL)));
	}
	@Override public String getTextAppearance(Page page)
	{
	    NullCheck.notNull(page, "page");
	    return page.toString();
	}
    }
}
