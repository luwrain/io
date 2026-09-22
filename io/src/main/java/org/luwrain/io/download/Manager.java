// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.download;

import java.io.*;
import java.util.*;
import java.net.*;

import org.luwrain.core.*;
import org.luwrain.util.*;

public final class Manager implements Task.Callback, AutoCloseable
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
	entries.clear();
	final DownloadConfig conf = luwrain.loadConf(DownloadConfig.class);
	if (conf == null || conf.getItems() == null)
	    return;
	for(DownloadConfig.Item item: conf.getItems())
	{
	    try {
		final EntryImpl entry = new EntryImpl(item, this);
		entries.add(entry);
	    }
	    catch(Exception ee)
	    {
		Log.error(LOG_COMPONENT, "unable to load an entry:" + ee.getClass().getName() + ":" + ee.getMessage());
	    }
	}
	for(EntryImpl e: entries)
	    if (e.isActive())
		e.task.startAsync();
    }

    @Override public void close()
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
	final DownloadConfig.Item item = new DownloadConfig.Item();
	item.setUrl(srcUrl.toString());
	item.setDestFile(destFile.getAbsolutePath());
	final EntryImpl entry = new EntryImpl(item, this);
	this.entries.add(entry);
	saveConf();
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
		saveConf();
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
		saveConf();
		notifyChangesListeners();
		return;
	    }
    }

    private void saveConf()
    {
	final DownloadConfig conf = new DownloadConfig();
	final List<DownloadConfig.Item> items = new ArrayList<>();
	for(EntryImpl e: entries)
	    items.add(e.item);
	conf.setItems(items);
	luwrain.saveConf(conf);
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
	final DownloadConfig.Item item;
	long fileSize = 0;
	long bytesFetched = 0;
	int prevNotificationPercent = -1;
	private Entry.Status statusCache = null;
	private String errorInfoCache = null;

	EntryImpl(DownloadConfig.Item item, Task.Callback callback) throws IOException
	{
	    NullCheck.notNull(item, "item");
	    NullCheck.notNull(callback, "callback");
	    this.item = item;
	    final String url = item.getUrl();
	    final String destFile = item.getDestFile();
	    NullCheck.notEmpty(url, "url");
	    NullCheck.notEmpty(destFile, "destFile");
	    this.task = new Task(callback, new URL(url), new File(destFile));
	}

	boolean isActive()
	{
	    final String status = item.getStatus();
	    return status == null || (!status.equals(DownloadConfig.COMPLETED) && !status.equals(DownloadConfig.FAILED));
	}

	void onSuccess()
	{
	    this.item.setStatus(DownloadConfig.COMPLETED);
	    this.statusCache = Status.SUCCESS;
	}

	void onFailure(Throwable e)
	{
	    this.item.setStatus(DownloadConfig.FAILED);
	    this.item.setErrorInfo(e.getClass().getName() + ":" + e.getMessage());
	    this.statusCache = Status.FAILED;
	    this.errorInfoCache = e.getClass().getName() + ":" + e.getMessage();
	}

	@Override public URL getUrl()
	{
	    return this.task.srcUrl;
	}

	@Override public Status getStatus()
	{
	    if (statusCache != null)
		return statusCache;
	    final String statusStr = item.getStatus();
	    if (statusStr == null)
		return Status.RUNNING;
	    switch(statusStr)
	    {
	    case DownloadConfig.COMPLETED:
		this.statusCache = Status.SUCCESS;
		return Status.SUCCESS;
	    case DownloadConfig.FAILED:
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
	    final String value = item.getErrorInfo();
	    if (value == null || value.isEmpty())
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