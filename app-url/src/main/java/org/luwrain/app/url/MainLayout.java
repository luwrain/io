// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.url;

import java.net.*;
import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;

import static org.luwrain.core.DefaultEventResponse.*;

final class MainLayout extends LayoutBase
{
    private final App app;
    final NavigationArea area;

    MainLayout(App app)
    {
	super(app);
	this.app = app;

	this.area = new NavigationArea(getControlContext()){
		@Override public int getLineCount()
		{
		    return app.text.length > 0?app.text.length:1;
		}
		@Override public String getLine(int index)
		{
		    return app.text[index];
		}
		@Override public String getAreaName()
		{
		    return app.getStrings().appName();
		}
		@Override public boolean onSystemEvent(SystemEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.getType() != SystemEvent.Type.REGULAR)
			return super.onSystemEvent(event);
		    switch(event.getCode())
		    {
		    case CLIPBOARD_PASTE:
			return app.getConv().onClipboardPaste();
		    default:
			return super.onSystemEvent(event);
		    }
		}
	    };
	final Actions actions = actions(
					);
	setAreaLayout(area, actions);
    }
}