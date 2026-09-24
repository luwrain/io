// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.url;

import java.net.*;
import java.util.*;
import java.io.*;

import okhttp3.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;
import org.luwrain.popups.*;

import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.util.TextUtils.*;

final class Conv
{
    private final App app;
    private final Luwrain luwrain;
    private final Strings strings;
    private final OkHttpClient httpClient;

    Conv(App app)
    {
	this.app = app;
	this.luwrain = app.getLuwrain();
	this.strings = app.getStrings();
	this.httpClient = new OkHttpClient.Builder()
	    .followRedirects(true)
	    .followSslRedirects(true)
	    .build();
    }

    boolean onClipboardPaste()
    {
	final Object[] objs = luwrain.getClipboard().get();
	for(Object obj: objs)
	{
	    final URL url;
	    if (obj instanceof URL)
	    {
		url = (URL)obj;
	    } else
	    {
		try {
		    url = new URL(obj.toString());
		}
		catch(MalformedURLException e)
		{
		    luwrain.message(strings.unableToMakeUrl(obj.toString()), Luwrain.MessageType.ERROR);
		    continue;
		}
	    }
	    fetchUrl(url);
	}
	return true;
    }

    void fetchUrl(URL url)
    {
	NullCheck.notNull(url, "url");
	final String urlStr = url.toString();
	luwrain.message(strings.fetching(), Luwrain.MessageType.ANNOUNCEMENT);
	final Request request = new Request.Builder()
	    .url(urlStr)
	    .build();
	httpClient.newCall(request).enqueue(new Callback()
	{
	    @Override public void onFailure(Call call, IOException e)
	    {
		luwrain.runUiSafely(()->{
			app.text = splitLines(strings.fetchError(urlStr, e.getMessage() != null?e.getMessage():""));
			luwrain.onAreaNewContent(app.getAreaLayout().getAreas()[0]);
			luwrain.message(strings.fetchError(urlStr, e.getMessage() != null?e.getMessage():""), Luwrain.MessageType.ERROR);
		    });
	    }
	    @Override public void onResponse(Call call, Response response) throws IOException
	    {
		final String body;
		try {
		    body = response.body() != null?response.body().string():"";
		}
		catch(IOException e)
		{
		    luwrain.runUiSafely(()->{
			    app.text = splitLines(strings.fetchError(urlStr, e.getMessage() != null?e.getMessage():""));
			    luwrain.onAreaNewContent(app.getAreaLayout().getAreas()[0]);
			    luwrain.message(strings.fetchError(urlStr, e.getMessage() != null?e.getMessage():""), Luwrain.MessageType.ERROR);
			});
		    return;
		}
		luwrain.runUiSafely(()->{
			app.text = splitLines(body);
			luwrain.onAreaNewContent(app.getAreaLayout().getAreas()[0]);
			luwrain.message(strings.fetched(urlStr), Luwrain.MessageType.ANNOUNCEMENT);
		    });
	    }
	});
    }
}
