
package org.luwrain.io.download;

import java.io.*;
import java.util.*;
import java.net.*;

import org.luwrain.core.*;
import org.luwrain.util.*;

public final class Task
{
    public enum Status {
	INITIAL,
	FETCHING,
	OK,
    };

    public interface Info
    {
	Status getStatus(Task task);
	void setStatus(Task task, Status status);
	long getFileSize(Task task);
	void setFileSize(Task task, long size);
	void onProgress(Task task, int percent);
	    }

    private final Info info;
    private final URL srcUrl;
    private File destFile;

    public Task(Info info, URL srcUrl, File destFile)
    {
	NullCheck.notNull(info, "info");
	NullCheck.notNull(srcUrl, "srcUrl");
	NullCheck.notNull(destFile, "destFile");
	this.info = info;
	this.srcUrl = srcUrl;
	this.destFile = destFile;
    }

    public void start()
    {
	final Status status = info.getStatus(this);
	switch(status)
	{
	case INITIAL:
	    launchInitial();
	    break;
	case FETCHING:
	    launchContinuing();
	    break;
	case OK:
	default:
	    return;
	    	}
    }

    private void launchInitial()
    {
	try {
	final URLConnection con = Connections.connect(srcUrl, 0);
	}
	catch(IOException e)
	{
	    
	}
    }

    private void launchContinuing()
    {
    }

    public void stop()
    {
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
