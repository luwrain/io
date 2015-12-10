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

package org.luwrain.network;

import java.net.*;
import org.luwrain.core.*;
import org.luwrain.popups.Popups;

import org.luwrain.app.wifi.WifiApp;

public class Extension extends org.luwrain.core.extensions.EmptyExtension
{
    private final Network network = new Network();

    @Override public Command[] getCommands(Luwrain luwrain)
    {
	return new Command[]{

	    new Command(){
		@Override public String getName()
		{
		    return "wifi";
		}
		@Override public void onCommand(Luwrain luwrain)
		{
		    luwrain.launchApp("wifi");
		}
	    },

};
    }

    @Override public Shortcut[] getShortcuts(Luwrain luwrain)
    {
	final Shortcut wifi = new Shortcut() {
		@Override public String getName()
		{
		    return "wifi";
		}
		@Override public Application[] prepareApp(String[] args)
		{
		    return new Application[]{new WifiApp()};
		}
	    };

	return new Shortcut[]{wifi};
    }

    @Override public SharedObject[] getSharedObjects(Luwrain luwrain)
    {
	return new SharedObject[]{
	    new SharedObject(){
		@Override public String getName()
		{
		    return "luwrain.network";
		}
		@Override public Object getSharedObject()
		{
		    return network;
		}
	    },
	};
    }
}
