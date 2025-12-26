// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.io.api.yandex.translate;

import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.*;

import org.luwrain.io.api.yandex.translate.model.*;

import static java.util.Objects.*;
import static java.net.URLEncoder.*;
import static java.nio.charset.StandardCharsets.*;

public final class YandexTranslate
{
    static private final String
	URL = "https://translate.api.cloud.yandex.net/translate/v2/translate";

    static private final Gson gson = new Gson();

    private final String apiKey;

    public YandexTranslate(String apiKey)
    {
	this.apiKey = requireNonNull(apiKey, "apiKey can't be null");
    }

    public Response request(Request request) throws IOException
    {
	final URL url = new URL(URL);
	final var b = new StringBuilder();
	gson.toJson(request, b);
	final byte[] postData       = new String(b).getBytes( UTF_8 );
	final int    postDataLength = postData.length;
	final HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	httpCon.setDoOutput(true);
	httpCon.setInstanceFollowRedirects( false );
	httpCon.setRequestMethod("POST");
	httpCon.setRequestProperty("Authorization", "Api-Key " + apiKey);
	httpCon.setRequestProperty("Content-Type", "application/json"); 
	httpCon.setRequestProperty( "charset", "utf-8");
	httpCon.setRequestProperty( "Content-Length", Integer.toString(postData.length));
	httpCon.setUseCaches( false );
	try( DataOutputStream w = new DataOutputStream( httpCon.getOutputStream())) {
	    w.write( postData );
	    w.flush();
	}
	if (httpCon.getResponseCode() != 200)
	    throw new IllegalStateException("HTTP Code is " + httpCon.getResponseCode());
	try (final var r = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), UTF_8))){
	    return gson.fromJson(r, Response.class);
	}
    }
}
