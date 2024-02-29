/*
   Copyright 2012-2021 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.app.urlget;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import org.luwrain.core.*;
import org.luwrain.core.Job.*;
import org.luwrain.util.*;

import static org.luwrain.core.Job.*;

public class UrlJob implements Job
{
    private final Luwrain luwrain;

    public UrlJob(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

        @Override public Instance launch(Listener listener, String[] args, String dir)
    {
	NullCheck.notNullItems(args, "args");
	NullCheck.notNull(dir, "dir");
	final AtomicBoolean cancelling = new AtomicBoolean(false);
	final EmptyJobInstance instance = new EmptyJobInstance(listener, "URL"){
		@Override public void stop()
		{
		    cancelling.set(true);
		}
	    };
	luwrain.executeBkg(new FutureTask<Object>(()->{
		    try {
			fetch("https://luwrain.org/", instance, cancelling);
		    }
		    catch(Throwable e)
		    {
			instance.setInfo("main", Arrays.asList(e.getClass().getName() + ": " + e.getMessage()));
						    instance.stop(1);
		    }

	}, null));
	return instance;
    }

    private void fetch(String url, EmptyJobInstance instance, AtomicBoolean cancelling) throws IOException
    {
	NullCheck.notEmpty(url, "url");
	NullCheck.notNull(instance, "instance");
	NullCheck.notNull(cancelling, "cancelling");

    }

    @Override public Set<Flags> getJobFlags()
    {
	return EnumSet.noneOf(Flags.class);
	}

    @Override public String getExtObjName()
    {
	return "url";
    }
}
