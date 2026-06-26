// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.github;

import java.util.*;
import java.io.*;
//import java.net.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.controls.list.*;
import org.luwrain.app.base.*;
import org.luwrain.app.github.layouts.*;

import org.luwrain.io.api.github.models.*;

import org.luwrain.app.github.layouts.*;

import static org.luwrain.core.DefaultEventResponse.*;

final class MainLayout extends LayoutBase
{
    private final App app;
    final List<Repo>
	repos = new ArrayList<>(),
	searchResult = new ArrayList<>();;
    final List<Issue> pullRequests = new ArrayList<>();
    
    final ListArea<Repo> reposArea;
    final ListArea<Issue> pullRequestsArea;
    final ConsoleArea<Repo> searchArea;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();

	this.reposArea = new ListArea<Repo>(listParams(p -> {
		    p.name = s.reposAreaName();
		    p.model = new ListModel<>(repos);
		    p.appearance = new RepoAppearance(app);
		    p.clickHandler = this::onRepoClick;
		}));

	this.pullRequestsArea = new ListArea<Issue>(listParams(p -> {
		    p.name = s.pullRequestsAreaName();
		    p.model = new ListModel<>(pullRequests);
		    p.appearance = new PullRequestAppearance(app);
		    p.clickHandler = this::onPullRequestClick;
		}));

		searchArea = new ConsoleArea<Repo>(consoleParams(p -> {
			    p.model = new org.luwrain.controls.console.ListModel<Repo>(searchResult);
			    //		    p.clickHandler = (area, index, repo) -> onClick(repo);
			    //		    p.inputHandler = (area, text) -> onInput(text);
		    //		    p.appearance = this;
			}));


	setAreaLayout(AreaLayout.LEFT_RIGHT_BOTTOM, reposArea, actions(
			      action("accounts", app.getStrings().actionAccounts(), new InputEvent(InputEvent.Special.F11),
				     () -> {
					 app.setAreaLayout(new AccountsLayout(app, getReturnAction()));
					 getLuwrain().announceActiveArea();
					 return true;
				     })),

		      pullRequestsArea, actions(

		      action("refresh", app.getStrings().actionRefresh(), new InputEvent(InputEvent.Special.F5), () -> {
			      //				     this::onRefresh)
			      return true;
			  })),

	searchArea, actions());
    }

    boolean onRepoClick(ListArea<Repo> area, int index, Repo repo)
    {
	if (repo == null)
	    return false;
	// TODO: Open repo details or issues
	return false;
    }

    boolean onPullRequestClick(ListArea<Issue> area, int index, Issue issue)
    {
	if (issue == null)
	    return false;
	// TODO: Open pull request details
	return false;
    }

    boolean onRefresh()
    {
	refreshRepos();
	refreshPullRequests();
	return true;
    }

    void refreshRepos()
    {
	/*
	final var taskId = app.newTaskId();
	app.runTask(taskId, () -> {
		try {
		    final var client = app.createGitHubClient();
		    if (client == null)
		    {
			app.finishedTask(taskId, () -> app.message(app.getStrings().actionAccounts(), Luwrain.MessageType.ERROR));
			return;
		    }
		    final var fetched = client.getMyRepos();
		    app.finishedTask(taskId, () -> {
			    repos.clear();
			    repos.addAll(fetched);
			    reposArea.refresh();
			});
		}
		catch (Exception e)
		{
		    app.finishedTask(taskId, () -> app.crash(e));
		}
	    });
	*/
    }

    void refreshPullRequests()
    {
	// TODO: Fetch pull requests
    }
}
