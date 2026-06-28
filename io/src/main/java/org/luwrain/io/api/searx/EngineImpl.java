
package org.luwrain.io.api.searx;

import java.io.*;
import com.google.auto.service.*;

import org.luwrain.core.*;
import org.luwrain.io.websearch.*;
import org.luwrain.settings.searx.Config;
import static java.util.Objects.*;

@AutoService(Engine.class)
public final class EngineImpl implements org.luwrain.io.websearch.Engine
{
    @Override public Response search(Luwrain luwrain, Query query) throws IOException
    {
	final var s = new Searx(getUrl(luwrain));
	final var resp = s.request(query.getText());
	final var res = new Response();
	res.setQuery(query);
	res.setEntries(resp.results.stream()
	.map(r -> {
		final var e = new Entry();
		e.setTitle(r.title);
		e.setClickUrl(r.url);
		e.setDisplayUrl(r.url);
		e.setSnippet(r.content);
		return e;
	    }).toList());
	return res;
    }

    String getUrl(Luwrain luwrain)
    {
	final var conf = requireNonNullElse(luwrain.loadConf(Config.class), new Config());
	final var url = requireNonNullElse(conf.getUrl(), "");
	return !url.isEmpty() ? Config.DEFAULT_URL : url;
    }
}
