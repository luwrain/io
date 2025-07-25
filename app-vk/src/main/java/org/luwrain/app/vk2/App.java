/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.app.vk2;

import java.util.*;
import java.util.concurrent.*;

import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.messages.ConversationWithMessage;
import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.oneofs.NewsfeedNewsfeedItemOneOf;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.events.InputEvent.*;
import org.luwrain.app.base.*;

import org.luwrain.app.vk.Strings;
import org.luwrain.app.vk.Settings;
import org.luwrain.app.vk2.layouts.*;

import static org.luwrain.core.NullCheck.*;

public final class App extends AppBase<Strings>
{
    static final String
	LOG_COMPONENT = "vk";

    static public final InputEvent
		HOT_KEY_MAIN_LAYOUT = new InputEvent(Special.F5),
	HOT_KEY_CHATS = new InputEvent(Special.F6),
		HOT_KEY_FRIENDS = new InputEvent(Special.F7),
	HOT_KEY_FRIENDSHIP_SUGGESTIONS = new InputEvent(Special.F7, EnumSet.of(Modifiers.ALT)),
	HOT_KEY_HOME_WALL = new InputEvent(Special.F9),
	HOT_KEY_PERSONAL_INFO = new InputEvent(Special.F10, EnumSet.of(Modifiers.ALT));

    public final ArrayList<UserFull>
	friends = new ArrayList<>(),
	frRequests = new ArrayList<>();

    final BirthdayUtils birthdayUtils = new BirthdayUtils(this);
    public final List<WallpostFull> homeWallPosts = new ArrayList<>();
    public final List<ConversationWithMessage> chats = new ArrayList<>();
    final List <NewsfeedNewsfeedItemOneOf> news = new ArrayList<>();

        final ConcurrentMap<Integer, UserFull> userCache = new ConcurrentHashMap<>();

    final Watching watching;
    final VkApiClient vk = new VkApiClient(new HttpTransportClient());
    private UserActor actor = null;

    private Settings sett = null;
    private Operations operations = null;
    private AuthLayout authLayout = null;
    private MainLayout mainLayout = null;
    private HomeWallLayout homeWallLayout = null;
    private ChatsLayout chatsLayout = null;
    private FriendsLayout friendsLayout = null;

    public App(Watching watching)
    {
	super(Strings.class, "luwrain.vk");
	notNull(watching, "watching");
	this.watching = watching;
    }

    @Override protected AreaLayout onAppInit()
    {
	this.sett = Settings.create(getLuwrain());
	this.authLayout = new AuthLayout(this);
	this.mainLayout = new MainLayout(this);
	this.homeWallLayout = new HomeWallLayout(this);
	this.chatsLayout = new ChatsLayout(this);
	this.friendsLayout = new FriendsLayout(this);
	setAppName(getStrings().appName());
	if (sett.getUserId(-1) == -1 || sett.getAccessToken("").isEmpty())
	    return this.authLayout.getAreaLayout();
	openInitial();
	return this.mainLayout.getAreaLayout();
    }

    	    private void openInitial()
	    {
				this.actor = new UserActor(sett.getUserId(0), sett.getAccessToken(""));
			this.operations = new Operations(this);
	final var taskId = newTaskId();
	runTask(taskId, ()->{
		final var n = operations.getNews();
final var c = operations.getChats();
		finishedTask(taskId, ()->{
			news.addAll(n);
			chats.addAll(c);
			mainLayout.chatsArea.refresh();
		    });
	    });
	    }

    void onAuth(int userId, String accessToken)
    {
	sett.setUserId(userId);
	sett.setAccessToken(accessToken);
	openInitial();
	setAreaLayout(mainLayout);
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    @Override public void onAppClose()
    {
    }

        String getUserCommonName(int userId)
    {
	if (userId < 0 || !userCache.containsKey(Integer.valueOf(userId)))
	    return String.valueOf(userId);
	final UserFull user = userCache.get(new Integer(userId));
	return user.getFirstName() + " " + user.getLastName();
    }

            boolean newFriendship(UserFull user)
    {
	if (user == null)
	    return false;
	final var taskId = newTaskId();
	return runTask(taskId, ()->{
operations.newFriendship(user.getId());
		finishedTask(taskId, ()->{
			getLuwrain().playSound(Sounds.OK);
		    });
	    });
    }

    Layouts layouts()
    {
	return new Layouts(){

	    	    @Override public boolean main()
	    {
		setAreaLayout(mainLayout);
		getLuwrain().announceActiveArea();
		return true;
			    }

	    @Override public boolean homeWall()
	    {
		final var taskId = newTaskId();
		return runTask(taskId, ()->{
			final var posts = operations.getWallPosts();
			finishedTask(taskId, ()->{
				homeWallPosts.clear();
				homeWallPosts.addAll(posts);
				homeWallLayout.wallArea.refresh();
				setAreaLayout(homeWallLayout);
			    });
		    });
	    }

	    	    @Override public boolean chats()
	    {
				setAreaLayout(chatsLayout);
				return true;
	    }


	    	    @Override public boolean friends()
	    {
		final var taskId = newTaskId();
		return runTask(taskId, ()->{
			final var  f = operations.getFriends(null);
			finishedTask(taskId, ()->{
				friends.clear();
				friends.addAll(f);
				friendsLayout.friendsArea.refresh();
				setAreaLayout(friendsLayout);
			    });
		    });
	    }

	    	    	    @Override public boolean friendshipSuggestions()
	    {
		final var taskId = newTaskId();
		return runTask(taskId, ()->{
			final var  f = operations.getFriendshipSuggestions();
			finishedTask(taskId, ()->{
				final var layout = new SuggestionsLayout(App.this, f);
				setAreaLayout(layout);
				getLuwrain().announceActiveArea();
			    });
		    });
	    }


	    	    	    @Override public boolean personalInfo()
	    {
		final var taskId = newTaskId();
		return runTask(taskId, ()->{
			final var  i = operations.getPersonalInfo();
			final var s = operations.getStatus();
			finishedTask(taskId, ()->{
				final PersonalInfoLayout layout = new PersonalInfoLayout(App.this, i, s);
				setAreaLayout(layout);
			    });
		    });
	    }
	};
    }

    UserActor getActor() { return actor; }
    public Operations getOperations() { return operations; }

    interface Layouts
    {
	boolean main();
	boolean homeWall();
	boolean chats();
	boolean friends();
	boolean friendshipSuggestions();
	boolean personalInfo();
    }
}
