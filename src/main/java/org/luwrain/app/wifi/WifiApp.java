/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.app.wifi;

import java.net.*;
import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.popups.Popups;

public class WifiApp implements Application, Actions
{
    public static final String STRINGS_NAME = "luwrain.wifi";

    private Luwrain luwrain;
    private Strings strings;
    private final Base base = new Base();
    private ListArea area;

    @Override public boolean onLaunch(Luwrain luwrain)
    {
	final Object o = luwrain.i18n().getStrings(STRINGS_NAME);
	if (o == null || !(o instanceof Strings))
	    return false;
	strings = (Strings)o;
	this.luwrain = luwrain;
	if (!base.init(luwrain))
	    return false;
	createArea();
	base.launchScanning(area);
	return true;
    }

    @Override public void onReady()
    {
	base.onReady();
	area.refresh();
	area.resetHotPoint(false);
    }

    @Override public boolean onClick(Object obj)
    {
	//FIXME:
	return false;
    }

    private void createArea()
    {
	final Actions a = this;
	final Strings s = strings;

	final ListParams params = new ListParams();
	params.environment = new DefaultControlEnvironment(luwrain);
	params.model = base.getListModel();
	params.appearance = new DefaultListItemAppearance(params.environment);
	params.clickHandler = (area, index, obj)->a.onClick(obj);
	params.name = strings.appName();

	area = new ListArea(params){
		@Override public boolean onEnvironmentEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    switch(event.getCode())
		    {
		    case EnvironmentEvent.CLOSE:
			a.closeApp();
			return true;
		    case EnvironmentEvent.THREAD_SYNC://FIXME:Change to something better
			a.onReady();
			return true;
		    default:
			return super.onEnvironmentEvent(event);
		    }
		}
	    };

    }

    @Override public String getAppName()
    {
	return strings.appName();
    }

    @Override public AreaLayout getAreasToShow()
    {
	return new AreaLayout(area);
    }

    @Override public boolean closeApp()
    {
	//FIXME:Checking threads;
	luwrain.closeApp();
	return true;
    }
}
