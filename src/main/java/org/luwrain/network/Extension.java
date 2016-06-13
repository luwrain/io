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

package org.luwrain.network;

import java.util.concurrent.*;
import java.net.*;

import org.luwrain.core.*;
import org.luwrain.popups.Popups;

import org.luwrain.app.wifi.WifiApp;

public class Extension extends org.luwrain.core.extensions.EmptyExtension
{
    private Network network;

    @Override public String init(Luwrain luwrain)
    {
	network = new Network(luwrain);
	checkDefaultNetwork(luwrain);
	return null;
    }

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

    private void checkDefaultNetwork(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	Log.debug("network", "checking default wifi network");
	org.luwrain.network.Settings.Network settings = org.luwrain.network.Settings.createNetwork(luwrain.getRegistry());
	final String networkName = settings.getDefaultWifiNetwork("");
	if (networkName.isEmpty())
	{
	    Log.debug("network", "no default wifi network");
	    return;
	}
	final FutureTask task = createConnectingTask(luwrain, networkName);
	Executors.newSingleThreadExecutor().execute(task);
    }

    private FutureTask createConnectingTask(Luwrain luwrain, String networkName)
    {
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(networkName, "networkName");
	return new FutureTask(()->{
		Log.debug("network", "trying to connect to default network \'" + networkName + "\'");
		final WifiScanResult res = network.wifiScan();
		if (res == null || res.type() != WifiScanResult.Type.SUCCESS)
		    return;
		WifiNetwork wifiNetwork = null;
		for(WifiNetwork n: res.networks())
		    if (n.name().equals(networkName))
			wifiNetwork = n;
		if (wifiNetwork == null)
		{
		    Log.error("network", "no wifi network with name \'" + networkName + "\'");
		    return;
		}
		if (wifiNetwork.hasPassword())
		{
		    org.luwrain.network.Settings.WifiNetwork settings = org.luwrain.network.Settings.createWifiNetwork(luwrain.getRegistry(), wifiNetwork);
		    wifiNetwork.setPassword(settings.getPassword(""));
		}
		if (network.wifiConnect(wifiNetwork, (line)->Log.debug("network", "connecting:" + line)))
		    Log.info("network", "connected to default network \'" + networkName + "\'"); else
		    Log.error("network", "unable to connect to default network \'" + networkName + "\'");
	}, null);
    }
}
