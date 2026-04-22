// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.telegram;

import java.util.*;
import java.util.function.Consumer;
import org.apache.logging.log4j.*;

import org.drinkless.tdlib.*;
import org.drinkless.tdlib.TdApi.*;

import org.luwrain.core.*;

final class SyncNoMessageHandler extends SyncHandler<TdApi.Object>
{
    SyncNoMessageHandler(Luwrain luwrain, int constructor)
    {
	super(luwrain, constructor,
	      obj -> {
		  luwrain.playSound(Sounds.DONE);
	      },
	      err -> {
		  luwrain.message(err.toString(), Luwrain.MessageType.ERROR);
	      });
    }
}
