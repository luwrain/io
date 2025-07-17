
/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.io.api.yandex_gpt;

import java.io.*;
import java.net.*;
import java.util.*;

import com.google.gson.*;

import static java.util.Objects.*;
import static java.net.URLEncoder.*;

public final class YandexGpt
{
    static private final String
	URL_ASYNC = "https://llm.api.cloud.yandex.net/foundationModels/v1/completionAsync",
	URL_SYNC = "https://llm.api.cloud.yandex.net/foundationModels/v1/completion";

    static private final Gson gson = new Gson();

    private final String apiKey;
    private final Prompt prompt;

    public YandexGpt(String folderId, String apiKey, CompletionOptions options, List<Message> messages)
    {
	this.apiKey = requireNonNull(apiKey, "apiKey can't be null");
	this.prompt = new Prompt();
	prompt.setCompletionOptions(requireNonNull(options, "options can't be null"));
	prompt.setMessages(requireNonNull(messages, "messages can't be null"));
	prompt.setModelUri("gpt://" + requireNonNull(folderId, "folderId can't be null") + "/yandexgpt");
    }

    public Response doSync() throws IOException
    {
	final URL url = new URL(URL_SYNC);
	final var b = new StringBuilder();
	gson.toJson(prompt, b);
	final byte[] postData       = new String(b).getBytes( java.nio.charset.StandardCharsets.UTF_8 );
	final int    postDataLength = postData.length;
	final HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	httpCon.setDoOutput(true);
	httpCon.setInstanceFollowRedirects( false );
	httpCon.setRequestMethod("POST");
	httpCon.setRequestProperty("Authorization", "Api-Key " + apiKey);
	httpCon.setRequestProperty("Content-Type", "application/json"); 
	httpCon.setRequestProperty( "charset", "utf-8");
	httpCon.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
	httpCon.setUseCaches( false );
	try( DataOutputStream w = new DataOutputStream( httpCon.getOutputStream())) {
	    w.write( postData );
	    w.flush();
	}
	if (httpCon.getResponseCode() != 200)
	    throw new IllegalStateException("HTTP Code is " + httpCon.getResponseCode());
	try (final var r = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "UTF-8"))){
	    return gson.fromJson(r, Response.class);
	}
    }
}
