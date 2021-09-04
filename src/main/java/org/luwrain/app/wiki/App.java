
package org.luwrain.app.wiki;

import java.net.*;
import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.queries.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;

public final class App extends AppBase<Strings> implements MonoApp
{
    private final String arg;
    private MainLayout mainLayout = null;

    public App()
    {
	this(null);
    }

    public App(String arg)
    {
	super(Strings.NAME, Strings.class);
	this.arg = arg;
    }

    @Override protected AreaLayout onAppInit()
    {
	this.mainLayout = new MainLayout(this);
	return this.mainLayout.getAreaLayout();
    }

    /*
        private Page[] query(String query)
    {
	NullCheck.notEmpty(query, "query");
	final AtomicReference res = new AtomicReference();
	luwrain.runHooks("luwrain.wiki.search", (hook)->{
		try {
		    final Object obj = hook.run(new Object[]{query});
		    if (obj == null)
			return Luwrain.HookResult.CONTINUE;
		    res.set(obj);
		    return Luwrain.HookResult.BREAK;
		}
		catch(RuntimeException e)
		{
		    res.set(e);
		    return Luwrain.HookResult.BREAK;
		}
	    });
	if (res.get() == null)
	    return new Page[0];
	if (res.get() instanceof RuntimeException)
	    throw (RuntimeException)res.get();
	final List objs = ScriptUtils.getArray(res.get());
	if (objs == null)
	    return new Page[0];
	final List<Page> pages = new LinkedList();
	for(Object o: objs)
	{
	    final Object langObj = ScriptUtils.getMember(o, "lang");
	    final Object titleObj = ScriptUtils.getMember(o, "title");
	    final Object commentObj = ScriptUtils.getMember(o, "comment");
	    final String lang = ScriptUtils.getStringValue(langObj);
	    final String title = ScriptUtils.getStringValue(titleObj);
	    final String comment = ScriptUtils.getStringValue(commentObj);
	    if (lang == null || title == null || comment == null)
		continue;
	    pages.add(new Page(lang, title, comment));
	}
	return pages.toArray(new Page[pages.size()]);
    }
    */



        @Override public MonoApp.Result onMonoAppSecondInstance(Application app)
    {
	NullCheck.notNull(app, "app");
		return MonoApp.Result.BRING_FOREGROUND;
    }
}
