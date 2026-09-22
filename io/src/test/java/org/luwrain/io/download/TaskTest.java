// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.download;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

import okhttp3.mockwebserver.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.*;

import org.luwrain.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TaskTest
{
    private MockWebServer server;

    @BeforeEach public void startServer() throws IOException
    {
	server = new MockWebServer();
	server.start();
    }

    @AfterEach public void stopServer() throws IOException
    {
	server.shutdown();
    }

    @Test public void downloadsFile(@TempDir Path tmpDir) throws Exception
    {
	final Task.Callback callback = mock(Task.Callback.class);
	server.enqueue(new MockResponse()
		       .setResponseCode(200)
		       .setHeader("Content-Length", "11")
		       .setBody("hello world"));
	final File destFile = tmpDir.resolve("download.bin").toFile();
	final Task task = new Task(callback, server.url("/file").url(), destFile);
	task.startSync();
	assertEquals("hello world", Files.readString(destFile.toPath(), StandardCharsets.ISO_8859_1));
	verify(callback).setFileSize(task, 11L);
	verify(callback, atLeastOnce()).onProgress(eq(task), anyLong());
	verify(callback).onSuccess(task);
	verify(callback, never()).onFailure(eq(task), any());
    }

    @Test public void doesNotReportSizeWhenContentLengthIsUnknown(@TempDir Path tmpDir) throws Exception
    {
	final Task.Callback callback = mock(Task.Callback.class);
	server.enqueue(new MockResponse()
		       .setResponseCode(200)
		       .setChunkedBody("abc", 2));
	final File destFile = tmpDir.resolve("download.bin").toFile();
	final Task task = new Task(callback, server.url("/file").url(), destFile);
	task.startSync();
	assertEquals("abc", Files.readString(destFile.toPath(), StandardCharsets.ISO_8859_1));
	verify(callback, never()).setFileSize(any(), anyLong());
	verify(callback).onSuccess(task);
	verify(callback, never()).onFailure(eq(task), any());
    }

    @Test public void resumesExistingFileWithRangeHeader(@TempDir Path tmpDir) throws Exception
    {
	final Task.Callback callback = mock(Task.Callback.class);
	final File destFile = tmpDir.resolve("download.bin").toFile();
	final byte[] prefix = new byte[3000];
	Arrays.fill(prefix, (byte)'a');
	Files.write(destFile.toPath(), prefix);
	server.enqueue(new MockResponse()
		       .setResponseCode(206)
		       .setHeader("Content-Length", "3")
		       .setBody("XYZ"));
	final Task task = new Task(callback, server.url("/file").url(), destFile);
	task.startSync();
	final RecordedRequest request = server.takeRequest();
	assertEquals("bytes=952-", request.getHeader("Range"));
	final byte[] result = Files.readAllBytes(destFile.toPath());
	assertEquals(955, result.length);
	assertEquals((byte)'X', result[952]);
	assertEquals((byte)'Y', result[953]);
	assertEquals((byte)'Z', result[954]);
	verify(callback).setFileSize(task, 955L);
	verify(callback, atLeastOnce()).onProgress(eq(task), anyLong());
	verify(callback).onSuccess(task);
	verify(callback, never()).onFailure(eq(task), any());
    }

    @Test public void reportsInvalidResponseCodeFailure(@TempDir Path tmpDir) throws Exception
    {
	final Task.Callback callback = mock(Task.Callback.class);
	server.enqueue(new MockResponse()
		       .setResponseCode(404)
		       .setBody("not found"));
	final File destFile = tmpDir.resolve("download.bin").toFile();
	final Task task = new Task(callback, server.url("/file").url(), destFile);
	task.startSync();
	verify(callback, never()).onSuccess(any());
	final ArgumentCaptor<Throwable> captor = ArgumentCaptor.forClass(Throwable.class);
	verify(callback).onFailure(eq(task), captor.capture());
	assertInstanceOf(Connections.InvalidHttpResponseCodeException.class, captor.getValue());
    }

    @Test public void retriesTransientIOException(@TempDir Path tmpDir) throws Exception
    {
	final Task.Callback callback = mock(Task.Callback.class);
	server.enqueue(new MockResponse()
		       .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START));
	server.enqueue(new MockResponse()
		       .setResponseCode(200)
		       .setHeader("Content-Length", "2")
		       .setBody("ok"));
	final File destFile = tmpDir.resolve("download.bin").toFile();
	final Task task = new Task(callback, server.url("/file").url(), destFile);
	task.startSync();
	assertEquals("ok", Files.readString(destFile.toPath(), StandardCharsets.ISO_8859_1));
	verify(callback).onSuccess(task);
	verify(callback, never()).onFailure(eq(task), any());
    }
}