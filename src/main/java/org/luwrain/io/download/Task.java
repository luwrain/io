
package org.luwrain.io.download;

import java.io.*;
import java.util.*;
import java.net.*;

import org.luwrain.core.*;
import org.luwrain.util.*;

public final class Task
{
    static private final String LOG_COMPONENT = "download";
    static private final int MAX_ATTEMPT_COUNT = 32;
    static private final long BACKSTEP = 2048;

    public interface Callback
    {
	void setFileSize(Task task, long size);
	void onProgress(Task task, long bytesFetched);
	void onSuccess(Task task);
	void onFailure(Task task, Throwable throwable);
	    }

    private final Callback callback;
    private final URL srcUrl;
    private File destFile;

    public Task(Callback callback, URL srcUrl, File destFile)
    {
	NullCheck.notNull(callback, "callback");
	NullCheck.notNull(srcUrl, "srcUrl");
	NullCheck.notNull(destFile, "destFile");
	this.callback = callback;
	this.srcUrl = srcUrl;
	this.destFile = destFile;
    }

    public void startSync()
    {
	try {
	    for(int i = 0;i < MAX_ATTEMPT_COUNT;++i)
	{
	    try {
		attempt();
		callback.onSuccess(this);
		return;
	    }
	    	    catch(org.luwrain.util.Connections.InvalidHttpResponseCodeException e)
	    {
	    		Log.error(LOG_COMPONENT, "downloading failed:" + e.getClass().getName() + ":" + e.getMessage() + " (" + srcUrl.toString() + ")");
			callback.onFailure(this, e);
			return;
	}
	    catch(java.net.UnknownHostException e)
	    {
	    		Log.error(LOG_COMPONENT, "downloading failed:" + e.getClass().getName() + ":" + e.getMessage() + " (" + srcUrl.toString() + ")");
			callback.onFailure(this, e);
			return;
	}
	    catch(IOException e)
	    {
		Log.debug(LOG_COMPONENT, "downloading attempt failed:" + e.getClass().getName() + ":" + e.getMessage() + " (" + srcUrl.toString() + ")");
	    }
	}
	    callback.onFailure(this, new IOException("Reached the limit of attempts"));
	    return;
	}
	catch(Throwable e)
	{
	    		Log.error(LOG_COMPONENT, "downloading failed:" + e.getClass().getName() + ":" + e.getMessage() + " (" + srcUrl.toString() + ")");
			callback.onFailure(this, e);
	}
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
	Log.debug(LOG_COMPONENT, "new downloading attempt from position " + pos + " (" + srcUrl.toString() + ")");
	final URLConnection con = Connections.connect(srcUrl, pos);
	final long len = con.getContentLength();
	if (len >= 0)
	    callback.setFileSize(this, len);
	final BufferedInputStream is = new BufferedInputStream(con.getInputStream());
	try {
	    final byte[] buf = new byte[512];
	    int numRead = 0;
	    while ( (numRead = is.read(buf)) >= 0)
	    {
		os.write(buf, 0, numRead);
	    }
	    os.flush();
	}
	finally {
	    is.close();
	    os.close();
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
