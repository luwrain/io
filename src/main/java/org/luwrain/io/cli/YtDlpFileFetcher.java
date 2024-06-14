/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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

//LWR_API 1.0

package org.luwrain.cli;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.io.*;

import static org.luwrain.core.NullCheck.*;

public final class YtDlpFileFetcher implements FileFetcher
{
    private final Luwrain luwrain;

    public YtDlpFileFetcher(Luwrain luwrain)
    {
	notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

        @Override public boolean canHandleUrl(String url)
    {
	return true;
    }


    @Override public Fetching fetchUrl(String url, String destDir, String destFileName)
    {
	final var fetching = new EmptyFileFetching("", url);
	final var job = luwrain.newJob("sys", new String[]{ "yt-dlp", url }, destDir, EnumSet.noneOf(Luwrain.JobFlags.class), new Job.Listener(){
	@Override public void onStatusChange(Job job)
	{
	    fetching.completed();
	}
	@Override public void onInfoChange(Job job, String infoType, List<String> value)
	{
	}
				       });
	return fetching;
    }

        @Override public Set<Flags> getFlags()
    {
	return EnumSet.noneOf(Flags.class);
    }

    @Override public String getExtObjName()
    {
	return "yt-dlp";
    }
}
