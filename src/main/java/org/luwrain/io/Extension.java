/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>

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

import org.luwrain.core.*;
import org.luwrain.popups.*;
import org.luwrain.io.api.duckduckgo.*;

public final class Extension extends EmptyExtension
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
	    new SimpleShortcutCommand("wiki"),
	    new SimpleShortcutCommand("download"),
	    new WebCommand(),
	    new org.luwrain.app.urlget.UrlCommand(),

	    new Command(){
	    		@Override public String getName()
		{
		    return "context-wiki";
		}
		@Override public void onCommand(Luwrain luwrain)
		{
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
			luwrain.launchApp("wiki", new String[]{text.trim()});
		}
	    },

	    	    new Command(){
		@Override public String getName()
		{
		    return "context-ddg";
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
		    final FutureTask task = new FutureTask<>(()->{
			    final Properties props = new Properties();
			    props.setProperty("kl", "ru-ru");//FIXME:
			    final InstantAnswer insAnswer = new InstantAnswer();
			    final InstantAnswer.Answer answer;
			    try {
				answer = insAnswer.getAnswer(text.trim(), props, EnumSet.noneOf(InstantAnswer.Flags.class));
			    }
			    catch(Throwable e)
			    {
				luwrain.crash(e);
				return;
			    }
				luwrain.runUiSafely(()->{
					if (answer.getType() == null)
					{
					    luwrain.playSound(Sounds.ERROR);
					    return;
					}
			    switch(answer.getType())
			    {
			    case InstantAnswer.Answer.TYPE_A:
				luwrain.message(answer.getAbsText());
				return;
			    /*
			    case InstantAnswer.Answer.TYPE_D:
			    {
				final Object res = Popups.fixedList(luwrain, answer.getHeading(), answer.getRelatedTopics());
				if (res == null || !(res instanceof InstantAnswer.RelatedTopic))
				    return;
				final InstantAnswer.RelatedTopic topic = (InstantAnswer.RelatedTopic)res;
				if (!topic.getFirstUrl().isEmpty())
				{
				    String addr = new org.luwrain.io.api.duckduckgo.Search().makeUrlFromInstantAnswer(topic.getFirstUrl());
				    luwrain.launchApp("reader", new String[]{addr});
				}
				return;
			    }
			    */
			    default:
			    luwrain.playSound(Sounds.ERROR);
			    return;
			    }
				    });
			}, null);
		    luwrain.executeBkg(task);
		}
	    },

	};
    }

    @Override public ExtensionObject[] getExtObjects(Luwrain luwrain)
    {
	return new ExtensionObject[]{
	    new org.luwrain.app.urlget.UrlJob(luwrain),

	    new Shortcut() {
		@Override public String getExtObjName() { return "download"; }
		@Override public Application[] prepareApp(String[] args)
		{
		    NullCheck.notNull(args, "args");
		    if (downloadManager == null)
			return new Application[0];
		    return new Application[]{new org.luwrain.app.download.App(downloadManager)};
		}
	    },

	    new Shortcut() {
		@Override public String getExtObjName() { return "urlget"; }
		@Override public Application[] prepareApp(String[] args)
		{
		    NullCheck.notNullItems(args, "args");
		    if (args.length == 0)
			return null;
		    return new Application[]{new org.luwrain.app.urlget.App(args[0])};
		}
	    },

new Shortcut() {
    @Override public String getExtObjName() { return "wiki"; }
		@Override public Application[] prepareApp(String[] args)
		{
		    NullCheck.notNullItems(args, "args");
		    if (args.length == 1)
			return new Application[]{new org.luwrain.app.wiki.App(args[0])};
		    return new Application[]{new org.luwrain.app.wiki.App()};
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
