// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.telegram;

import java.util.*;
import java.util.function.Consumer;
import org.apache.logging.log4j.*;

import org.drinkless.tdlib.*;
import org.drinkless.tdlib.TdApi.*;

import org.luwrain.core.*;

public class SyncHandler<O> implements Client.ResultHandler
{
    static private final Logger log = LogManager.getLogger();

    final Luwrain luwrain;
    final int constructor;
    final Consumer<O> onSuccess;
    final Consumer<TdApi.Error> onError;
    
    public SyncHandler(Luwrain luwrain, int constructor, Consumer<O> onSuccess, Consumer<TdApi.Error> onError)
    {
	this.luwrain = luwrain;	    
	this.constructor = constructor;
	this.onSuccess = onSuccess;
	this.onError = onError;
    }

    SyncHandler(Luwrain luwrain, int constructor, Consumer<O> onSuccess)
    {
	this(luwrain, constructor, onSuccess, null);
    }
    
    @Override public void onResult(TdApi.Object object)
    {
	if (object == null)
	    return;
	if (object.getConstructor() == constructor)
	{
	    luwrain.runUiSafely(() -> onSuccess.accept((O)object));
	    return;
	}
	if (object.getConstructor() == TdApi.Error.CONSTRUCTOR)
	{
	    final TdApi.Error error = (TdApi.Error)object;
	    log.error("TdApi error: {} {}", String.valueOf(constructor), error.toString());
	    if (onError != null)
		luwrain.runUiSafely(() -> onError.accept(error));
	    return;
	}
	log.warn("Unknown response for {}: {}", String.valueOf(constructor), object.toString());
    }
}
