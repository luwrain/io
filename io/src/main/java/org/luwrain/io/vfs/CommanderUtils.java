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
//import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;

import org.luwrain.controls.*;
import org.luwrain.controls.CommanderArea.EntryType;

import static java.util.Objects.*;
import static org.luwrain.controls.CommanderUtils.*;

public final class CommanderUtils
{
    static private final Logger log = LogManager.getLogger();

    static public class NoHiddenFilter implements CommanderArea.Filter<FileObject>
    {
	@Override public boolean commanderEntrySuits(FileObject entry)
	{
	    requireNonNull(entry, "entry can't be null");
	    try {
	    return !entry.isHidden();
	    }
	    catch(java.io.IOException e)
	    {
		log.error("Unable to get attributes of " + entry.toString() + ":" + e.getClass().getName() + ":" + e.getMessage());
		return true;
	    }
	}
    }

    static public CommanderArea.Params<FileObject> createParams(ControlContext context) throws org.apache.commons.vfs2.FileSystemException
    {
	requireNonNull(context, "context can't be null");
	final CommanderArea.Params<FileObject> params = new CommanderArea.Params<FileObject>();
	final FileSystemManager manager = VFS.getManager();
	params.context = context;
	params.model = new CommanderModel(manager);
	params.appearance = new CommanderAppearance(context, manager);
	params.filter = new AllEntriesFilter<FileObject>();
	params.comparator = new ByNameComparator<>();
	return params;
    }

    	    static public FileObject createInitialFileObject(CommanderModel model, String path) throws org.apache.commons.vfs2.FileSystemException
    {
requireNonNull(model, "model can't be null");
requireNonNull(path, "path can't be null");
final var opts = new FileSystemOptions();                                                                              
//	FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true); 
//	FtpFileSystemConfigBuilder.getInstance( ).setUserDirIsRoot(opts,true);                                                                            
	return model.getFileSystemManager().resolveFile(path, opts);
    }
}
