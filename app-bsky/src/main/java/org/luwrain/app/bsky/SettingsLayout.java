// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bsky;

import java.util.*;
import org.apache.logging.log4j.*;

import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.app.bsky.*;

import static java.util.Objects.*;

public final class SettingsLayout extends LayoutBase
{
    static private final Logger log = LogManager.getLogger();

    static private final String
	HANDLE = "handle",
	APP_PASSWORD = "app-password";

    final App app;
    final FormArea form;

    public SettingsLayout(App app, ActionHandler close)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();
	form = new FormArea(getControlContext(), s.settingsAreaName());
	form.addEdit(HANDLE, s.handleEdit(), requireNonNullElse(app.conf.getHandle(), ""));
	form.addEdit(APP_PASSWORD, s.appPasswordEdit(), requireNonNullElse(app.conf.getAppPassword(), ""));
	setAreaLayout(form, null);
	setOkHandler(() -> {
		app.conf.setHandle(form.getEnteredText(HANDLE));
		app.conf.setAppPassword(form.getEnteredText(APP_PASSWORD));
		app.getLuwrain().saveConf(app.conf);
		close.onAction();
		return true;
	    });
	log.debug("Setting close handler");
	setCloseHandler(close);
    }
}
