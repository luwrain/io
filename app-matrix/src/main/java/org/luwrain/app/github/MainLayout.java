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
	setAreaLayout(searchArea, null);
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
