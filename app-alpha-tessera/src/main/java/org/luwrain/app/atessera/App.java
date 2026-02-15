// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.atessera;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import io.grpc.*;
import alpha4.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.core.annotations.*;

import static java.util.Objects.*;
import static org.luwrain.util.TextUtils.*;
import static alpha4.PublicationsGrpc.*;

@AppNoArgs(
	   name = "a4",
	   title = { "en=LUWRAIN Social", "ru=LUWRAIN Social" },
	   category = StarterCategory.COMMUNICATIONS)
public final class App extends AppBase<Strings>
{
    static public final String ENDPOINT = "https://luwrain.social";

    public Conv conv = null;
    public Config conf = null;
    public ManagedChannel channel = null;
    private MainLayout mainLayout = null;
    private Set<String> nbspAfterWords = new HashSet<>(Arrays.asList("В", "НА", "ПОД"));

    public App() { super(Strings.class, "luwrain.commander"); }

    @Override public AreaLayout onAppInit()
    {
	conf = getLuwrain().loadConf(Config.class);
	if (conf == null)
	{
	    conf = new Config();
	    getLuwrain().saveConf(conf);
	}
	conv = new Conv(this);
		final String target = "localhost:4040";
channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();
	mainLayout = new MainLayout(this);
	setAppName(getStrings().appName());
	if (!requireNonNullElse(conf.getAccessToken(), "").trim().isEmpty())
	    mainLayout.updateMainList();
	return mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    public String translateUserInput(String line, int pos, String text)
    {
	final var lastWord = getLastWord(line, pos).toUpperCase();
	if (text.equals(" ") && nbspAfterWords.contains(lastWord))
	    return "~";
	return text;
    }

private Alpha4Credentials getCredentials()
    {
	return new Alpha4Credentials(conf.getAccessToken());
    }

    boolean okAnswer(String type, String message)
    {
	if (type.equals("OK"))
	return true;
	message(message, Luwrain.MessageType.ERROR);
	return false;
    }

    boolean isReady()
    {
	return true;
    }

    public PublicationsBlockingStub getPubl()
    {
return alpha4.PublicationsGrpc.newBlockingStub(channel).withCallCredentials(getCredentials());
    }
}
