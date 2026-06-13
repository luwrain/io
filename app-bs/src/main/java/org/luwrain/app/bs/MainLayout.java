// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.bs;

import java.util.*;
import java.io.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.controls.list.*;
import org.luwrain.app.bs.model.*;

import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;

public class MainLayout extends LayoutBase implements ListArea.ClickHandler<Record>
{
    static private final Logger log = LogManager.getLogger();

    public final List<Record> records = new ArrayList<>();
    public final ListArea<Record> recordsArea;
    public final EditArea quickPostArea;
    final Actions recordsActions;
    final Actions quickPostActions;
    final App app;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();

	recordsArea = new ListArea<Object>(listParams(p -> {
		    p.name = s.mainAreaName();
		    p.model = new ListModel<Object>(records);
		    p.appearance = new RecordListAppearance(getControlContext());
		    p.clickHandler = this;
		}));

	setPropertiesHandler(recordsArea, a -> {
		final var sel = recordsArea.selected();
		if (sel == null)
		    return null;
		if (sel instanceof org.luwrain.app.bs.model.Record r && r.getAuthorDid() != null)
		    return new UserLayout(app, r.getAuthorDid(), r.getAuthorHandle(), getReturnAction());
		return null;
	    });

	recordsActions = actions(
				 action("create", s.post(), new InputEvent(Special.INSERT),
					() -> { /*setActiveArea(quickPostArea);*/ return true; }),
				 action("refresh", s.refresh(), new InputEvent(Special.F5),
					this::onRefresh),
				 action("followings", s.followingsAreaName(), new InputEvent(Special.F6),
					() -> { app.setAreaLayout(app.getFollowingsLayout());
					    return true; })
				 );

	quickPostArea = new EditArea(editParams(p -> {
		    p.name = s.quickPostAreaName();
		    p.appearance = new DefaultEditAreaAppearance(p.context){
			    @Override public void announceLine(int index, String line)
			    {
				if (line.trim().isEmpty())
				{
				    app.setEventResponse(hint(Hint.EMPTY_LINE));
				    return;
				}
				app.setEventResponse(text(line));
			    }
			};
		})){
		@Override public boolean onSystemEvent(org.luwrain.core.events.SystemEvent event)
		{
		    if (event.getType() != org.luwrain.core.events.SystemEvent.Type.REGULAR)
			return super.onSystemEvent(event);
		    switch(event.getCode())
		    {
		    case SAVE:
			return onQuickPost();
		    default:
			return super.onSystemEvent(event);
		    }
		}
	    };

	quickPostActions = actions(
				   action("post", s.post(), new InputEvent(Special.INSERT),
					  this::onQuickPost)
				   );

	setAreaLayout(AreaLayout.TOP_BOTTOM, quickPostArea, quickPostActions, recordsArea, recordsActions);
    }

    boolean onQuickPost()
    {
	final var text = quickPostArea.getTextAsList().stream().collect(joining("\n"));
	if (text.trim().isEmpty())
	    return false;
	if (!app.isReady())
	{
	    app.message(app.getStrings().notConfigured(), Luwrain.MessageType.ERROR);
	    return false;
	}
	final var taskId = app.newTaskId();
	return app.runTask(taskId, () -> {
		// FIXME: call BlueSky API createRecord
		app.finishedTask(taskId, () -> {
			//			quickPostArea.setText("");
			app.message(app.getStrings().recordPosted(), Luwrain.MessageType.OK);
			updateRecords();
		    });
	    });
    }

    boolean onRefresh()
    {
	updateRecords();
	return true;
    }

    @Override public boolean onListClick(ListArea<Object> area, int index, Object obj)
    {
	if (obj instanceof org.luwrain.app.bs.model.Record record)
	    return onRecordClick(record);
	return false;
    }

    boolean onRecordClick(org.luwrain.app.bs.model.Record record)
    {
	if (record.getAuthorDid() != null)
	{
	    final var userLayout = new UserLayout(app, record.getAuthorDid(), record.getAuthorHandle(), getReturnAction());
	    app.setAreaLayout(userLayout);
	    return true;
	}
	return false;
    }

    void updateRecords()
    {
	if (!app.isReady())
	    return;
	final var taskId = app.newTaskId();
	app.runTask(taskId, () -> {
		final var res = fetchRecords();
		app.finishedTask(taskId, () -> {
			records.clear();
			records.addAll(res);
			recordsArea.refresh();
		    });
	    });
    }

    List<org.luwrain.app.bs.model.Record> fetchRecords()
    {
	// FIXME: call BlueSky API getTimeline
	return List.of();
    }

    final class RecordListAppearance extends DoubleLevelAppearance<Object>
    {
	RecordListAppearance(ControlContext context) { super(context); }

	@Override public boolean isSectionItem(Object obj)
	{
	    return false;
	}

	@Override public void announceNonSection(Object item)
	{
	    if (item instanceof org.luwrain.app.bs.model.Record r)
	    {
		final var author = requireNonNullElse(r.getAuthorDisplayName(),
						      requireNonNullElse(r.getAuthorHandle(), ""));
		final var text = requireNonNullElse(r.getText(), "");
		app.setEventResponse(listItem(author + " " + text));
	    }
	    else
		app.setEventResponse(listItem(item.toString()));
	}

	@Override public String getNonSectionScreenAppearance(Object item)
	{
	    if (item instanceof org.luwrain.app.bs.model.Record r)
	    {
		final var author = requireNonNullElse(r.getAuthorDisplayName(),
						      requireNonNullElse(r.getAuthorHandle(), ""));
		final var text = requireNonNullElse(r.getText(), "");
		return author + ": " + text;
	    }
	    return item.toString();
	}
    }
}
