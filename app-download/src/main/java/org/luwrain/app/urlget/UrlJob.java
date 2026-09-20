// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.urlget;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.luwrain.core.*;

public class UrlJob extends EmptyJob
{
    private final Luwrain luwrain;

    public UrlJob(Luwrain luwrain)
    {
	super("url");
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

    public void fetch(String url)
    {
	NullCheck.notEmpty(url, "url");
	final AtomicBoolean cancelling = new AtomicBoolean(false);
	luwrain.executeBkg(()->{
		try {
		    doFetch(url, cancelling);
		}
		catch(Throwable e)
		{
		    setInfo("main", Arrays.asList(e.getClass().getName() + ": " + e.getMessage()));
		    stop(1);
		}
	    });
    }

    private void doFetch(String url, AtomicBoolean cancelling) throws IOException
    {
	NullCheck.notEmpty(url, "url");
	NullCheck.notNull(cancelling, "cancelling");
    }
}