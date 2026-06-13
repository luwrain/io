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

public class FollowingsLayout extends LayoutBase implements ListArea.ClickHandler<Following>
{
    static private final Logger log = LogManager.getLogger();

    public final List<Following> followings = new ArrayList<>();
    public final ListArea<Following> followingsArea;
    final Actions followingsActions;
    final App app;

    FollowingsLayout(App app)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();

	followingsArea = new ListArea<Following>(listParams(p -> {
		    p.name = s.followingsAreaName();
		    p.model = new ListModel<Following>(followings);
		    p.appearance = new FollowingsAppearance(getControlContext());
		    p.clickHandler = this;
		}));

	followingsActions = actions(
				   action("refresh", s.refresh(), new InputEvent(Special.F5),
					  this::onRefresh),
				   action("unfollow", s.unfollow(), new InputEvent(Special.DELETE),
					  this::onUnfollow)
				   );

	setAreaLayout(followingsArea, followingsActions);
	updateFollowings();
    }

    boolean onRefresh()
    {
	updateFollowings();
	return true;
    }

    boolean onUnfollow()
    {
	final var sel = followingsArea.selected();
	if (sel == null)
	    return false;
	if (!app.isReady())
	{
	    app.message(app.getStrings().notConfigured(), Luwrain.MessageType.ERROR);
	    return false;
	}
	final var taskId = app.newTaskId();
	return app.runTask(taskId, () -> {
		// FIXME: call BlueSky API deleteFollow
		app.finishedTask(taskId, () -> {
			followings.remove(sel);
			followingsArea.refresh();
		    });
	    });
    }

    @Override public boolean onListClick(ListArea<Following> area, int index, Following item)
    {
	final var userLayout = new UserLayout(app, item.getDid(), item.getHandle(), getReturnAction());
	app.setAreaLayout(userLayout.getAreaLayout());
	return true;
    }

    void updateFollowings()
    {
	if (!app.isReady())
	    return;
	final var taskId = app.newTaskId();
	app.runTask(taskId, () -> {
		final var res = fetchFollowings();
		app.finishedTask(taskId, () -> {
			followings.clear();
			followings.addAll(res);
			followingsArea.refresh();
		    });
	    });
    }

    List<Following> fetchFollowings()
    {
	// FIXME: call BlueSky API getFollows
	return List.of();
    }

    final class FollowingsAppearance extends DoubleLevelAppearance<Following>
    {
	FollowingsAppearance(ControlContext context) { super(context); }

	@Override public boolean isSectionItem(Following item)
	{
	    return false;
	}

	@Override public void announceNonSection(Following item)
	{
	    final var name = requireNonNullElse(item.getDisplayName(),
						requireNonNullElse(item.getHandle(), ""));
	    app.setEventResponse(listItem(name));
	}

	@Override public String getNonSectionScreenAppearance(Following item)
	{
	    final var name = requireNonNullElse(item.getDisplayName(),
						requireNonNullElse(item.getHandle(), ""));
	    final var desc = requireNonNullElse(item.getDescription(), "");
	    if (!desc.isEmpty())
		return name + " (" + desc + ")";
	    return name;
	}
    }
}
