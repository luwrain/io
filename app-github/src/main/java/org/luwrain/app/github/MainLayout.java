// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.github;

import java.util.*;
import java.io.*;
import java.net.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.controls.console.*;
import org.luwrain.popups.*;

import org.luwrain.app.base.*;

import org.luwrain.io.api.github.models.*;

import org.luwrain.app.github.layouts.*;

final class MainLayout extends LayoutBase implements ConsoleArea.Appearance<Repo>
{
    private final App app;
    final List<Repo> searchRepos = new ArrayList<>();
    final ConsoleArea<Repo> searchArea;
    
    MainLayout(App app)
    {
	super(app);
	this.app = app;
	searchArea = new ConsoleArea<Repo>(consoleParams(p -> {
		    p.model = new ListModel<>(searchRepos);
		    p.clickHandler = (area, index, repo) -> onClick(repo);
		    p.inputHandler = (area, text) -> onInput(text);
		    p.appearance = this;
		}));
	setAreaLayout(searchArea, actions(
					  action("accounts", app.getStrings().actionAccounts(), new InputEvent(InputEvent.Special.F11),
						 () -> {
						     app.setAreaLayout(new AccountsLayout(app, getReturnAction()));
						     getLuwrain().announceActiveArea();
						     return true;
						 })
					  ));
    }

    boolean onClick(Repo repo)
    {
	return false;
    }

    ConsoleArea.InputHandler.Result onInput(String text)
    {
	return null;
    }

        @Override public void announceItem(Repo repo)
    {
    }

    @Override public String getTextAppearance(Repo repo)
    {
	return "";
	    }

}
