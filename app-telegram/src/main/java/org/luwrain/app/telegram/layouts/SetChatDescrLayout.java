
package org.luwrain.app.telegram.layouts;

import java.util.*;

import org.drinkless.tdlib.*;
import org.drinkless.tdlib.TdApi.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.app.base.*;
import org.luwrain.util.*;
import org.luwrain.app.telegram.*;

import static org.luwrain.util.TextUtils.*;

public final class SetChatDescrLayout extends LayoutBase
{
    final App app;
    final EditArea editArea;
    final Chat chat;

    public SetChatDescrLayout(App app, Chat chat, String initialValue, ActionHandler closing)
    {
	super(app);
	this.app = app;
	this.chat = chat;
	this.editArea = new EditArea(editParams((params)->{
		    params.name = app.getStrings().setChatDescrAreaName();
		    params.content = new MutableMarkedLinesImpl(LineIterator.toArray(initialValue));
		}));
	setCloseHandler(closing);
	setOkHandler(()->{ return onOk(closing); });
	setAreaLayout(editArea, null);
    }

    private boolean onOk(ActionHandler closing)
    {
	app.getOperations().callFunc(new SetChatDescription(chat.id, editArea.getText("\n")), Ok.CONSTRUCTOR, (res)->{});
	return closing.onAction();
    }
}
