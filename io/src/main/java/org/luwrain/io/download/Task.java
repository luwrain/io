// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.download;


import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.net.*;
import org.apache.logging.log4j.*;

import okhttp3.*;

import org.luwrain.core.*;
import org.luwrain.util.*;

public final class Task implements Runnable
{
    static private final Logger log = LogManager.getLogger();
    static private final int MAX_ATTEMPT_COUNT = 32;
    static private final long BACKSTEP = 2048;

    public interface Callback
    {
	void setFileSize(Task task, long size);
	void onProgress(Task task, long bytesFetched);
	void onSuccess(Task task);
	void onFailure(Task task, Throwable throwable);
    }

    public final Callback callback;
    public final URL srcUrl;
    public File destFile;

    private final OkHttpClient httpClient;
    private volatile Call currentCall = null;

    //For asynchronous launching
    private Thread thread = null;
    private volatile boolean interrupting = false;

    public Task(Callback callback, URL srcUrl, File destFile)
    {
	NullCheck.notNull(callback, "callback");
	NullCheck.notNull(srcUrl, "srcUrl");
	NullCheck.notNull(destFile, "destFile");
	this.callback = callback;
	this.srcUrl = srcUrl;
	this.destFile = destFile;
	this.httpClient = new OkHttpClient.Builder()
	    .followRedirects(true)
	    .followSslRedirects(true)
	    .connectTimeout(15, TimeUnit.SECONDS)
	    .readTimeout(15, TimeUnit.SECONDS)
	    .build();
    }

    public void startSync()
    {
	this.interrupting = false;
	try {
	    for(int i = 0;i < MAX_ATTEMPT_COUNT;++i)
	    {
		if (this.interrupting)
		    return;
		try {
		    attempt();
		    if (!this.interrupting)
			callback.onSuccess(this);
		    return;
		}
		catch(org.luwrain.util.Connections.InvalidHttpResponseCodeException e)
		{
		    log.error("Downloading failed: {}", srcUrl.toString() + ")", e);
		    callback.onFailure(this, e);
		    return;
		}
		catch(java.net.UnknownHostException e)
		{
		    log.error("Downloading failed: {}", srcUrl.toString() + ")", e);
		    callback.onFailure(this, e);
		    return;
		}
		catch(IOException e)
		{
		    log.trace("Downloading attempt failed: {}", srcUrl.toString(), e);
		}
	    }
	    callback.onFailure(this, new IOException("Reached the limit of attempts"));
	    return;
	}
	catch(Throwable e)
	{
	    log.error("Downloading failed: {}", srcUrl.toString(), e);
	    if (!interrupting)
		callback.onFailure(this, e);
	}
    }

    synchronized public void startAsync()
    {
	if (thread != null)
	    throw new RuntimeException("The task is already running");
	this.thread = new Thread(this);
	thread.start();
    }

    @Override public void run()
    {
	startSync();
    }

    synchronized public void stop()
    {
	if (thread == null)
	    return;
	this.interrupting = true;
	final Call cur = this.currentCall;
	if (cur != null)
	    cur.cancel();
	try {
	    this.thread.join();
	}
	catch(InterruptedException e)
	{
	    Thread.currentThread().interrupt();
	}
	this.thread = null;
    }

    private void attempt() throws IOException
    {
	final long pos;
	final BufferedOutputStream os;
	if (destFile.exists())
	{
	    if (destFile.isDirectory())
		throw new RuntimeException(destFile.getAbsolutePath() + " exists and is a directory");
	    final long size = destFile.length();
	    pos = size > BACKSTEP?size - BACKSTEP:0;
	    truncate(pos);
	    os = new BufferedOutputStream(new FileOutputStream(destFile, true));
	} else
	{
	    pos = 0;
	    os = new BufferedOutputStream(new FileOutputStream(destFile));
	}
	if (interrupting)
	{
	    os.close();
	    return;
	}
	final Request.Builder requestBuilder = new Request.Builder()
	    .url(srcUrl)
	    .header("User-Agent", Connections.DEFAULT_USER_AGENT);
	if (pos > 0)
	    requestBuilder.header("Range", "bytes=" + pos + "-");
	final Request request = requestBuilder.build();
	this.currentCall = httpClient.newCall(request);
	try (final Response response = this.currentCall.execute())
	{
	    if (interrupting)
		return;
	    final int code = response.code();
	    if (pos == 0 && code != 200)
		throw new Connections.InvalidHttpResponseCodeException(code, srcUrl.toString());
	    if (pos > 0 && code != 206)
		throw new Connections.InvalidHttpResponseCodeException(code, srcUrl.toString());
	    final ResponseBody body = response.body();
	    if (body == null)
		throw new IOException("Empty response body for " + srcUrl.toString());
	    final long contentLength = body.contentLength();
	    if (contentLength >= 0)
		callback.setFileSize(this, pos + contentLength);
	    if (interrupting)
		return;
	    final InputStream is = body.byteStream();
	    try {
		final byte[] buf = new byte[512];
		int numRead = 0;
		int totalRead = 0;
		if (interrupting)
		    return;
		while ( (numRead = is.read(buf)) >= 0)
		{
		    if (this.interrupting)
			return;
		    os.write(buf, 0, numRead);
		    totalRead += numRead;
		    callback.onProgress(this, pos + totalRead);
		    if (interrupting)
			return;
		}
		if (interrupting)
		    return;
		os.flush();
	    }
	    finally {
		try {
		    is.close();
		    os.close();
		}
		catch(IOException e) {}
		this.currentCall = null;
	    }
	}
	finally {
	    this.currentCall = null;
	}
    }

    private void truncate(long pos) throws IOException
    {
	final RandomAccessFile file = new RandomAccessFile(destFile, "rws");
	try {
	    file.setLength(pos);
	}
	finally {
	    file.close();
	}
    }
}
