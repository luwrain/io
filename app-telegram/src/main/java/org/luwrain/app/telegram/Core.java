// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.telegram;

import java.util.*;
import java.io.*;
import org.apache.logging.log4j.*;

import org.drinkless.tdlib.*;
import org.drinkless.tdlib.TdApi.*;

import org.luwrain.core.Log;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.queries.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;

public final class Core
{
    static private final Logger log = LogManager.getLogger();
    static private final int CHAT_NUM_LIMIT = 200;


    final Luwrain luwrain;
    final java.io.File tdlibDir;
    final Operations operations;
    final Objects objects;
    final Client client;
    final UpdatesHandler updatesHandler;
    private boolean ready = false;

    Core(Luwrain luwrain, Runnable onReady)
    {
	this.luwrain = luwrain;
	this.tdlibDir = new java.io.File(new java.io.File(luwrain.getPath("var:luwrain.telegram")), "tdlib");
	final String logFile = new java.io.File(new java.io.File(luwrain.getPath("var:luwrain.telegram")), "td.log").getAbsolutePath();
	this.objects = new Objects(luwrain);
	this.operations = newOperations();
	this.updatesHandler = newUpdates(onReady);
	this.client = Client.create(updatesHandler, null, null);
	try {
	    Client.setLogMessageHandler(0, new LogMessageHandler());
	    Client.execute(new TdApi.SetLogVerbosityLevel(0));
	    log.trace("TD log file {}", logFile);
	    log.trace("tdlib log file is " + logFile);
	    if (!(Client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile(logFile, 1 << 27, true))) instanceof TdApi.Ok))
		throw new IOError(new IOException("Write access to the current directory is required"));
	}
	catch(Exception e)
	{
		    throw new RuntimeException(e);
	}
    }

    private UpdatesHandler newUpdates(Runnable onReadyFunc)
    {
	return new UpdatesHandler(luwrain, tdlibDir, objects){

	    @Override void connectionStateUpdate(ConnectionState state)
	    {
		log.info("Connection state  {}", state.getClass().getSimpleName());
		luwrain.message(state.getClass().getSimpleName());
	    }

	    
	    @Override public void onReady()
	    {
		Core.this.ready = true;
		luwrain.runUiSafely(()->{
			operations.fillMainChatList(CHAT_NUM_LIMIT);
			onReadyFunc.run();
		    });
	    }
	    
	    @Override Client getClient()
	    {
		if (Core.this.client == null)
		    log.warn("providing a null pointer as a client to the updates handler");
		return Core.this.client;
	    }
	    
	};
    }

    private Operations newOperations()
    {
	return new Operations(luwrain, objects){
	    @Override Client getClient()
	    {
		if (Core.this.client == null)
		    log.warn("providing a null pointer as a client to operations");
		return Core.this.client;
	    }
	};
    }

    boolean isReady() { return ready; }
    UpdatesHandler.InputWaiter getInputWaiter(){ return updatesHandler.getInputWaiter(); }

        private static class LogMessageHandler implements Client.LogMessageHandler {
        @Override
        public void onLogMessage(int verbosityLevel, String message) {
            if (verbosityLevel == 0)
	    {
		log.fatal(message);
                return;
            }
	    log.error(message);
        }
    }

}
