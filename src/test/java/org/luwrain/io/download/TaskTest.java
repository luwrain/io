/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.luwrain.core.*;

public class TaskTest
{
    static private final String url = "http://download.luwrain.org/pdf/presentation-HongKongOSConference-en-2015-06-27.pdf";
        static private final String noSuchFileUrl = "http://download.luwrain.org/pdf/no-such-file.pdf";
            static private final String noSuchHostUrl = "http://no.such.host/presentation.pdf";

    @Disabled @Test public void fetch() throws Exception
    {
	final TestingTaskCallback callback = new TestingTaskCallback();
	final File destFile = File.createTempFile("lwriotest", ".pdf");
	final Task task = new Task(callback, new URL(url), destFile);
	task.startSync();
	assertTrue(callback.success);
	assertTrue(callback.fileSize == 77249);
    }

        @Disabled @Test public void noSuchFile() throws Exception
    {
	final TestingTaskCallback callback = new TestingTaskCallback();
	final File destFile = File.createTempFile("lwriotest", ".pdf");
	final Task task = new Task(callback, new URL(noSuchFileUrl), destFile);
	task.startSync();
    }

            @Disabled @Test public void noSuchHost() throws Exception
    {
	final TestingTaskCallback callback = new TestingTaskCallback();
	final File destFile = File.createTempFile("lwriotest", ".pdf");
	final Task task = new Task(callback, new URL(noSuchHostUrl), destFile);
	task.startSync();
	assertFalse(callback.success);
	assertTrue(callback.throwable != null);
	assertTrue(callback.throwable instanceof java.net.UnknownHostException);
    }
}
