// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.url;

import org.luwrain.core.*;
import org.luwrain.core.annotations.*;
import static org.luwrain.popups.Popups.*;

@Cmd(
     name = "open-url",
     title = { "en=Open URL", "ru=Открыть URL" }
     )
public final class OpenUrlCommand extends DefaultCommand
{
    public OpenUrlCommand() { super("open-url"); }
    @Override public void onCommandImpl(Luwrain luwrain)
    {
	final var url = textNotEmpty(luwrain, "URL:", "URL:", "https://");
	if (url == null)
	    return;
	luwrain.message(url);
    }
}
