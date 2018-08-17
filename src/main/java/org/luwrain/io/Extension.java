/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.io;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import org.luwrain.base.*;
import org.luwrain.core.*;
import org.luwrain.io.api.duckduckgo.*;

public class Extension extends org.luwrain.core.extensions.EmptyExtension
{
    private org.luwrain.io.download.Manager downloadManager = null;

    @Override public String init(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.downloadManager = new org.luwrain.io.download.Manager(luwrain);
	this.downloadManager.load();
	return null;
    }
    
    @Override public Command[] getCommands(Luwrain luwrain)
    {
	return new Command[]{

	    new Command(){
		@Override public String getName()
		{
		    return "download";
		}
		@Override public void onCommand(Luwrain luwrain)
		{
		    luwrain.launchApp("download");
		}
	    },

	    	    new Command(){
		@Override public String getName()
		{
		    return "ddg";
		}
		@Override public void onCommand(Luwrain luwrain)
			{
			    NullCheck.notNull(luwrain, "luwrain");
			    final String region = luwrain.getActiveAreaText(Luwrain.AreaTextType.REGION, false);
			    final String word = luwrain.getActiveAreaText(Luwrain.AreaTextType.WORD, false);
			    final String text;
			    if (region != null && !region.trim().isEmpty())
				text = region; else
				text = word;
			    if (text == null || text.trim().isEmpty())
			    {
				luwrain.playSound(Sounds.BLOCKED);
				return;
			    }
		    final FutureTask task = new FutureTask(()->{
			    final Properties props = new Properties();
			    props.setProperty("kl", "ru-ru");//FIXME:
			    final InstantAnswer insAnswer = new InstantAnswer();
			    final InstantAnswer.Answer answer;
			    try {
				answer = insAnswer.getAnswer(text.trim(), props, EnumSet.noneOf(InstantAnswer.Flags.class));
			    }
			    catch(IOException e)
			    {
				luwrain.message(luwrain.i18n().getExceptionDescr(e), Luwrain.MessageType.ERROR);
				return;
			    }
			    if (answer.getType() == InstantAnswer.Answer.Type.A)
			    {
				luwrain.message(answer.getAbsText());
				return;
			    }
			    luwrain.playSound(Sounds.ERROR);
			}, null);
		    luwrain.executeBkg(task);
		}
	    },


	};
    }

    @Override public ExtensionObject[] getExtObjects(Luwrain luwrain)
    {
	return new ExtensionObject[]{

	    new Shortcut() {
		@Override public String getExtObjName()
		{
		    return "download";
		}
		@Override public Application[] prepareApp(String[] args)
		{
		    NullCheck.notNull(args, "args");
		    if (downloadManager == null)
			return new Application[0];
		    return new Application[]{new org.luwrain.app.download.App(downloadManager)};
		}
	    },

	};
    }

@Override public void close()
    {
	if (downloadManager != null)
	    downloadManager.close();
    }
}
