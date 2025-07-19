
package org.luwrain.app.mastodon;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.speech.*;
import org.luwrain.app.base.*;

public final class App extends AppBase<Strings>
{
    static final String
	LOG_COMPONENT = "notepad";

    private Data data = null;
    private Conversations conv = null;
    private MainLayout mainLayout = null;
    private StartingLayout startingLayout = null;

    public App()
    {
	super(Strings.NAME, Strings.class, "luwrain.notepad");
    }

    @Override protected AreaLayout onAppInit() throws IOException
    {
	this.data = new Data(getLuwrain());
	this.conv = new Conversations(this);
	this.mainLayout = new MainLayout(this);
	this.startingLayout = new StartingLayout(this);
	setAppName(getStrings().appName());
	if (data.sett.getToken("").trim().isEmpty())
		return startingLayout.getAreaLayout();
return mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

            Conversations getConv() { return this.conv; }
    Data getData() { return data; }
}
