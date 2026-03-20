
package org.luwrain.app.telegram.layouts;

import java.util.*;
import java.io.*;

import org.drinkless.tdlib.TdApi;
import org.drinkless.tdlib.TdApi.Chat;
import org.drinkless.tdlib.TdApi.ChatTypeSupergroup;
import org.drinkless.tdlib.TdApi.Supergroup;
import org.drinkless.tdlib.TdApi.ChatTypeBasicGroup;
import org.drinkless.tdlib.TdApi.BasicGroup;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;
import org.luwrain.app.telegram.*;

public final class ChatPropertiesLayout extends LayoutBase
{
    private final App app;
    private final SimpleArea propsArea;

    public ChatPropertiesLayout(App app, Chat chat, Runnable closing)
    {
	//FIXME:super
	this.app = app;
	this.propsArea = new SimpleArea(new DefaultControlContext(app.getLuwrain()), app.getStrings().chatPropsAreaName()) {
		@Override public boolean onInputEvent(InputEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (app.onInputEvent(this, event, closing))
			return true;
		    return super.onInputEvent(event);
		}
		@Override public boolean onSystemEvent(SystemEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (app.onSystemEvent(this, event))
			return true;
		    			return super.onSystemEvent(event);
		}
		@Override public boolean onAreaQuery(AreaQuery query)
		{
		    NullCheck.notNull(query, "query");
		    if (app.onAreaQuery(this, query))
			return true;
		    return super.onAreaQuery(query);
		}
	    };
	fill(chat);
    }

    private void fill(Chat chat)
    {
	if (chat.type != null && chat.type instanceof ChatTypeSupergroup)
	{
	    final ChatTypeSupergroup s = (ChatTypeSupergroup)chat.type;
	    app.getOperations().getSupergroup(s.supergroupId, (supergroup)->{
		    propsArea.update((lines)->{
		    fillSupergroup(chat, supergroup);

		    lines.add("");
			});
		});
	    return;
	}

		if (chat.type != null && chat.type instanceof ChatTypeBasicGroup)
	{
	    final ChatTypeBasicGroup b = (ChatTypeBasicGroup)chat.type;
	    app.getOperations().getBasicGroup(b.basicGroupId, (basicGroup)->{
		    propsArea.update((lines)->{
		    fillBasicGroup(chat, basicGroup);
		    lines.add("");
			});
		});
	    return;
	}

		propsArea.update((lines)->{
	fillBasic(chat);
	lines.add("");
		    });
    }

    private void fillBasic(Chat chat)
    {
	propsArea.add("Тип: " + chat.type.getClass().getName());
	//	propsArea.add(chat.chatList.getClass().getName());
	propsArea.add("Имя: " + chat.title);
	propsArea.add("canBeDeletedOnlyForSelf: " + chat.canBeDeletedOnlyForSelf);
	propsArea.add("canBeDeletedForAllUsers: " + chat.canBeDeletedForAllUsers);
	propsArea.add("unreadCount: " + chat.unreadCount);
    }

    private void fillSupergroup(Chat chat, Supergroup supergroup)
    {
	//	propsArea.add("Имя: " + supergroup.username);
	propsArea.add("Участников: " + supergroup.memberCount);
	propsArea.add("Канал: " + supergroup.isChannel);
    }

        private void fillBasicGroup(Chat chat, BasicGroup basicGroup)
    {
	propsArea.add("Name: " + chat.title);
	propsArea.add("Member count: " + basicGroup.memberCount);
    }

    AreaLayout getLayout()
    {
	return new AreaLayout(propsArea);
    }
}
