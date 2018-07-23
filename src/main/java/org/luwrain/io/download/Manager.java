
package org.luwrain.io.download;

import java.io.*;
import java.util.*;
import java.net.*;

import org.luwrain.core.*;
import org.luwrain.util.*;

public final class Manager implements Task.Callback
{

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
    


    


    static private final class Entry
    {
	final Task task = null;
	final Settings.Entry sett = null;
    }
}
