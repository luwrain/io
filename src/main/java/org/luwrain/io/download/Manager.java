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

package org.luwrain.io.download;

import java.io.*;
import java.util.*;
import java.net.*;

import org.luwrain.core.*;
import org.luwrain.util.*;

public final class Manager implements Task.Callback
{
    static private final String LOG_COMPONENT = "download";

    private final Luwrain luwrain;
        private final Vector<EntryImpl> entries = new Vector();
    private final List<Runnable> changesListeners = new Vector();

    public Manager(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

    public void load()
    {
	final Registry registry = luwrain.getRegistry();
	entries.clear();
		final int[] ids = Settings.getIds(luwrain.getRegistry());
	for(int i = 0;i < ids.length;++i)
	    try {
		entries.add(new EntryImpl(registry, ids[i], this));
	    }
	    catch(IOException ee)
	    {
		Log.error(LOG_COMPONENT, "unable to load an entry:" + ee.getClass().getName() + ":" + ee.getMessage());
	    }
	for(EntryImpl e: entries)
	    if (e.isActive())
		e.task.startAsync();
    }

    public void close()
    {
    }

    public void addDownload(URL srcUrl, File destFile) throws IOException
    {
	NullCheck.notNull(srcUrl, "srcUrl");
	NullCheck.notNull(destFile, "destFile");
	final int id = Settings.addEntry(luwrain.getRegistry(), srcUrl.toString(), destFile.getAbsolutePath());
	final EntryImpl entry = new EntryImpl(luwrain.getRegistry(), id, this);
	this.entries.add(entry);
	entry.task.startAsync();
	notifyChangesListeners();
    }

    public Entry[] getAllEntries()
    {
	return entries.toArray(new Entry[entries.size()]);
    }

    public void addChangesListener(Runnable runnable)
    {
	NullCheck.notNull(runnable, "runnable");
	for(Runnable r: changesListeners)
	    if (r == runnable)
		return;
		changesListeners.add(runnable);
    }

    public void removeChangesListener(Runnable runnable)
    {
	NullCheck.notNull(runnable, "runnable");
	for(int i = 0;i < changesListeners.size();i++)
	    if (changesListeners.get(i) == runnable)
	    {
		changesListeners.remove(i);
		return;
	    }
    }

    private void notifyChangesListeners()
    {
	for(Runnable r: changesListeners)
	    r.run();
    }

    

    	@Override public void setFileSize(Task task, long size)
    {
    }
    
	@Override public void onProgress(Task task, long bytesFetched)
    {
    }
    
	@Override public void onSuccess(Task task)
    {
    }
    
	@Override public void onFailure(Task task, Throwable throwable)
    {
    }

    public interface Entry
    {
	String getUrl();
    }

    static private final class EntryImpl implements Entry
    {
	final Task task;
	final Settings.Entry sett;
	EntryImpl(Registry registry, int id, Task.Callback callback) throws IOException
	{
	    NullCheck.notNull(registry, "registry");
	    NullCheck.notNull(callback, "callback");
	    this.sett = Settings.createEntry(registry, id);
	    final String url = sett.getUrl("");
	    final String destFile = sett.getDestFile("");
	    NullCheck.notEmpty(url, "url");
	    NullCheck.notEmpty(destFile, "destFile");
	    this.task = new Task(callback, new URL(url), new File(destFile));
	}
	boolean isActive()
	{
	    final String status = sett.getStatus("");
	    return status.equals(Settings.COMPLETED) || status.equals(Settings.FAILED);
	}
	@Override public String getUrl()
	{
	    return this.task.srcUrl.toString();
	}
    }
}
