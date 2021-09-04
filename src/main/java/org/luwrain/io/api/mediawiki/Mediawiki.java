
package org.luwrain.io.api.mediawiki;

import java.io.*;
import java.net.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import org.luwrain.core.*;

public final class Mediawiki
{
    private final String baseUrl;

    public Mediawiki(String baseUrl)
    {
	NullCheck.notEmpty(baseUrl, "baseUrl");
	this.baseUrl = baseUrl;
    }

    public void query(String q) throws IOException
    {
	final String url;
	try {
url = baseUrl + "/api.php?action=query&list=search&srsearch=" + URLEncoder.encode(q, "UTF-8") + "&format=xml";
	}
	catch(UnsupportedEncodingException e)
	{
	    throw new RuntimeException(e);
	}
final Connection con = Jsoup.connect(url);
final Document doc = con.get();
final Elements pages = doc.getElementsByTag("p");
for(Element page: pages)
	{
	    final String title = page.attr("title");
	    String comment = page.attr("snippet");
	    if (title == null || title.isEmpty())
		continue;
	    if (comment == null)
		comment = "";
	    comment = stripTags(comment);
	    //	    res.push({title: title, lang: lang, comment: comment});
	}
    }

    static private String stripTags(String s)
    {
	    return s.replaceAll("</span>", "").replaceAll("<span class=.searchmatch.>", "");
    }
    
    }
