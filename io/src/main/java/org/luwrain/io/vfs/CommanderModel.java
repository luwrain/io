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
//import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.CommanderArea.EntryType;

import static java.util.Objects.*;

public class CommanderModel implements CommanderArea.Model<FileObject>
{
    static private final Logger log = LogManager.getLogger();

    protected final FileSystemManager manager;

    public CommanderModel(FileSystemManager manager)
    {
	this.manager = requireNonNull(manager, "manager can't be null");
    }

    @Override public FileObject[] getEntryChildren(FileObject entry)
    {
	requireNonNull(entry, "entry can't be null");
	log.trace("Reading children of " + entry);
	try {
	    entry.refresh();
	    final FileObject[]children = entry.getChildren();
	    final FileObject parent = entry.getParent();
	    if (parent == null)
		return children;
	    final List<FileObject> res = new ArrayList<>();
	    res.add(parent);
	    for(FileObject f: children)
		res.add(f);
	    log.trace("Read " + res.size() + " entries in " + entry);
	    return res.toArray(new FileObject[res.size()]);
	}
	catch(Throwable e)
	{
	    log.error("Unable to get children of " + entry, e);
	    return null;
	}
    }

    public FileSystemManager getFileSystemManager()
    {
	return manager;
    }

    @Override public EntryType getEntryType(FileObject currentLocation, FileObject entry)
    {
	requireNonNull(entry, "entry can't be null");
	try {
	    if (currentLocation.getParent() != null && currentLocation.getParent().equals(entry))
		return EntryType.PARENT;
	    if (entry instanceof org.apache.commons.vfs2.provider.local.LocalFile)
	    {
		final Path path = Paths.get(entry.getName().getPath());
		if (Files.isSymbolicLink(path))
		    return Files.isDirectory(path)?EntryType.SYMLINK_DIR:EntryType.SYMLINK;
		if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
		    return EntryType.DIR;
		if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS))
		    return EntryType.REGULAR;
		return EntryType.SPECIAL;
	    }
	    return entry.getType().hasChildren()?EntryType.DIR:EntryType.REGULAR;
	}
	catch(org.apache.commons.vfs2.FileSystemException e)
	{
	    log.error("Unable to get the type of " + entry, e);
	    return EntryType.REGULAR;
	}
    }

    @Override public FileObject getEntryParent(FileObject entry)
    {
	requireNonNull(entry, "entry can't be null");
	try {
	    return entry.getParent();
	}
	catch(org.apache.commons.vfs2.FileSystemException e)
	{
	    log.error("Unable to get the parent of " + entry, e);
	    return null;
	}
    }
}
