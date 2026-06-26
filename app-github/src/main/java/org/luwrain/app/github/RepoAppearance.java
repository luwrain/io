// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.github;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.list.*;
import org.luwrain.io.api.github.models.*;

import static org.luwrain.core.DefaultEventResponse.*;

final class RepoAppearance extends AbstractAppearance<Repo>
{
    final private App app;

    RepoAppearance(App app)
    {
	this.app = app;
    }

    @Override public void announceItem(Repo repo, Set<Flags> flags)
    {
	final var sb = new StringBuilder();
	sb.append(repo.getName());
	if (repo.getDescription() != null && !repo.getDescription().trim().isEmpty())
	    sb.append(": ").append(repo.getDescription().trim());
	app.setEventResponse(listItem(sb.toString()));
    }

    @Override public String getScreenAppearance(Repo repo, Set<Flags> flags)
    {
	final var sb = new StringBuilder();
	sb.append(repo.getFullName());
	if (repo.getStars() > 0)
	    sb.append(" \u2B50").append(repo.getStars());
	return new String(sb);
    }
}
