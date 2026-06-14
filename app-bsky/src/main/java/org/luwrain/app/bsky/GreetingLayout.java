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
	    public Strings getStrings() { return app.getStrings(); }

	    public void skip()
	    {
		app.setAreaLayout(app.mainLayout);
		app.getLuwrain().announceActiveArea();
	    }

	    public void save(String handle, String appPassword)
	    {
		app.conf.setHandle(handle);
		app.conf.setAppPassword(appPassword);
		app.getLuwrain().saveConf(app.conf);
		app.message(app.getStrings().wizardSaved(), Luwrain.MessageType.OK);
		app.mainLayout.updateRecords();
		app.setAreaLayout(app.mainLayout);
		app.getLuwrain().announceActiveArea();
	    }
	};
	Eval.me("wizard", controller, getStringResource(this.getClass(), "greeting.groovy"));
	setAreaLayout(wizardArea, null);
    }
}
