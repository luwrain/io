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
import org.luwrain.controls.list.*;
import org.luwrain.app.bs.model.*;

import static java.util.Objects.*;
import static org.luwrain.core.DefaultEventResponse.*;
import static org.luwrain.core.events.InputEvent.*;

public class UserLayout extends LayoutBase implements ListArea.ClickHandler<Object>
{
    static private final Logger log = LogManager.getLogger();

    final App app;
    final String userDid;
    final String userHandle;

    public final List<Object> records = new ArrayList<>();
    public final List<Following> followings = new ArrayList<>();
    public final List<Following> followers = new ArrayList<>();

    public final ListArea<Object> recordsArea;
    public final ListArea<Following> followingsArea;
    public final ListArea<Following> followersArea;

    final Actions recordsActions;
    final Actions followingsActions;
    final Actions followersActions;

    UserLayout(App app, String userDid, String userHandle, ActionHandler close)
    {
	super(app);
	this.app = app;
	this.userDid = userDid;
	this.userHandle = userHandle;
	final var s = app.getStrings();

	recordsArea = new ListArea<Object>(listParams(p -> {
		    p.name = s.userRecordsAreaName();
		    p.model = new ListModel<Object>(records);
		    p.appearance = new RecordAppearance(getControlContext());
		    p.clickHandler = this;
		}));

	recordsActions = actions(
				action("refresh", s.refresh(), new InputEvent(Special.F5),
				       this::onRefreshRecords)
				);

	followingsArea = new ListArea<Following>(listParams(p -> {
		    p.name = s.userFollowingsAreaName();
		    p.model = new ListModel<Following>(followings);
		    p.appearance = new UserFollowingsAppearance(getControlContext());
		    p.clickHandler = (area, index, f) -> {
			final var ul = new UserLayout(app, f.getDid(), f.getHandle(), getReturnAction());
			app.setAreaLayout(ul.getAreaLayout());
			return true;
		    };
		}));

	followingsActions = actions(
				   action("open", s.open(), new InputEvent(Special.INSERT),
					  () -> {
					      final var sel = followingsArea.selected();
					      if (sel != null)
					      {
						  final var ul = new UserLayout(app, sel.getDid(), sel.getHandle(), getReturnAction());
						  app.setAreaLayout(ul.getAreaLayout());
					      }
					      return true;
					  })
				   );

	followersArea = new ListArea<Following>(listParams(p -> {
		    p.name = s.userFollowersAreaName();
		    p.model = new ListModel<Following>(followers);
		    p.appearance = new UserFollowersAppearance(getControlContext());
		    p.clickHandler = (area, index, f) -> {
			final var ul = new UserLayout(app, f.getDid(), f.getHandle(), getReturnAction());
			app.setAreaLayout(ul.getAreaLayout());
			return true;
		    };
		}));

	followersActions = actions(
				  action("open", s.open(), new InputEvent(Special.INSERT),
					 () -> {
					     final var sel = followersArea.selected();
					     if (sel != null)
					     {
						 final var ul = new UserLayout(app, sel.getDid(), sel.getHandle(), getReturnAction());
						 app.setAreaLayout(ul.getAreaLayout());
					     }
					     return true;
					 })
				  );

	setAreaLayout(AreaLayout.TOP_BOTTOM_LEFT_RIGHT,
		      recordsArea, recordsActions,
		      followingsArea, followingsActions,
		      followersArea, followersActions);

	setCloseHandler(close);

	updateUserData();
    }

    boolean onRefreshRecords()
    {
	updateUserData();
	return true;
    }

    @Override public boolean onListClick(ListArea<Object> area, int index, Object obj)
    {
	if (obj instanceof Record r && r.getAuthorDid() != null)
	{
	    final var ul = new UserLayout(app, r.getAuthorDid(), r.getAuthorHandle(), getReturnAction());
	    app.setAreaLayout(ul.getAreaLayout());
	    return true;
	}
	return false;
    }

    void updateUserData()
    {
	if (!app.isReady())
	    return;
	final var taskId = app.newTaskId();
	app.runTask(taskId, () -> {
		final var recs = fetchUserRecords();
		final var fings = fetchUserFollowings();
		final var fers = fetchUserFollowers();
		app.finishedTask(taskId, () -> {
			records.clear();
			records.addAll(recs);
			recordsArea.refresh();
			followings.clear();
			followings.addAll(fings);
			followingsArea.refresh();
			followers.clear();
			followers.addAll(fers);
			followersArea.refresh();
		    });
	    });
    }

    List<Record> fetchUserRecords()
    {
	// FIXME: call BlueSky API getAuthorFeed
	return List.of();
    }

    List<Following> fetchUserFollowings()
    {
	// FIXME: call BlueSky API getFollows
	return List.of();
    }

    List<Following> fetchUserFollowers()
    {
	// FIXME: call BlueSky API getFollowers
	return List.of();
    }

    final class RecordAppearance extends DoubleLevelAppearance<Object>
    {
	RecordAppearance(ControlContext context) { super(context); }

	@Override public boolean isSectionItem(Object obj) { return false; }

	@Override public void announceNonSection(Object item)
	{
	    if (item instanceof Record r)
	    {
		final var text = requireNonNullElse(r.getText(), "");
		app.setEventResponse(listItem(text));
	    }
	    else
		app.setEventResponse(listItem(item.toString()));
	}

	@Override public String getNonSectionScreenAppearance(Object item)
	{
	    if (item instanceof Record r)
		return requireNonNullElse(r.getText(), "");
	    return item.toString();
	}
    }

    final class UserFollowingsAppearance extends DoubleLevelAppearance<Following>
    {
	UserFollowingsAppearance(ControlContext context) { super(context); }

	@Override public boolean isSectionItem(Following item) { return false; }

	@Override public void announceNonSection(Following item)
	{
	    final var name = requireNonNullElse(item.getDisplayName(),
						requireNonNullElse(item.getHandle(), ""));
	    app.setEventResponse(listItem(name));
	}

	@Override public String getNonSectionScreenAppearance(Following item)
	{
	    return requireNonNullElse(item.getDisplayName(),
				      requireNonNullElse(item.getHandle(), ""));
	}
    }

    final class UserFollowersAppearance extends DoubleLevelAppearance<Following>
    {
	UserFollowersAppearance(ControlContext context) { super(context); }

	@Override public boolean isSectionItem(Following item) { return false; }

	@Override public void announceNonSection(Following item)
	{
	    final var name = requireNonNullElse(item.getDisplayName(),
						requireNonNullElse(item.getHandle(), ""));
	    app.setEventResponse(listItem(name));
	}

	@Override public String getNonSectionScreenAppearance(Following item)
	{
	    return requireNonNullElse(item.getDisplayName(),
				      requireNonNullElse(item.getHandle(), ""));
	}
    }
}
