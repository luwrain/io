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
import org.luwrain.network.*;

public class WifiApp implements Application, Actions
{
    public static final String STRINGS_NAME = "luwrain.wifi";

    private Luwrain luwrain;
    private Strings strings;
    private final Base base = new Base();
    private ListArea listArea;
    private ProgressArea progressArea;
    private AreaLayoutSwitch layouts;

    @Override public boolean onLaunch(Luwrain luwrain)
    {
	final Object o = luwrain.i18n().getStrings(STRINGS_NAME);
	if (o == null || !(o instanceof Strings))
	    return false;
	strings = (Strings)o;
	this.luwrain = luwrain;
	if (!base.init(luwrain, this))
	    return false;
	createArea();
	layouts = new AreaLayoutSwitch(luwrain);
	layouts.add(new AreaLayout(listArea));
	layouts.add(new AreaLayout(AreaLayout.TOP_BOTTOM, listArea, progressArea));
	base.launchScanning();
	return true;
    }

    @Override public void onReady()
    {
	listArea.refresh();
    }

    @Override public void doScanning()
    {
	if (!base.launchScanning())
	    return;
	listArea.refresh();
    }

    @Override public boolean onClick(Object obj)
    {
	if (obj == null || !(obj instanceof WifiNetwork))
	    return false;
	progressArea.clear();
	System.out.println("here");
	if (!base.launchConnection(progressArea, (WifiNetwork)obj))
	    return false;
	layouts.show(1);
	goToProgress();
	return true;
    }

    @Override public boolean isScanning()
    {
	return base.isScanning();
    }

    private void createArea()
    {
	final Actions actions = this;

	final ListParams params = new ListParams();
	params.environment = new DefaultControlEnvironment(luwrain);
	params.model = base.getListModel();
	params.appearance = new DefaultListItemAppearance(params.environment);
	params.clickHandler = (area, index, obj)->actions.onClick(obj);
	params.name = strings.appName();

	listArea = new ListArea(params){
		@Override public boolean onKeyboardEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isCommand() && !event.isModified())
			switch(event.getCommand())
			{
			case KeyboardEvent.TAB:
			    actions.goToProgress();
			    return true;
			}
		    return super.onKeyboardEvent(event);
		}
		@Override public boolean onEnvironmentEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    switch(event.getCode())
		    {
		    case EnvironmentEvent.CLOSE:
			actions.closeApp();
			return true;
		    case EnvironmentEvent.REFRESH:
			actions.doScanning();
			return true;
		    default:
			return super.onEnvironmentEvent(event);
		    }
		}
		@Override protected String noContentStr()
		{
		    return actions.isScanning()?"Идёт  поиск беспроводных сетей. Пожалуйста, подождите...":"Беспроводные сети отсутствуют";
		}
	    };

	progressArea = new ProgressArea(new DefaultControlEnvironment(luwrain), "Подключение"){
		@Override public boolean onKeyboardEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isCommand() && !event.isModified())
			switch(event.getCommand())
			{
			case KeyboardEvent.TAB:
			    actions.goToList();
			    return true;
			}
		    return super.onKeyboardEvent(event);
		}
		@Override public boolean onEnvironmentEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    switch(event.getCode())
		    {
case EnvironmentEvent.CLOSE:
actions.closeApp();
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
	return layouts.getCurrentLayout();
    }

    @Override public boolean closeApp()
    {
	//FIXME:Checking threads;
	luwrain.closeApp();
	return true;
    }

    @Override public void goToList()
    {
	luwrain.setActiveArea(listArea);
    }

    @Override public void goToProgress()
    {
	System.out.println(layouts.getCurrentIndex());
	if (layouts.getCurrentIndex() == 0)
	{
	    luwrain.setActiveArea(listArea);
	    return;
	}
	luwrain.setActiveArea(progressArea);
    }
}
