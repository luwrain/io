
package org.luwrain.app.telegram;

import java.util.*;
import java.io.*;

import org.drinkless.tdlib.TdApi;
import org.drinkless.tdlib.TdApi.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;
import static org.luwrain.util.TextUtils.*;
import org.luwrain.util.*;

final class MessageContentLayout extends LayoutBase
{
    private final App app;
    private final SimpleArea textArea;
    private final Message message;

    MessageContentLayout(App app, Message message, ActionHandler closing)
    {
	super(app);
	try {
	this.app = app;
		this.message = message;
		final List<String> lines = new ArrayList<>();
		if (message.content != null && message.content instanceof MessageText)
		{
		    final MessageText text = (MessageText)message.content;
		    lines.addAll(new LineIterator(text.text.text).toList());
		}
		this.textArea = new SimpleArea(getControlContext(), "FIXME", lines.toArray(new String[lines.size()]));
			setCloseHandler(closing);
	setAreaLayout(textArea, null);
	}
	catch(IOException ex)
	{
	    throw new RuntimeException(ex);
	}
    }
}
