// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bsky;

import java.io.*;
import org.apache.logging.log4j.*;
import groovy.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.wizard.*;
import org.luwrain.app.base.*;
import org.luwrain.app.bsky.api.*;

import static org.luwrain.util.ResourceUtils.*;

final class GreetingLayout extends LayoutBase
{
    static private final Logger log = LogManager.getLogger();

    final App app;
    final WizardArea wizardArea;
    final WizardGroovyController controller;

    GreetingLayout(App app) throws IOException
    {
	super(app);
	this.app = app;
	wizardArea = new WizardArea(getControlContext());
	controller = new WizardGroovyController(getLuwrain(), wizardArea)
	{
	    public Strings getStrings()
	    {
		return app.getStrings();
	    }

	    public void signUp(String mail, String handle, String passwd)
	    {
		final var api = new BlueSkyApi();
		try {
		final var authData = api.createAccount(mail, handle, passwd, null);
				app.conf.setAuthData(authData);
		app.getLuwrain().saveConf(app.conf);

		}
		catch(Exception e)
		{
		    app.message(e.getMessage(), Luwrain.MessageType.ERROR);
		    return;
		}
		app.message("OK");
	    }

	    


	};
	Eval.me("wizard", controller, getStringResource(this.getClass(), "greeting.groovy"));
	setAreaLayout(wizardArea, null);
    }
}
