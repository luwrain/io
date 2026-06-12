// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.openmeteo.layouts;

import java.util.*;
import org.apache.logging.log4j.*;

import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.app.openmeteo.*;

import static java.util.Objects.*;

public final class OptionsLayout extends LayoutBase
{
    static private final Logger log = LogManager.getLogger();

    static private final String
	LATITUDE = "latitude",
	LONGITUDE = "longitude";

    final App app;
    final FormArea form;

    public OptionsLayout(App app, ActionHandler close)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();
	form = new FormArea(getControlContext(), s.optionsAreaName());
	form.addEdit(LATITUDE, s.latitudeEdit(), requireNonNullElse(app.conf.getDefaultLatitude(), ""));
	form.addEdit(LONGITUDE, s.longitudeEdit(), requireNonNullElse(app.conf.getDefaultLongitude(), ""));
	setAreaLayout(form, null);
	setOkHandler(() -> {
		app.conf.setDefaultLatitude(form.getEnteredText(LATITUDE));
		app.conf.setDefaultLongitude(form.getEnteredText(LONGITUDE));
		app.getLuwrain().saveConf(app.conf);
		close.onAction();
		return true;
	    });
	log.debug("OptionsLayout: close handler set");
	setCloseHandler(close);
    }
}
