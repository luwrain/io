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

import java.util.concurrent.*;

import org.luwrain.core.*;
import org.luwrain.core.events.ThreadSyncEvent;
import org.luwrain.controls.*;
import org.luwrain.network.*;

class Base
{
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Network network;
    private Luwrain luwrain;
    private final FixedListModel listModel = new FixedListModel();
    private FutureTask task;

    boolean init(Luwrain luwrain)
    {
	this.luwrain = luwrain;
	final Object o = luwrain.getSharedObject("luwrain.network");
	if (o == null || !(o instanceof Network))
	    return false;
	network = (Network)o;
	return true;
    }

    ListArea.Model getListModel()
    {
	return listModel;
    }

    boolean launchScanning(Area destArea)
    {
	if (task != null && !task.isDone())
	    return false;
	task = createScanTask(destArea);
	executor.execute(task);
	return true;
    }

    void onReady()
    {
	Object res;
	try {
	    res = task.get();
	task = null;
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    luwrain.message(e.getMessage(), Luwrain.MESSAGE_ERROR);
	    return;
	}
    if (res == null || !(res instanceof WifiScanResult))
	//FIXME:message
	return;
    final WifiScanResult scanRes = (WifiScanResult)res;
    if (scanRes.type() != WifiScanResult.Type.OK)
	//FIXME:message;
	return;
    }

    private FutureTask createScanTask(final Area destArea)
    {
	final Luwrain l = luwrain;
	final Network n = network;
	return new FutureTask(()->{
		final Object res = n.wifiScan();
		l.enqueueEvent(new ThreadSyncEvent(destArea));//FIXME:
		return res;
	});
    }
}
