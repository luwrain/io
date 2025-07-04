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
        private final List<EntryImpl> entries = new ArrayList<>();
    private final List<Runnable> changesListeners = new ArrayList<>();

    public Manager(Luwrain luwrain)
    {
	NullCheck.notNull(luwrain, "luwrain");
	this.luwrain = luwrain;
    }

    synchronized public void load()
    {
	//FIXME:newreg 	final Registry registry = luwrain.getRegistry();
	entries.clear();
	final int[] ids = null;//FIXME:newreg Settings.getIds(luwrain.getRegistry());
	for(int i = 0;i < ids.length;++i)
	    try {
		//FIXME:newreg 		entries.add(new EntryImpl(registry, ids[i], this));
	    }
	    catch(Exception ee)
	    {
		Log.error(LOG_COMPONENT, "unable to load an entry:" + ee.getClass().getName() + ":" + ee.getMessage());
	    }
	for(EntryImpl e: entries)
	    if (e.isActive())
		e.task.startAsync();
    }

    public void close()
    {
	changesListeners.clear();
	final EntryImpl[] ee = this.entries.toArray(new EntryImpl[this.entries.size()]);
	this.entries.clear();
	for(EntryImpl e: ee)
	{
	    if (!e.isActive())
		continue;
	    Log.debug(LOG_COMPONENT, "stopping download of " + e.task.srcUrl.toString() + " at " + e.bytesFetched + "/" + e.fileSize);
	    e.task.stop();
	}
    }

    synchronized public void addDownload(URL srcUrl, File destFile) throws IOException
    {
	NullCheck.notNull(srcUrl, "srcUrl");
	NullCheck.notNull(destFile, "destFile");
		Log.debug(LOG_COMPONENT, "new download: " + srcUrl.toString() + " -> " + destFile.getAbsolutePath());
		final int id = 0;//FIXME:newreg Settings.addEntry(luwrain.getRegistry(), srcUrl.toString(), destFile.getAbsolutePath());
		final EntryImpl entry = null;//FIXME:newreg new EntryImpl(luwrain.getRegistry(), id, this);
	this.entries.add(entry);
	entry.task.startAsync();
	notifyChangesListeners();
    }

    synchronized public Entry[] getAllEntries()
    {
	return entries.toArray(new Entry[entries.size()]);
    }

    synchronized public void addChangesListener(Runnable runnable)
    {
	NullCheck.notNull(runnable, "runnable");
	for(Runnable r: changesListeners)
	    if (r == runnable)
		return;
		changesListeners.add(runnable);
    }

    synchronized public void removeChangesListener(Runnable runnable)
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

    @Override synchronized public void setFileSize(Task task, long size)
    {
	NullCheck.notNull(task, "task");
	for(EntryImpl e: entries)
	    if (e.task == task)
	    {
		e.fileSize = size >= 0?size:0;
		notifyChangesListeners();
		return;
	    }
    }

	@Override synchronized public void onProgress(Task task, long bytesFetched)
    {
	NullCheck.notNull(task, "task");
	for(EntryImpl e: entries)
	    if (e.task == task)
	    {
		e.bytesFetched = bytesFetched >= 0?bytesFetched:0;
		final int percent = e.getPercent();
		if (percent != e.prevNotificationPercent)
		{
		notifyChangesListeners();
		e.prevNotificationPercent = percent;
		}
		return;
	    }
    }

	@Override synchronized public void onSuccess(Task task)
    {
	NullCheck.notNull(task, "task");
	for(EntryImpl e: entries)
	    if (e.task == task)
	    {
		e.onSuccess();
		notifyChangesListeners();
		return;
	    }
    }

	@Override synchronized public void onFailure(Task task, Throwable throwable)
    {
	NullCheck.notNull(task, "task");
	NullCheck.notNull(throwable, "throwable");
	for(EntryImpl e: entries)
	    if (e.task == task)
	    {
		e.onFailure(throwable);
		notifyChangesListeners();
		return;
	    }
    }

    public interface Entry
    {
	public enum Status {RUNNING, SUCCESS, FAILED};

	URL getUrl();
	int getPercent();
	Status getStatus();
	String getErrorInfo();
    }

    static private final class EntryImpl implements Entry
    {
	final Task task;
	final Settings.Entry sett;
	long fileSize = 0;
	long bytesFetched = 0;
	int prevNotificationPercent = -1;
	//To reduce the number of queries to the registry
	private Entry.Status statusCache = null;
	private String errorInfoCache = null;
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
	    return !status.equals(Settings.COMPLETED) && !status.equals(Settings.FAILED);
	}
	void onSuccess()
	{
	    this.sett.setStatus(Settings.COMPLETED);
	}
		void onFailure(Throwable e)
	{
	    this.sett.setStatus(Settings.FAILED);
	    this.sett.setErrorInfo(e.getClass().getName() + ":" + e.getMessage());
	}
		@Override public URL getUrl()
	{
	    return this.task.srcUrl;
	}
	@Override public Status getStatus()
	{
	    if (statusCache != null)
		return statusCache;
	    final String statusStr = sett.getStatus("");
	    switch(statusStr)
	    {
	    case Settings.COMPLETED:
		this.statusCache = Status.SUCCESS;
		return Status.SUCCESS;
	    case Settings.FAILED:
		this.statusCache = Status.FAILED;
		return Status.FAILED;
	    default:
		return Status.RUNNING;
	    }
	}
	@Override public String getErrorInfo()
	{
	    if (errorInfoCache != null)
		return errorInfoCache;
	    final String value = sett.getErrorInfo("");
	    if (value.isEmpty())
		return "";
	    errorInfoCache = value;
	    return value;
	}
	@Override public int getPercent()
	{
	    if (fileSize <= 0 || bytesFetched <= 0)
		return 0;
	    final int res = (int)((bytesFetched * 100) / fileSize);
	    return res <= 100?res:100;
	}
    }
}
