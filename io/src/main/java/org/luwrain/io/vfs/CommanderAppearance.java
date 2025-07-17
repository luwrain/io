/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.io.vfs;

import java.util.*;
import java.nio.file.*;

import org.apache.logging.log4j.*;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.auth.*;
import org.apache.commons.vfs2.impl.*;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.CommanderArea.EntryType;

import static java.util.Objects.*;
import static org.luwrain.controls.CommanderUtils.*;

public class CommanderAppearance implements CommanderArea.Appearance<FileObject>
    {
	static private final Logger log = LogManager.getLogger();

	protected final ControlContext context;
	protected final FileSystemManager manager;

	public CommanderAppearance(ControlContext context, FileSystemManager manager)
	{
	    this.context = requireNonNull(context, "content can't be null");
	    this.manager = requireNonNull(manager, "manager can't be null");
	}
	
	@Override public String getCommanderName(FileObject entry)
	{
	    NullCheck.notNull(entry, "entry");
	    return entry.getName().getPath();
	}
	
	@Override public void announceLocation(FileObject entry)
	{
	    NullCheck.notNull(entry, "entry");
	    if (entry.getName().getPath().equals("/"))
		context.say(context.getStaticStr("CommanderRoot"), Sounds.COMMANDER_LOCATION); else
		context.say(context.getSpeakableText(entry.getName().getBaseName(), Luwrain.SpeakableTextType.PROGRAMMING), Sounds.COMMANDER_LOCATION);
	}
	
	@Override public String getEntryText(FileObject entry, EntryType type, boolean marked)
	{
	    NullCheck.notNull(entry, "entry");
	    //type may be null
	    if (type != null  && type == EntryType.PARENT)
		return "..";
	    return entry.getName().getBaseName();
	}

	@Override public void announceEntry(FileObject entry, CommanderArea.EntryType type, boolean marked)
	{
	    NullCheck.notNull(entry, "entry");
	    NullCheck.notNull(type, "type");
	    final String name = context.getSpeakableText(entry.getName().getBaseName(), Luwrain.SpeakableTextType.PROGRAMMING);
	    defaultEntryAnnouncement(context, name, type, marked);
	}
    }
