/*
 * Copyright 2024-2025 Michael Pozhidaev <msp@luwrain.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.luwrain.io.api.lsocial;

import java.io.*;
import java.net.*;
import java.util.*;

import com.google.gson.*;

import static java.util.Objects.*;
import static java.net.URLEncoder.*;

public class Query<E, R>
{
    public enum Type {GET, POST};

    static protected final String CHARSET = "UTF-8";
    static protected Gson gson = new Gson();

    private final Type type;
    private final String baseUrl, addr;
    Class<R> responseClass;
    protected Map<String, String> args = new HashMap<>();

    public Query(Type type, String baseUrl, String addr, Class<R> responseClass)
    {
	this.type = requireNonNull(type, "type can't be null");
	this.baseUrl = requireNonNull(baseUrl, "baseUrl can't be null");
	this.addr = requireNonNull(addr, "addr can't be null");
	this.responseClass = requireNonNull(responseClass, "responseClass can't be null");
    }

    public R exec() throws IOException
    {
	switch(type)
	{
	case GET: return execGet();
	case POST: return execPost();
	default:
	    throw new IllegalArgumentException("Unknown query type: " + type.toString());
	}
    }

    protected R execGet() throws IOException
    {
	try (final var r = new BufferedReader(new InputStreamReader(doGet(addr), CHARSET))) {
	    return gson.fromJson(r, responseClass);	    
	}
    }

        protected R execPost() throws IOException
    {
	try (final var r = new BufferedReader(new InputStreamReader(doPost(addr, args), CHARSET))) {
	    return gson.fromJson(r, responseClass);	    
	}
    }

    public E accessToken(String value)
    {
	args.put("atoken", requireNonNull(value, "value can't be null").trim());
	return (E)this;
    }

    protected InputStream doGet(String resource) throws IOException
    {
	return doGet(resource, args, true);
    }

        protected InputStream doGet(String resource, Map<String, String> args) throws IOException
    {
	return doGet(resource, args, true);
    }

    protected InputStream doGet(String resource, Map<String, String> args, boolean useEndpoint) throws IOException
    {
	if (resource == null || resource.isEmpty())
	    throw new IllegalArgumentException("resource may not be empty");
	requireNonNull(args, "args can't be empty");
	final StringBuilder b = new StringBuilder();
	b.append(resource);
	b.append(encodeArgs(args, "?"));
	final URL url;
	if (useEndpoint)
	url = new URL(getBaseUrl(), new String(b)); else
	    	url = new URL(new String(b));
	final HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	httpCon.connect();
	if (httpCon.getResponseCode() == 200)
	    return httpCon.getInputStream();
	throw buildException(url, httpCon);
    }

    protected InputStream doUpload(String resource, File file) throws IOException
    {
	final URL url = new URL(resource);
	String charset = "UTF-8";
	final String boundary = Long.toHexString(System.currentTimeMillis());
	final String CRLF = "\r\n";
	final HttpURLConnection con = (HttpURLConnection)url.openConnection();
	con.setDoOutput(true);
	con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
	try (
	     final OutputStream output = con.getOutputStream();
	     final PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
	     ) {
	    writer.append("--" + boundary).append(CRLF);
	    writer.append("Content-Disposition: form-data; name=\"data\"; filename=\"" + file.getName() + "\"").append(CRLF);
	    writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(CRLF);
	    writer.append("Content-Transfer-Encoding: binary").append(CRLF);
	    writer.append(CRLF).flush();
	    java.nio.file.Files.copy(file.toPath(), output);
	    output.flush();
	    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.
	    writer.append("--" + boundary + "--").append(CRLF).flush();
	}
	if (con.getResponseCode() == 200)
	    return con.getInputStream();
	throw buildException(url, con);
    }

    protected InputStream doPost(String resource, Map<String, String> args) throws IOException
    {
	final URL url = new URL(getBaseUrl(), resource + "?atoken=" + requireNonNull(args.get("atoken"), "No access token in the arguments map"));
	final HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	httpCon.setDoOutput(true);
	httpCon.setInstanceFollowRedirects( false );
	httpCon.setRequestMethod("POST");
	final byte[] postData       = encodeArgs(args, "").getBytes( java.nio.charset.StandardCharsets.UTF_8 );
	final int    postDataLength = postData.length;
	httpCon.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
	httpCon.setRequestProperty( "charset", "utf-8");
	httpCon.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
	httpCon.setUseCaches( false );
	try( DataOutputStream w = new DataOutputStream( httpCon.getOutputStream())) {
	    w.write( postData );
	    w.flush();
	}
	if (httpCon.getResponseCode() == 200)
	    return httpCon.getInputStream();
	throw buildException(url, httpCon);
    }

        public InputStream doPost(String resource, String data) throws IOException
    {
	final URL url = new URL(getBaseUrl(), resource);
	final HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	httpCon.setDoOutput(true);
	httpCon.setInstanceFollowRedirects( false );
	httpCon.setRequestMethod("POST");
	final byte[] postData       = data.getBytes(java.nio.charset.StandardCharsets.UTF_8 );
	final int    postDataLength = postData.length;
	httpCon.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
	httpCon.setRequestProperty( "charset", "utf-8");
	httpCon.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
	httpCon.setUseCaches( false );
	try( DataOutputStream w = new DataOutputStream( httpCon.getOutputStream())) {
	    w.write( postData );
	    w.flush();
	}
	if (httpCon.getResponseCode() == 200)
	    return httpCon.getInputStream();
	throw buildException(url, httpCon);
    }

    void doPut(String resource) throws IOException
    {
	final URL url = new URL(getBaseUrl(), resource);
	final HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	httpCon.setDoOutput(true);
	httpCon.setRequestMethod("PUT");
	final OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
	out.write("FIXME");
	out.close();
	httpCon.getInputStream();
    }

    protected <R> R execGet(String path, Class<R> respClass) throws IOException
    {
	try (final var r = new BufferedReader(new InputStreamReader(doGet(path), CHARSET))) {
	    return gson.fromJson(r, respClass);	    
	}
    }


    String encodeArgs(Map<String, String> args, String prefix)
    {
	if (args.isEmpty())
	    return "";
	try {
	    final var b = new StringBuilder();
	    boolean first = true;
	    for(Map.Entry<String, String> e: args.entrySet())
	    {
		b.append(first?prefix:"&");
		first = false;
		b.append(encode(e.getKey(), CHARSET)).append("=").append(encode(e.getValue(), CHARSET));
	    }
	    return new String(b);
	}
	catch(UnsupportedEncodingException e)
	{
	    throw new RuntimeException(e);
	}
	}

    URL getBaseUrl() throws IOException
    {
	return new URL(baseUrl);
    }

QueryException buildException(URL url, HttpURLConnection con) throws IOException
    {
	final int code = con.getResponseCode();
	if (code != 400 && code != 500)
	    return new QueryException("HTTP code: " + String.valueOf(code));
	try (final var r = new BufferedReader(new InputStreamReader(con.getErrorStream(), CHARSET))) {
	    final var resp = gson.fromJson(r, Response.class);
	    if (resp == null)
			    return new QueryException("HTTP code: " + String.valueOf(code));
	    return new QueryException(code, resp);
	}
	    }
}
