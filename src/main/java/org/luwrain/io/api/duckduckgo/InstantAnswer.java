
package org.luwrain.io.api.duckduckgo;

import java.io.*;
import java.util.*;
import java.net.*;
import org.json.*;

import org.luwrain.core.*;
import org.luwrain.util.*;

//https://api.duckduckgo.com/?q=лондон&format=xml&kl=ru-ru&skip_disambig
//skip_disambig


public final class InstantAnswer
{
    static private final String ENCODING = "UTF-8";

    public enum Flags {}

    public Answer getAnswer(String query, Properties props, Set<Flags> flags) throws IOException
    {
	NullCheck.notEmpty(query, "query");
	NullCheck.notNull(props, "props");
	NullCheck.notNull(flags, "flags");
	final StringBuilder b = new StringBuilder();
	b.append("https://api.duckduckgo.com/?q=");
	b.append(URLEncoder.encode(query, ENCODING));
	b.append("&format=json");
	if (props.getProperty("kl") != null && !props.getProperty("kl").isEmpty())
	    b.append("&kl=" + props.getProperty("kl"));

	final URL url = new URL(new String(b));
	final URLConnection con = Connections.connect(url, 0);
	final InputStream is = con.getInputStream();
			final JSONTokener t = new JSONTokener(is);
		final JSONObject obj = new JSONObject(t);
		final String type = obj.getString("Type");
		return null;
    }

    static public final class Answer
    {
    }
}
