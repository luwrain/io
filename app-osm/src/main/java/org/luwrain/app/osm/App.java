// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.osm;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.core.annotations.*;
import org.luwrain.io.api.osm.nominatim.*;

@AppNoArgs(name = "osm",
	   title = {"en=Maps", "ru=Карты"})
public final class App extends AppBase<Strings>
{
    Conv conv = null;
    MainLayout mainLayout = null;
    NominatimClient nominatim = null;

    public App()
    {
	super(Strings.class, "luwrain.commander");
    }

    @Override public AreaLayout onAppInit()
    {
	nominatim = new NominatimClient();
	conv = new Conv(this);
	mainLayout = new MainLayout(this);
	setAppName(getStrings().appName());
	return mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }
}
