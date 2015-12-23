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
//import org.luwrain.core.events.ThreadSyncEvent;
import org.luwrain.controls.*;
import org.luwrain.network.*;

class Base
{
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Network network;
    private Luwrain luwrain;
    private final FixedListModel listModel = new FixedListModel();
    private FutureTask task;
    private Actions actions;

    boolean init(Luwrain luwrain, Actions actions)
    {
	this.luwrain = luwrain;
	this.actions = actions;
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

    boolean launchScanning()
    {
	if (task != null && !task.isDone())
	    return false;
	task = createScanTask();
	executor.execute(task);
	return true;
    }

    private void acceptResult(WifiScanResult scanRes)
    {
    if (scanRes.type() != WifiScanResult.Type.SUCCESS)
    {
	listModel.clear();
	actions.onReady();
	return;
    }
    listModel.setItems(scanRes.networks());
    actions.onReady();
    }

    private FutureTask createScanTask()
    {
	return new FutureTask(()->{
final WifiScanResult res = network.wifiScan();
luwrain.runInMainThread(()->acceptResult(res));
	}, null);
    }
}
