// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.github;

import java.util.*;
import org.luwrain.core.*;
import org.luwrain.app.base.*;
import org.luwrain.core.annotations.*;

import static java.util.Objects.*;

@AppNoArgs(name = "github",
	   title = {"en=GitHub", "ru=GitHub"})
public final class App extends AppBase<Strings>
{
    public Config conf;
    Conv conv = null;
    MainLayout mainLayout = null;

    public App()
    {
	super(Strings.class, "luwrain.commander");
    }

    @Override public AreaLayout onAppInit()
    {
	conf = requireNonNullElse(getLuwrain().loadConf(Config.class), new Config());
	conf.setAccounts(requireNonNullElse(conf.getAccounts(), new ArrayList<>()));
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

    public Conv getConv()
    {
	return conv;
    }
}
