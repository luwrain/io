/*
   Copyright 2012-2022 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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
import java.net.*;

import org.luwrain.core.*;

class TestingTaskCallback implements Task.Callback
{
    long fileSize = 0;
    long fetchedBytes = 0;
    boolean success = false;
    Throwable throwable = null;

    @Override public void setFileSize(Task task, long bytes)
    {
	this.fileSize = bytes;
    }

    @Override public void onProgress(Task task, long bytes)
    {
	this.fetchedBytes = bytes;
    }

    @Override public void onSuccess(Task task)
    {
	this.success = true;
    }

    @Override public void onFailure(Task task, Throwable throwable)
    {
	this.throwable = throwable;
    }
}
