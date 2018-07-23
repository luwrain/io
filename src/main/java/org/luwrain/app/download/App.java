/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.app.download;

import java.net.*;
import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.core.queries.*;

public class App implements Application, MonoApp
{
    private Luwrain luwrain = null;
    private Strings strings = null;
    private Base base = null;
    private ListArea listArea = null;

    private final org.luwrain.io.download.Manager manager;

    App(org.luwrain.io.download.Manager manager)
    {
	NullCheck.notNull(manager, "manager");
	this.manager = manager;
    }

    @Override public InitResult onLaunchApp(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	final Object o = luwrain.i18n().getStrings(Strings.NAME);
	if (o == null || !(o instanceof Strings))
	    return new InitResult(InitResult.Type.NO_STRINGS_OBJ, Strings.NAME);
	this.strings = (Strings)o;
	this.luwrain = luwrain;
	this.base = new Base( luwrain, strings);
	createArea();
	return new InitResult();
    }

    private void createArea()
    {
	final ListArea.Params params = new ListArea.Params();
	params.context = new DefaultControlEnvironment(luwrain);
	//	params.model = base.getListModel();
	params.model = new ListUtils.FixedModel();
	params.appearance = new Appearance(luwrain, strings);
	//	params.clickHandler = (area, index, obj)->onConnect(obj);
	params.name = strings.appName();

	this.listArea = new ListArea(params){
		@Override public boolean onInputEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isSpecial() && !event.isModified())
			switch(event.getSpecial())
			{
			case ESCAPE:
closeApp();
return true;
			}
		    return super.onInputEvent(event);
		}
		@Override public boolean onSystemEvent(EnvironmentEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.getType() != EnvironmentEvent.Type.REGULAR )
			return super.onSystemEvent(event);
		    switch(event.getCode())
		    {
			/*
		    case ACTION:
			if (ActionEvent.isAction(event, "disconnect"))
			    return onDisconnect();
			return false;
			*/
		    case CLOSE:
			closeApp();
			return true;
		    default:
			return super.onSystemEvent(event);
		    }
		}
		@Override public Action[] getAreaActions()
		{
		    return new Action[0];
		}
		/*
		@Override protected String noContentStr()
		{
		    return base.isScanning()?strings.scanningInProgress():strings.noWifiNetworks();
		}
		*/
	    };
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

    @Override public AreaLayout getAreaLayout()
    {
	return new AreaLayout(listArea);
    }

    @Override public void closeApp()
    {
	luwrain.closeApp();
    }
}
