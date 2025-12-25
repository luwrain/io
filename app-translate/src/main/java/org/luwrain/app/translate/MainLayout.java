// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.translate;

import java.util.*;
import java.io.*;

import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.io.api.yandex_gpt.*;
import org.luwrain.app.translate.layouts.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.controls.ConsoleArea.*;
import static org.luwrain.util.FileUtils.*;

final class MainLayout extends LayoutBase 
{
    static private final Logger log = LogManager.getLogger();

    final EditArea fromArea, toArea;
    final App app;

    MainLayout(App app)
    {
	super(app);
	final var s = app.getStrings();
	this.app = app;
	this.fromArea = new EditArea(editParams(p -> {
		    p.name = s.firstLangAreaName();
		}));
		this.toArea = new EditArea(editParams(p ->{
			    p.name = s.secondLangAreaName();
		}));
	setPropertiesHandler(fromArea, a -> new OptionsLayout(app, getReturnAction()));
	setAreaLayout(AreaLayout.LEFT_RIGHT, fromArea, null, toArea, null);
setOkHandler(() -> {
		app.message("Proba");
		return true;
	    });
    }

}
