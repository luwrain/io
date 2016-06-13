/*
   Copyright 2012-2016 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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
import org.luwrain.network.*;

public class WifiApp implements Application, Actions, MonoApp
{
    private Luwrain luwrain;
    private Strings strings;
    private final Base base = new Base();
    private ListArea listArea;
    private ProgressArea progressArea;
    private AreaLayoutSwitch layouts;

    @Override public boolean onLaunch(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	final Object o = luwrain.i18n().getStrings(Strings.NAME);
	if (o == null || !(o instanceof Strings))
	    return false;
	strings = (Strings)o;
	this.luwrain = luwrain;
	if (!base.init(luwrain, this, strings))
	    return false;
	createArea();
	layouts = new AreaLayoutSwitch(luwrain);
	layouts.add(new AreaLayout(listArea));
	layouts.add(new AreaLayout(AreaLayout.TOP_BOTTOM, listArea, progressArea));
	base.launchScanning();
	return true;
    }

    private void createArea()
    {
	final ListArea.Params params = new ListArea.Params();
	params.environment = new DefaultControlEnvironment(luwrain);
	params.model = base.getListModel();
	params.appearance = new Appearance(luwrain, strings);
	params.clickHandler = (area, index, obj)->onClick(obj);
	params.name = strings.appName();
	params.flags = ListArea.Params.loadRegularFlags(luwrain.getRegistry());

	listArea = new ListArea(params){
		@Override public boolean onKeyboardEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isSpecial() && !event.isModified())
			switch(event.getSpecial())
			{
			case TAB:
			    goToProgress();
			    return true;
			}
		    return super.onKeyboardEvent(event);
		}
		@Override public boolean onEnvironmentEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    switch(event.getCode())
		    {
		    case CLOSE:
closeApp();
			return true;
		    case REFRESH:
doScanning();
			return true;
		    default:
			return super.onEnvironmentEvent(event);
		    }
		}
		@Override protected String noContentStr()
		{
		    return isScanning()?strings.scanningInProgress():strings.noWifiNetworks();
		}
	    };

	progressArea = new ProgressArea(new DefaultControlEnvironment(luwrain), "Подключение"){
		@Override public boolean onKeyboardEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isSpecial() && !event.isModified())
			switch(event.getSpecial())
			{
			case TAB:
goToList();
			    return true;
			}
		    return super.onKeyboardEvent(event);
		}
		@Override public boolean onEnvironmentEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    switch(event.getCode())
		    {
case CLOSE:
closeApp();
return true;
default:
return super.onEnvironmentEvent(event);
 }
		}
	    };
    }

    @Override public void onReady()
    {
	listArea.refresh();
    }

    private void doScanning()
    {
	if (base.launchScanning())
	    listArea.refresh();
    }

    private boolean onClick(Object obj)
    {
	NullCheck.notNull(obj, "obj");
	if (!(obj instanceof WifiNetwork))
	    return false;
	progressArea.clear();
	if (!base.launchConnection(progressArea, (WifiNetwork)obj))
	    return false;
	layouts.show(1);
	goToProgress();
	return true;
    }

    private boolean isScanning()
    {
	return base.isScanning();
    }

    private void goToList()
    {
	luwrain.setActiveArea(listArea);
    }

private void goToProgress()
    {
	if (layouts.getCurrentIndex() == 0)
	{
	    luwrain.setActiveArea(listArea);
	    return;
	}
	luwrain.setActiveArea(progressArea);
    }

    @Override public String getAppName()
    {
	return strings.appName();
    }

    @Override public MonoApp.Result onMonoAppSecondInstance(Application app)
    {
	NullCheck.notNull(app, "app");
	return MonoApp.Result.BRING_FOREGROUND;
    }

    @Override public AreaLayout getAreasToShow()
    {
	return layouts.getCurrentLayout();
    }

    @Override public boolean closeApp()
    {
	//FIXME:Checking threads;
	luwrain.closeApp();
	return true;
    }
}
