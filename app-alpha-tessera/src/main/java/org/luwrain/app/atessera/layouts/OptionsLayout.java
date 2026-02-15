// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.atessera.layouts;

import java.util.*;
import org.apache.logging.log4j.*;

import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.app.atessera.*;

import static java.util.Objects.*;

public final class OptionsLayout extends LayoutBase
{
    static private final Logger log = LogManager.getLogger();

static private final String
    ACCESS_TOKEN = "access-token",
	SYSTEM_PROMPT = "system-prompt";

    final App app;
    final FormArea form;

    public OptionsLayout(App app, ActionHandler close)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();
	form = new FormArea(getControlContext(), s.optionsAreaName());
		form.addEdit(ACCESS_TOKEN, s.accessTokenEdit(), requireNonNullElse(app.conf.getAccessToken(), ""));
				form.addEdit(SYSTEM_PROMPT, s.systemPromptEdit(), requireNonNullElse(app.conf.getSystemPrompt(), ""));
			setAreaLayout(form, null);
			setOkHandler(() -> {
				app.conf.setAccessToken(form.getEnteredText(ACCESS_TOKEN));
								app.conf.setSystemPrompt(form.getEnteredText(SYSTEM_PROMPT));
				app.getLuwrain().saveConf(app.conf);
				close.onAction();
				return true;
			    });
			log.debug("Setting close handler");
			setCloseHandler(close);
		    }
}
