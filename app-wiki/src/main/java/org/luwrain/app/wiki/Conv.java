// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.wiki;

import java.net.*;
import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;
import org.luwrain.io.api.mediawiki.*;
import org.luwrain.popups.*;

import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.controls.ConsoleUtils.*;

final class Conv
{
    private final Luwrain luwrain;
    private final Strings strings;

    Conv(App app)
    {
	this.luwrain = app.getLuwrain();
	this.strings = app.getStrings();
    }

    String newServerName()
    {
	return Popups.textNotEmpty(luwrain, strings.newServerPopupName(), strings.newServerPopupPrefix(), "");
    }

    boolean confirmServerDeleting(Server server)
    {
	return Popups.confirmDefaultNo(luwrain, strings.serverDeletingPopupName(), strings.serverDeletingPopupText(server.getName()));
    }
}
